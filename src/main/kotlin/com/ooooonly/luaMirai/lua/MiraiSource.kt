package com.ooooonly.luaMirai.lua

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.data.MessageSource
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs

class MiraiSource : LuaSource {

    var source: MessageSource
    var bot: Bot

    constructor(receipt: MessageReceipt<Contact>) {
        this.source = receipt.source
        this.bot = receipt.target.bot
    }

    constructor(source: MessageSource, bot: Bot) {
        this.source = source
        this.bot = bot
    }

    override fun getOpFunction(opcode: Int): OpFunction {
        return object : OpFunction(opcode) {
            override fun op(varargs: Varargs?): Varargs {
                return when (opcode) {
                    GET_QUOTE -> MiraiMsg(source)
                    RECALL -> runBlocking { bot.recall(source);LuaValue.NIL }
                    GET_BOT -> MiraiBot(bot)
                    else -> LuaValue.NIL
                }
            }

        }
    }
}