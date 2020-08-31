package com.ooooonly.luaMirai.frontend.web.verticals

import com.fasterxml.jackson.databind.ObjectMapper
import com.ooooonly.luaMirai.frontend.web.entities.BotCreateInfo
import com.ooooonly.luaMirai.frontend.web.entities.BotInfo
import com.ooooonly.luaMirai.frontend.web.entities.StatusResponse
import com.ooooonly.luaMirai.frontend.web.mirai.WebBotConfiguration
import com.ooooonly.luaMirai.frontend.web.utils.*
import com.ooooonly.luaMirai.lua.BotReceiverManager
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.auth.AuthProvider
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
    val botChannel = "bot"
    val netChannel = "net"
    val sockJsPath = "/eb/*"
    val apiPath = "/api"
    val loginPagePath = "/loginpage.html"

    override suspend fun start() {
        eventBus = vertx.eventBus()
        val mainRouter = vertx.createRouter()
        with(mainRouter.route()) {
            handler(BodyHandler.create())
            handler(SessionHandler.create(LocalSessionStore.create(vertx)))
            handler(StaticHandler.create())
            handler(ResponseContentTypeHandler.create())
            failureHandler {
                it.failure().printStackTrace()
                it.responseObjectEnd(StatusResponse.ERROR(it.failure().message ?: ""))
            }
        }
        val authProvider = SimpleFixAuthProvider("admin", "ooo")
        mainRouter.initSockJs()
        mainRouter.initAuthApi(authProvider)
        mainRouter.initRedirector(
            protectPaths = listOf("$apiPath/*"),
            redirectUrl = loginPagePath,
            authProvider = authProvider
        )
        mainRouter.subRouter("$apiPath/v1", vertx) { initApi() }
        mainRouter.route().handler(StaticHandler.create())
        vertx.createHttpServer().requestHandler(mainRouter).listen()
    }

    fun Router.initSockJs() {
        route(sockJsPath).handler(buildSockJsHandler(vertx) {
            addOutboundAddressRegex("$botChannel\\d*")
            addOutboundAddressRegex("$netChannel\\d*")
        })
    }

    fun Router.initRedirector(protectPaths: List<String>, redirectUrl: String, authProvider: AuthProvider) {
        val redirectAuthProvider = RedirectAuthHandler.create(authProvider, redirectUrl)
        protectPaths.forEach {
            route(it).handler(redirectAuthProvider)
        }
    }

    fun Router.initAuthApi(authProvider: AuthProvider): Router = apply {
        post("/auth").handler(FormLoginHandler.create(authProvider))
        delete("/auth").handler { context: RoutingContext ->
            context.clearUser()
            context.response().putHeader("location", "/").setStatusCode(302).end()
        }
    }

    fun Router.initApi() {
        jsonRoute()
        subRouter("/bots", vertx) { initBotApi() }
        subRouter("/scripts", vertx) { initScriptApi() }
    }

    fun Router.initBotApi() {
        getHandler {
            responseObjectEnd(BotInfo.fromBots())
        }
        getHandler("/:botId") {
            responseObjectEnd(BotInfo.fromBot(pathParam("botId").toLong()))
        }
        postCoroutineHandler(coroutineScope = this@LuaMiraiVertical) {
            withContext(Dispatchers.IO) {
                val info = getBodyAsObject<BotCreateInfo>()
                val bot = Bot(info.id, info.password, WebBotConfiguration(eventBus, botChannel, netChannel))
                bot.login()
                BotReceiverManager.noticeReceivers(bot)
                responseObjectEnd(StatusResponse.OK("成功创建Bot${info.id}！"))
            }
        }
        deleteCoroutineHandler("/:botId", this@LuaMiraiVertical) {
            Bot.getInstanceOrNull(pathParam("botId").toLong())?.apply {
                close()
                responseObjectEnd(StatusResponse.OK())
            } ?: responseObjectEnd(StatusResponse.ERROR("No such bot"))
        }
    }

    fun Router.initScriptApi() = apply {

    }

    fun periodicPublishTest() {
        vertx.setPeriodic(1000) {
            eventBus.publish("bot", "现在时间是：" + Date()) //发布消息
        }
    }
}
