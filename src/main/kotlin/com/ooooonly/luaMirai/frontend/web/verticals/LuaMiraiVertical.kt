package com.ooooonly.luaMirai.frontend.web.verticals

import com.ooooonly.luaMirai.frontend.web.Config
import com.ooooonly.luaMirai.frontend.web.entities.BotCreateInfo
import com.ooooonly.luaMirai.frontend.web.entities.BotInfo
import com.ooooonly.luaMirai.frontend.web.entities.FileInfo
import com.ooooonly.luaMirai.frontend.web.mirai.WebBotConfiguration
import com.ooooonly.luaMirai.frontend.web.utils.*
import com.ooooonly.luaMirai.lua.ScriptManager
import io.vertx.core.eventbus.EventBus
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.AuthProvider
import io.vertx.ext.auth.JWTOptions
import io.vertx.ext.auth.KeyStoreOptions
import io.vertx.ext.auth.PubSecKeyOptions
import io.vertx.ext.auth.jwt.JWTAuth
import io.vertx.ext.auth.jwt.JWTAuthOptions
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.*
import io.vertx.ext.web.sstore.LocalSessionStore
import io.vertx.kotlin.core.http.sendFileAwait
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.ext.auth.pubSecKeyOptionsOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.Bot
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.util.*


class LuaMiraiVertical:CoroutineVerticle() {
    private lateinit var eventBus: EventBus
    private lateinit var authProvider: JWTAuth

    private val sockJsPath = "/eb/*"

