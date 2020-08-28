package com.ooooonly.luaMirai.lua

import net.mamoe.mirai.Bot

interface BotReceivable {
    fun receiveBot(bot: Bot)
}