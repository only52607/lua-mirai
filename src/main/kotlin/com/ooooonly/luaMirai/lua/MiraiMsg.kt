package com.ooooonly.luaMirai.lua

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.uploadImage
import org.luaj.vm2.*
import java.net.URL

class MiraiMsg : LuaMsg {
    var chain: MessageChain = EmptyMessageChain
    var raw: StringBuilder = StringBuilder()

    // var source: MessageSource? = null
    var quote: MessageSource? = null
    var bot: Bot? = null

/*
    constructor(messageChain: MessageChain, bot: Bot) {
        var builder: StringBuilder = StringBuilder()
        messageChain.forEach {
            if (it is QuoteReply) quote = it.source
            else if (it is MessageSource) source = it
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
                    APPEND_POKE -> luaMsg.raw.append("[mirai:poke:${varargs.optjstring(2, "")}]")
                    APPEND_FORWARD -> luaMsg.raw.append("[mirai:forward:${varargs.optjstring(2, "")}]")
                    APPEND_LONG_TEXT -> luaMsg.raw.append("[mirai:long:${varargs.optjstring(2, "")}]")
                    //APPEND_SHARE -> {}
                    SET_QUOTE -> {
                        val quoteArg = varargs.arg(2)
                        if (quoteArg is MiraiSource) {
                            quote = quoteArg.source
                        } else if (quoteArg is MiraiMsg) {
                            quote = quoteArg.source
                        }
                    }
                    GET_QUOTE -> quote?.let { return MiraiSource(quote!!, bot!!) }
                    RECALL -> runBlocking { if (source != null && bot != null) bot!!.recall(source!!) }
                    GET_SOURCE -> if (source != null && bot != null) return MiraiSource(source = source!!, bot = bot!!)
                }
                return luaMsg
            }
        }
    }

    fun getChain(contact: Contact?): MessageChain {
        return MessageAnalyzer.toMessageChain(raw, contact = contact)
    }
*/

    constructor(table: LuaTable, bot: Bot?) {
        for (i in 0..table.length()) {
            var tmp = table.get(i) as MiraiMsg
            if (tmp.bot != null && this.bot == null) this.bot = tmp.bot
            this.append(tmp.chain)
        }
    }

    constructor(table: LuaTable) : this(table, null)
    constructor(message: Message, bot: Bot?) {
        this.bot = bot
        this.chain = message.asMessageChain()
    }

    constructor(message: Message) : this(message, null)
    constructor(message: String, bot: Bot?) {
        this.bot = bot
        this.chain = PlainText(message).asMessageChain()
    }

    constructor(message: String) : this(message, null)
    constructor(bot: Bot?) {
        this.bot = bot
    }

    constructor() : this("")

    override fun getOpFunction(opcode: Int): OpFunction {
        return object : OpFunction(opcode) {
            override fun op(varargs: Varargs): Varargs {
                val luaMsg = varargs.arg1() as MiraiMsg
                when (opcode) {
                    APPEND_TEXT -> luaMsg.append(varargs.optjstring(2, ""))
                    APPEND_FACE -> luaMsg.append(Face(varargs.optint(2, 0)))
                    APPEND_IMAGE -> getImage(varargs.arg(2), varargs.arg(3))?.let { luaMsg.append(it) }
                    APPEND_AT -> luaMsg.append(At((varargs.arg(2) as MiraiGroupMember).member))
                    APPEND_AT_ALL -> luaMsg.append(AtAll)
                    APPEND_JSON -> luaMsg.append(LightApp(varargs.optjstring(2, "")))
                    APPEND_SERVICE -> luaMsg.append(ServiceMessage(varargs.optint(2, 1), varargs.optjstring(3, "")))
                    APPEND_XML -> luaMsg.append(ServiceMessage(60, varargs.optjstring(3, "")))
                    //APPEND_POKE -> append( PokeMessage(varargs.optjstring(2,""),varargs.optint(3,0),varargs.optint(4,-1) ) )
                    APPEND_POKE -> luaMsg.append(PokeMessage.Poke)
                    APPEND_IMAGE_FLASH -> getImage(varargs.arg(2), varargs.arg(3))?.let { luaMsg.append(it) }
                    SET_QUOTE -> {
                        val quoteArg = varargs.arg(2)
                        if (quoteArg is MiraiSource) luaMsg.append(QuoteReply(quoteArg.source))
                        else if (quoteArg is MiraiMsg) quoteArg.chain[MessageSource]?.let { luaMsg.append(QuoteReply(it)) }
                    }
                    GET_QUOTE -> return luaMsg.chain[QuoteReply]?.source?.let { MiraiSource(it, bot!!) } ?: LuaValue.NIL
                    RECALL -> runBlocking { luaMsg.chain[MessageSource]?.let { bot!!.recall(it) } }
                    GET_SOURCE -> return luaMsg.chain[MessageSource]?.let { MiraiSource(it, bot = bot!!) }
                        ?: LuaValue.NIL
                    TO_TABLE -> {
                        var table = LuaTable()
                        var i = 0
                        luaMsg.chain.forEachContent {
                            table.insert(i, MiraiMsg(it, bot))
                            i++
                        }
                        return table
                    }
                }
                return luaMsg
            }
        }
    }

    fun getImage(arg: LuaValue, contact: LuaValue): Image? {
        if (arg is LuaString) {
            val argString = arg.toString()
            if (argString.contains("http")) {
                if (contact == LuaValue.NIL) throw LuaError("需要传入一个Contact对象，或使用sender:upLoadImage上传图片")
                var image: Image? = null
                runBlocking {
                    if (contact is MiraiGroup) image = contact.group.uploadImage(URL(argString))
                    if (contact is MiraiFriend) image = contact.qq.uploadImage(URL(argString))
                }
                return image
            } else {
                return Image(argString)
            }
        }
        return null
    }

    fun append(msg: Message) {
        this.chain = this.chain + msg
    }

    fun append(msg: String) {
        this.chain = this.chain + msg
    }

    fun append(msg: LuaValue?): MiraiMsg {
        msg?.let {
            if (msg is MiraiMsg) this.chain = this.chain + msg.chain
            else this.chain = this.chain + msg.toString()
        }
        return this
    }

    override fun add(value: LuaValue?): LuaValue = append(value)
    override fun concat(value: LuaValue?): LuaValue = append(value)
    override fun concatTo(lhs: LuaString): LuaValue? =
        MiraiMsg(PlainText(lhs.toString())).also { it.append(this.chain) }

    override fun toString() = chain.toString()
}