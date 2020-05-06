package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.utils.MessageAnalyzer
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.*
import org.luaj.vm2.LuaInteger
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import java.lang.StringBuilder

class MiraiMsg : LuaMsg {
    var raw: StringBuilder = StringBuilder()
    var source: MessageSource? = null
    var bot: Bot?

    constructor(messageChain: MessageChain, bot: Bot) {
        var builder: StringBuilder = StringBuilder()
        messageChain.forEachContent {
            if (it is MessageSource) source = it
            else builder.append(it.toString())
        }
        this.bot = bot
        this.raw = builder
    }

    constructor(text: String) {
        raw.append(text);source = null;bot = null;
    }

    constructor(message: Message) : this(message.toString()) {
        source = null;bot = null;
    }

    constructor() : this("")

    override fun getOpFunction(opcode: Int): OpFunction? {
        return object : OpFunction(opcode) {
            override fun op(varargs: Varargs): Varargs {
                val luaMsg = varargs.arg1() as MiraiMsg
                when (opcode) {
                    APPEND_TEXT -> luaMsg.raw.append(varargs.optjstring(2, ""))
                    APPEND_FACE -> luaMsg.raw.append("[mirai:face:${varargs.optint(2, 0)}]")
                    APPEND_IMAGE -> luaMsg.raw.append("[mirai:image:${varargs.optjstring(2, "")}]")
                    APPEND_AT -> {
                        val member = varargs.arg(2)
                        if (member is LuaGroupMember) luaMsg.raw.append("[mirai:at:${member.id}]")
                        if (member is LuaInteger) luaMsg.raw.append("[mirai:at:${member.checklong()}]")
                    }
                    APPEND_AT_ALL -> luaMsg.raw.append("[mirai:atall]")
                    APPEND_LONG_TEXT -> luaMsg.raw.append("[mirai:long:${varargs.optjstring(2, "")}]")
                    APPEND_SHARE -> {
                    }
                    GET_QUOTE -> if (source != null) return MiraiMsg(source!!)
                    RECALL -> runBlocking { if (source != null && bot != null) bot!!.recall(source!!) }
                    GET_SOURCE -> if (source != null && bot != null) MiraiSource(source = source!!, bot = bot!!)
                }
                return luaMsg
            }
        }
    }

    fun getChain(contact: Contact?): MessageChain {
        return MessageAnalyzer.toMessageChain(raw, contact = contact)
    }


    override fun getPlain() = raw.toString()
}