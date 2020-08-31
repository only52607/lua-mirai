package com.ooooonly.luaMirai.frontend.web.entities

import net.mamoe.mirai.Bot

data class BotInfo(
    val id: Long = 0,
    val nick: String = "",
    val avatarUrl: String = "",
    val isOnline: Boolean = false
) {
    companion object {
        val EmptyInfo by lazy {
            BotInfo()
        }

        fun fromBot(bot: Bot): BotInfo = try {
            BotInfo(bot.id, bot.nick, bot.selfQQ.avatarUrl, bot.isOnline)
        } catch (e: Exception) {
            BotInfo(bot.id, "", "", false)
        }

        fun fromBot(id: Long): BotInfo = Bot.getInstanceOrNull(id)?.let(::fromBot) ?: EmptyInfo
        fun fromBots(): List<BotInfo> = Bot.botInstances.map { fromBot(it) }
    }
}