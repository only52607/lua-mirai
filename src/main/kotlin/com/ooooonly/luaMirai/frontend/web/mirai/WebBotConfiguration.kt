package com.ooooonly.luaMirai.frontend.web.mirai

import com.ooooonly.luaMirai.frontend.web.Config
import io.vertx.core.eventbus.EventBus
import io.vertx.core.json.JsonObject
import net.mamoe.mirai.Bot
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.LoginSolver
import net.mamoe.mirai.utils.SimpleLogger

class WebBotConfiguration(eventBus: EventBus) : BotConfiguration() {
    init {
        botLoggerSupplier = {
            SimpleLogger("") { message, e ->
                println(message)
                eventBus.publish(
                    Config.Eventbus.LOG,
                    JsonObject().put("type", "bot").put("from", it.id).put("message", message).encode()
                )
            }
        }
        networkLoggerSupplier = {
            SimpleLogger("") { message, e ->
                println(message)
                eventBus.publish(
                    Config.Eventbus.LOG,
                    JsonObject().put("type", "net").put("from", it.id).put("message", message).encode()
                )
            }
        }
    }
}