package com.ooooonly.luaMirai.frontend.web.verticals

import com.ooooonly.luaMirai.frontend.web.entities.BotInfo
import com.ooooonly.luaMirai.frontend.web.mirai.WebBotConfiguration
import com.ooooonly.luaMirai.frontend.web.utils.UserNameAndPasswordProvider
import com.ooooonly.luaMirai.lua.BotReceiverManager
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.AuthProvider
import io.vertx.ext.bridge.BridgeEventType
import io.vertx.ext.bridge.PermittedOptions
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.*
import io.vertx.ext.web.handler.sockjs.SockJSBridgeOptions
import io.vertx.ext.web.handler.sockjs.SockJSHandler
import io.vertx.ext.web.sstore.LocalSessionStore
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import io.vertx.kotlin.ext.web.handler.sockjs.sockJSHandlerOptionsOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import net.mamoe.mirai.Bot
import net.mamoe.mirai.closeAndJoin
import java.util.*


class LuaMiraiVertical:CoroutineVerticle() {
    lateinit var eventBus: EventBus
    override suspend fun start() {
        val options: SockJSBridgeOptions = SockJSBridgeOptions()
            .addInboundPermitted(PermittedOptions().setAddress("to_publish")) //注册服务器地址
            .addOutboundPermitted(PermittedOptions().setAddress("to_client")) //注册客户端地址

        val ebHandler: SockJSHandler = SockJSHandler.create(vertx, sockJSHandlerOptionsOf(heartbeatInterval = 2000)).apply { bridge(options) }

        eventBus = vertx.eventBus()
        val mainRouter = Router.router(vertx)

        mainRouter.route("/eb/*").handler(ebHandler)

        with(mainRouter.route()){
            handler(BodyHandler.create())
            handler(SessionHandler.create(LocalSessionStore.create(vertx)))
            //handler(StaticHandler.create())
            failureHandler {
                it.failure().printStackTrace()
            }
        }

        val authProvider = UserNameAndPasswordProvider()
        mainRouter.initAuthApi(authProvider)
        mainRouter.initRedirector(
            protectPaths = listOf("/api/*","/index.html","/"),
            redirectUrl = "/loginpage.html",
            authProvider = authProvider
        )
        mainRouter.mountSubRouter("/api/v1",Router.router(vertx).initApi())


        mainRouter.route().handler(StaticHandler.create())

        vertx.setPeriodic(1000) {
            vertx.eventBus().publish("to_client", "现在时间是：" + Date() ) //发布消息
        }

        vertx.createHttpServer().requestHandler(mainRouter).listen()
    }

    fun Router.initRedirector(protectPaths:List<String>,redirectUrl:String,authProvider: AuthProvider){
        val redirectAuthProvider = RedirectAuthHandler.create(authProvider,redirectUrl)
        protectPaths.forEach {
            route(it).handler(redirectAuthProvider)
            route(it).handler(redirectAuthProvider)
        }
    }
    fun Router.initAuthApi(authProvider: AuthProvider):Router = apply {
        route().handler(ResponseContentTypeHandler.create())
        post("/auth").handler(FormLoginHandler.create(authProvider))
        delete("/auth").handler { context: RoutingContext ->
            context.clearUser()
            context.response().putHeader("location", "/").setStatusCode(302).end()
        }
    }
    fun Router.initApi():Router = apply{
        mountSubRouter("/bots", Router.router(vertx).initBotApi())
        mountSubRouter("/scripts", Router.router(vertx).initScriptApi())
    }
    fun Router.initBotApi():Router = apply{
        get("/").produces("application/json").handler{ context ->
            context.response().end(JsonArray(BotInfo.fromBots()).encodePrettily())
        }
        post("/").produces("application/json").coroutineHandler {  context ->
            val bodyJson = JsonObject(context.bodyAsString)
            val jsonObject = withContext(Dispatchers.IO){
                val bot = Bot(bodyJson.getLong("id"), bodyJson.getString("password"),WebBotConfiguration{

                })
                bot.login()
                BotReceiverManager.noticeReceivers(bot)
                json {
                    obj("message" to "ok")
                }
            }
            context.response().end(jsonObject.encodePrettily())
        }
        delete("/:botId").produces("application/json").coroutineHandler { context ->
            Bot.getInstanceOrNull(context.pathParam("botId").toLong())?.apply {
                closeAndJoin()
                context.response().end(json { obj { "message" to "ok!" } }.encode())
            }?:context.response().end(json { obj { "message" to "failed!" } }.encode())
        }
    }

    fun Router.initScriptApi():Router = apply {

    }
    fun Route.coroutineHandler(fn: suspend (RoutingContext) -> Unit) {
        handler { ctx ->
            launch(ctx.vertx().dispatcher()) {
                try {
                    fn(ctx)
                } catch (e: Exception) {
                    ctx.fail(e)
                }
            }
        }
    }
}
