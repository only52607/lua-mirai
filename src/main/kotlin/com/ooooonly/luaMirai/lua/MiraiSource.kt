package com.ooooonly.luaMirai.lua

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.data.MessageSource
import net.mamoe.mirai.message.recall
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs

class MiraiSource(var receipt: MessageReceipt<Contact>?, var source: MessageSource, var bot: Bot) : LuaSource() {
    constructor(receipt: MessageReceipt<Contact>) : this(receipt, receipt.source, receipt.target.bot)
    constructor(source: MessageSource, bot: Bot) : this(null, source, bot)

    override fun getOpFunction(opcode: Int): OpFunction = object : OpFunction(opcode) {
        override fun op(varargs: Varargs): Varargs = when (opcode) {
            RECALL -> runBlocking {
                receipt?.recall() ?: bot.recall(source)
                LuaValue.NIL
            }
            //GET_QUOTE -> MiraiMsg(source)
            //GET_BOT -> MiraiBot(bot)
            else -> LuaValue.NIL
        }
    }

}