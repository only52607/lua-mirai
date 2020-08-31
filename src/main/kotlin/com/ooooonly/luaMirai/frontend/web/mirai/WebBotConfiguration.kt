package com.ooooonly.luaMirai.frontend.web.mirai

import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.SimpleLogger

class WebBotConfiguration(val logger:(message:String?)->Unit):BotConfiguration() {
    init {
        botLoggerSupplier = {
            SimpleLogger("") { message, e ->
                println(message)
                logger(message)
            }
        }
        networkLoggerSupplier = botLoggerSupplier
    }
}