    companion object {
        const val API = "/api/v1"
        const val AUTH = "auth"
        const val BOTS = "bots"
        const val SCRIPTS = "scripts"
        const val COMMAND = "command"
        const val FILES = "files"
    }
    override suspend fun start() {
        eventBus = vertx.eventBus()
        authProvider = JWTAuth.create(
            vertx, JWTAuthOptions()
                .addPubSecKey(
                    pubSecKeyOptionsOf(
                        algorithm = "HS256",
                        publicKey = "oooonly",
                        secretKey = "ooooonlyok",
                        symmetric = true
                    )
                )
        )
        val mainRouter = vertx.createRouter()

        with(mainRouter.route()) {
            handlerApply {
                response().putHeader(
                    "Access-Control-Allow-Headers",
                    "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With, Authorization"
                )
                response().putHeader("Access-Control-Expose-Headers", "Authorization")
                next()
            }
            handler(
                CorsHandler.create("*")
                    .allowedMethod(HttpMethod.GET)
                    .allowedMethod(HttpMethod.POST)
                    .allowedMethod(HttpMethod.PUT)
                    .allowedMethod(HttpMethod.DELETE)
            )

            handler(BodyHandler.create().setUploadsDirectory(Config.PATH_UPLOAD).setDeleteUploadedFilesOnEnd(true))
//            handler(SessionHandler.create(LocalSessionStore.create(vertx)))
            handler(StaticHandler.create())
            handler(ResponseContentTypeHandler.create())
            failureHandler {
                it.failure().printStackTrace()
                it.responseServerErrorEnd(it.failure().message ?: "")
            }
        }
        mainRouter.route(sockJsPath).handler(buildSockJsHandler(vertx) {
            addOutboundAddressRegex(Config.EVENTBUS_LOG)
        })
        mainRouter.route(API.anySubPath()).handlerApply {
            if (request().path() == API.subPath(AUTH)) return@handlerApply next()
            authProvider.authenticate(JsonObject().put("jwt", request().getHeader("Authorization"))) {
                when (it.succeeded()) {
                    true -> next()
                    false -> responseUnauthorizedEnd("请先登录！")
                }
            }
        }
        vertx.createSubRouter(mainRouter,API,::api)
        vertx.createHttpServer().requestHandler(mainRouter).listen()
    }
    private fun api(router:Router) {
        vertx.createSubRouter(router,AUTH.asSubPath(),::apiAuth)
        vertx.createSubRouter(router,BOTS.asSubPath(),::apiBot)
        vertx.createSubRouter(router,SCRIPTS.asSubPath(),::apiScript)
        vertx.createSubRouter(router, FILES.asSubPath(), ::apiFile)
        vertx.createSubRouter(router, COMMAND.asSubPath(), ::apiCommand)
    }
    private fun apiAuth(router:Router) {
        router.handleJson()
        router.apply {
            postHandlerApply {
                val username = bodyAsJson.getString("username")
                val password = bodyAsJson.getString("password")
                if ("admin" == username && "ooo" == password) {
                    response().putHeader(
                        "Authorization", authProvider.generateToken(
                            JsonObject().put("auth", true),
                            JWTOptions()
                        )
                    )
                    responseOkEnd("验证通过！")
                } else {
                    responseUnauthorizedEnd("验证失败！")
                }
            }
            getHandlerApply {
                authProvider.authenticate(JsonObject().put("jwt", request().getHeader("Authorization"))) {
                    when (it.succeeded()) {
                        true -> responseOkEnd(it.result().principal())
                        false -> responseUnauthorizedEnd("验证失败！")
                    }
                }
            }
        }
    }
    private fun apiBot(router:Router) {
        router.handleJson()
        router.apply {
            getHandlerApply(ROOT) {
                responseEnd(BotInfo.fromBots().filter { it.isOnline })
            }
            getHandlerApply("/:botId") {
                responseEnd(BotInfo.fromBot(pathParam("botId").toLong()))
            }
            postCoroutineHandlerApply(ROOT, this@LuaMiraiVertical) {
                var exception: Exception? = null
                val result = withContext(Dispatchers.IO) {
                    val info = getBodyAsObject<BotCreateInfo>()
                    println(info.id)
                    println(info.password)
                    val bot = Bot(info.id, info.password, WebBotConfiguration(eventBus))
                    try {
                        bot.login()
                        ScriptManager.loadBot(bot)
                        true
                    } catch (e: Exception) {
                        exception = e
                        false
                    }
                }
                if (result) {
                    responseCreatedEnd("创建成功")
                } else {
                    responseServerErrorEnd("创建失败！\n$exception")
                }
            }
            deleteCoroutineHandlerApply("/:botId", this@LuaMiraiVertical) {
                val bot = Bot.getInstanceOrNull(pathParam("botId").toLong())
                bot ?: return@deleteCoroutineHandlerApply responseNotFoundEnd("不存在此bot！")
                bot.close()
                responseDeletedEnd("删除成功！")
            }
        }
    }
    private fun apiScript(router:Router) {
        router.handleJson()
        router.apply {
            postCoroutineHandlerApply(ROOT,this@LuaMiraiVertical) {
                val data = bodyAsJson
                val scriptFile = File(Config.PATH_UPLOAD, data.getString("name"))
                if (!scriptFile.exists()) return@postCoroutineHandlerApply responseNotFoundEnd("此脚本不存在!")
                try {
                    ScriptManager.addScript(scriptFile)
                    responseCreatedEnd("添加脚本成功！")
                }catch (e:Exception){
                    responseInvalidEnd("脚本执行失败！\n${e}")
                }
            }
            getHandlerApply(ROOT) {
                val scriptInfos = JsonArray()
                ScriptManager.listScript().forEach { script ->
                    scriptInfos.add(JsonObject().apply {
                        put("name", script.file.name)
                        put("file", script.file.name)
                        put("version", "v1.2")
                        put("description", "测试")
                        put("author", "oooonly")
                    })
                }
                responseOkEnd(scriptInfos)
            }
            deleteHandlerApply("/:index") {
                val index = pathParam("index").toInt()
                if (index >= ScriptManager.listScript().size || index < 0) return@deleteHandlerApply responseNotFoundEnd("索引不存在！")
                ScriptManager.removeScript(index)
                responseDeletedEnd("删除成功！")
            }
        }
    }
    private fun apiFile(router: Router){
        router.apply {
            getCoroutineHandlerApply("/:filename/raw", this@LuaMiraiVertical) {
                val file = File(Config.PATH_UPLOAD, pathParam("filename"))
                if (!file.exists()) return@getCoroutineHandlerApply responseNotFoundEnd("不存在此文件！")
                var data = ""
                withContext(Dispatchers.IO) {
                    data = Base64.getEncoder().encodeToString(file.readBytes())
                }
                responseOkEnd(data)
            }
            putCoroutineHandlerApply("/:filename/raw", this@LuaMiraiVertical) {
                val file = File(Config.PATH_UPLOAD, pathParam("filename"))
                withContext(Dispatchers.IO) {
                    FileOutputStream(file).apply {
                        write(Base64.getDecoder().decode(bodyAsString))
                    }.close()
                }
                responseCreatedEnd("更新成功！")
            }
            putCoroutineHandlerApply("/:filename/name", this@LuaMiraiVertical) {
                val file = File(Config.PATH_UPLOAD, pathParam("filename"))
                if (!file.exists()) return@putCoroutineHandlerApply responseNotFoundEnd("不存在此文件！")
                val newName = JsonObject(bodyAsString).getString("name")
                file.renameTo(File(file.parentFile, newName))
                responseCreatedEnd("更新成功！")
            }
            getCoroutineHandlerApply("/:filename/file", this@LuaMiraiVertical) {
                val file = File(Config.PATH_UPLOAD, pathParam("filename"))
                if (!file.exists()) return@getCoroutineHandlerApply responseNotFoundEnd("不存在此文件！")
                response().putHeader("content-Type", "text/plain");
                response().putHeader("Content-Disposition", "attachment;filename=" + file.name);
                response().sendFileAwait(file.absolutePath)
                response().end()
            }
            postHandlerApply(ROOT) {
                response().isChunked = true
                for (f in fileUploads()) {
                    val file = File(f.uploadedFileName())
                    val newFile = File(file.parentFile, f.fileName())
                    if (newFile.exists()) {
                        file.delete()
                    } else {
                        file.renameTo(newFile)
                    }
                }
                responseCreatedEnd("上传脚本成功!")
            }

            getHandlerApply(ROOT) {
                responseOkEnd(FileInfo.fromFiles(File(Config.PATH_UPLOAD).listFiles()))
            }
            getHandlerApply("/:filename") {
                responseOkEnd(FileInfo.fromFile(File(Config.PATH_UPLOAD,pathParam("filename"))))
            }
            deleteHandlerApply("/:filename") {
                val file = File(Config.PATH_UPLOAD,pathParam("filename"))
                if(!file.exists()) return@deleteHandlerApply responseNotFoundEnd("不存在此脚本！")
                file.delete()
                responseDeletedEnd("删除脚本成功!")
            }
            putHandlerApply("/:filename") {
                val newFile = File(Config.PATH_UPLOAD, pathParam("filename"))
                if (newFile.exists()) {
                    newFile.delete()
                }
                File(fileUploads().first().uploadedFileName()).renameTo(newFile)
                responseCreatedEnd("更新脚本成功!")
            }
        }
    }

    private fun apiCommand(router: Router) {
        router.handleJson()
        router.apply {
            postHandlerApply {
                val command = bodyAsJson.getString("command")

                responseOkEnd("")
            }
        }
    }

    fun periodicPublishTest() {
        vertx.setPeriodic(1000) {
            eventBus.publish("bot", "现在时间是：" + Date()) //发布消息
        }
    }
}
