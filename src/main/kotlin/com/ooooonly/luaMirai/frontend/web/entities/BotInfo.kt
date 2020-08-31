package com.ooooonly.luaMirai.frontend.web.entities

import net.mamoe.mirai.Bot

data class BotInfo(
    val id:Long,
    val nick:String,
    val avatarUrl:String,
    val isOnline:Boolean
) {
    companion object{
        fun fromBot(bot: Bot):BotInfo = try {
            BotInfo(bot.id,bot.nick,bot.selfQQ.avatarUrl,bot.isOnline)
        }catch (e:Exception){
            BotInfo(bot.id,"","",false)
        }
        fun fromBots():List<BotInfo> = Bot.botInstances.map { fromBot(it) }
    }
}