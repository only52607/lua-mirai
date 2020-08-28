package com.ooooonly.luaMirai.lua

import net.mamoe.mirai.Bot

//管理可接受bot推送的对象，以便在创建bot时将bot推送给对象
object BotReceiverManager {
    private val receivers = mutableSetOf<BotReceivable>()

    fun addReceiver(receiver: BotReceivable) {
        receivers.add(receiver)
        Bot.forEachInstance { receiver.receiveBot(it) }
    }
    fun removeReceiver(receiver: BotReceivable) = receivers.remove(receiver)
    fun noticeReceivers(bot: Bot) = receivers.forEach { it.receiveBot(bot) }
}