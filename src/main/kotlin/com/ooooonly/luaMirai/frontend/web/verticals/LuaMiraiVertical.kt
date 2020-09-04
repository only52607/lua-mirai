package com.ooooonly.luaMirai.frontend.web.verticals

import com.ooooonly.luaMirai.frontend.web.Config
import com.ooooonly.luaMirai.frontend.web.entities.BotCreateInfo
import com.ooooonly.luaMirai.frontend.web.entities.BotInfo
import com.ooooonly.luaMirai.frontend.web.entities.FileInfo
import com.ooooonly.luaMirai.frontend.web.mirai.WebBotConfiguration
import com.ooooonly.luaMirai.frontend.web.utils.*
import com.ooooonly.luaMirai.lua.ScriptManager
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.JsonArray
import io.vertx.ext.auth.AuthProvider
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.ext.web.handler.ResponseContentTypeHandler
import io.vertx.ext.web.handler.SessionHandler
import io.vertx.ext.web.handler.StaticHandler
import io.vertx.ext.web.sstore.LocalSessionStore
import io.vertx.kotlin.coroutines.CoroutineVerticle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.Bot
import java.io.File
import java.util.*


class LuaMiraiVertical:CoroutineVerticle() {
    private lateinit var eventBus: EventBus
    private lateinit var authProvider:AuthProvider
    private val botChannel = "bot"
    private val netChannel = "net"
    private val sockJsPath = "/eb/*"
    companion object{
        const val API = "/api/v1"
        const val AUTH = "auth"
        const val BOTS = "bots"
        const val SCRIPTS = "scripts"
        const val FILES = "files"
    }
    override suspend fun start() {
        eventBus = vertx.eventBus()
        authProvider = SimpleFixAuthProvider("admin", "ooo")
        val mainRouter = vertx.createRouter()
        with(mainRouter.route()) {
            handler(BodyHandler.create().setUploadsDirectory(Config.PATH_UPLOAD).setDeleteUploadedFilesOnEnd(true))
            handler(SessionHandler.create(LocalSessionStore.create(vertx)))
            handler(StaticHandler.create())
            handler(ResponseContentTypeHandler.create())
            failureHandler {
                it.failure().printStackTrace()
                it.responseServerErrorEnd(it.failure().message?:"")
            }
        }
        mainRouter.route(sockJsPath).handler(buildSockJsHandler(vertx) {
            addOutboundAddressRegex("$botChannel\\d*")
            addOutboundAddressRegex("$netChannel\\d*")
        })
        mainRouter.route(API.anySubPath()).handlerApply {
            if(request().path() == API.subPath(AUTH)) return@handlerApply next()
            user()?.let { next() }?:responseUnauthorizedEnd("请先登录！")
        }
        vertx.createSubRouter(mainRouter,API,::api)
        vertx.createHttpServer().requestHandler(mainRouter).listen()
    }
    private fun api(router:Router) {
        vertx.createSubRouter(router,AUTH.asSubPath(),::apiAuth)
        vertx.createSubRouter(router,BOTS.asSubPath(),::apiBot)
        vertx.createSubRouter(router,SCRIPTS.asSubPath(),::apiScript)
        vertx.createSubRouter(router,FILES.asSubPath(),::apiFile)
    }
    private fun apiAuth(router:Router) {
        router.handleJson()
        router.apply {
            postHandlerApply {
                authProvider.authenticate(bodyAsJson){ u ->
                    when(u.succeeded()){
                        true -> {
                            setUser(u.result())
                            responseStatusMessageEnd(200,"验证通过！")
                        }
                        false -> responseUnauthorizedEnd("验证失败！")
                    }
                }
            }
            deleteHandlerApply(ROOT) {
                clearUser()
                responseRedirectEnd(ROOT)
            }
        }
    }
    private fun apiBot(router:Router) {
        router.handleJson()
        router.apply {
            getHandlerApply(ROOT) {
                responseEnd(BotInfo.fromBots())
            }
            getHandlerApply("/:botId") {
                responseEnd(BotInfo.fromBot(pathParam("botId").toLong()))
            }
            postCoroutineHandlerApply(ROOT, this@LuaMiraiVertical) {
                withContext(Dispatchers.IO) {
                    val info = getBodyAsObject<BotCreateInfo>()
                    val bot = Bot(info.id, info.password, WebBotConfiguration(eventBus, botChannel, netChannel))
                    bot.login()
                    ScriptManager.loadBot(bot)
                    responseCreatedEnd("成功创建Bot${info.id}！")
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
                val scriptFile = File(Config.PATH_UPLOAD,data.getString("filename"))
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
                ScriptManager.listScript().forEach {
                    scriptInfos.add(it.file.name)
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
            postHandlerApply(ROOT) {
                response().isChunked = true
                for (f in fileUploads()) {
                    val file = File(f.uploadedFileName())
                    val newFile = File(file.parentFile,f.fileName())
                    if (newFile.exists()) {
                        file.delete()
                    }else{
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
                val newFile = File(Config.PATH_UPLOAD,pathParam("filename"))
                if (newFile.exists()) {
                    newFile.delete()
                }
                File(fileUploads().first().uploadedFileName()).renameTo(newFile)
                responseCreatedEnd("更新脚本成功!")
            }
        }
    }
    fun periodicPublishTest() {
        vertx.setPeriodic(1000) {
            eventBus.publish("bot", "现在时间是：" + Date()) //发布消息
        }
    }
}
