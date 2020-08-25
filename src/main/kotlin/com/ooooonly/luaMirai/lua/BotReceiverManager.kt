package com.ooooonly.luaMirai.lua

import net.mamoe.mirai.Bot

object BotReceiverManager {
    private val receivers = mutableSetOf<BotReceivable>()

    interface BotReceivable {
        fun receiveBot(bot: Bot)
    }

    fun addReceiver(receiver: BotReceivable) {
        receivers.add(receiver)
        Bot.forEachInstance { receiver.receiveBot(it) }
    }

    fun removeReceiver(receiver: BotReceivable) = receivers.remove(receiver)
    fun noticeReceivers(bot: Bot) = receivers.forEach { it.receiveBot(bot) }
}