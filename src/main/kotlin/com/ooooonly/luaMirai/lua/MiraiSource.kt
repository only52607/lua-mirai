package com.ooooonly.luaMirai.lua

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.data.MessageSource
import net.mamoe.mirai.message.recall
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs

class MiraiSource(var receipt: MessageReceipt<Contact>?, var source: MessageSource, var bot: Bot) : LuaSource() {
    constructor(receipt: MessageReceipt<Contact>) : this(receipt, receipt.source, receipt.target.bot)
    constructor(source: MessageSource, bot: Bot) : this(null, source, bot)

    override fun getOpFunction(opcode: Int): OpFunction = object : OpFunction(opcode) {
        override fun op(varargs: Varargs): Varargs = varargs.arg1().let {
            if (it !is MiraiSource) throw LuaError("The reference object must be MiraiSource")
            when (opcode) {
                RECALL -> runBlocking {
                    it.receipt?.recall() ?: bot.recall(it.source)
                    LuaValue.NIL
                }
                //GET_QUOTE -> MiraiMsg(source)
                //GET_BOT -> MiraiBot(bot)
                else -> LuaValue.NIL
            }

        }
    }
}