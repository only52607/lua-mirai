package com.ooooonly.luaMirai.frontend.web.mirai

import io.vertx.core.eventbus.EventBus
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.SimpleLogger

class WebBotConfiguration(eventBus: EventBus, botChannel: String, netChannel: String) : BotConfiguration() {
    init {
        botLoggerSupplier = {
            SimpleLogger("Bot [${it.id}] ") { message, e ->
                println(message)
                eventBus.publish(botChannel, message)
                eventBus.publish("$botChannel$it.id", message)
            }
        }
        networkLoggerSupplier = {
            SimpleLogger("Net [${it.id}] ") { message, e ->
                println(message)
                eventBus.publish(netChannel, message)
                eventBus.publish("$netChannel$it.id", message)
            }
        }
    }
}