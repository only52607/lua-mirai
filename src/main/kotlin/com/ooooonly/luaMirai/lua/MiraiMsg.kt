package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.utils.checkArg
import com.ooooonly.luaMirai.utils.checkMessageSource
import com.ooooonly.luaMirai.utils.generateOpFunction
import com.ooooonly.luaMirai.utils.toLuaValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.uploadImage
import okhttp3.OkHttpClient
import okhttp3.Request
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import java.io.FileOutputStream
import java.net.URL
import kotlin.coroutines.CoroutineContext


class MiraiMsg : LuaMsg {
    var chain: MessageChain = EmptyMessageChain
    var bot: Bot? = null

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

    companion object {
        fun getImage(arg: LuaValue, contact: LuaValue?): Image? = when (arg) {
            is LuaString -> arg.toString().let {
                if (it.contains("http")) {
                    runBlocking {
                        when (contact) {
                            is MiraiFriend -> contact.friend.uploadImage(URL(it))
                            is MiraiGroup -> contact.group.uploadImage(URL(it))
                            else -> throw LuaError("Second parameter must be a contact!")
                        }
                    }
                } else Image(it)
            }
            else -> null
        }

        fun uploadImage(arg: LuaValue, contact: LuaValue?): Image? = when (arg) {
            is LuaString -> arg.toString().let {
                runBlocking {
                    when (contact) {
                        is MiraiFriend -> contact.friend.uploadImage(URL(it))
                        is MiraiGroup -> contact.group.uploadImage(URL(it))
                        else -> throw LuaError("Second parameter must be a contact!")
                    }
                }
            }
            else -> null
        }
    }

    override fun getOpFunction(opcode: Int): OpFunction = generateOpFunction(opcode) { op, varargs ->
        varargs.checkArg<MiraiMsg>(1).let {
            when (opcode) {
                GET_QUOTE -> it.chain[QuoteReply]?.source?.let { MiraiSource(it, it.bot) } ?: LuaValue.NIL
                GET_SOURCE -> it.chain[MessageSource]?.let { MiraiSource(it, it.bot) } ?: LuaValue.NIL
                GET_IMAGE_URL -> it.chain[Image]?.queryUrl()?.toLuaValue() ?: LuaValue.NIL
                TO_TABLE -> LuaTable().also { t ->
                    var i = 0
                    it.chain.forEachContent { t.insert(i++, MiraiMsg(it, bot)) }
                }
                else -> null
            } ?: it.also {
                when (opcode) {
                    APPEND_TEXT -> it.append(varargs.optjstring(2, ""))
                    APPEND_FACE -> it.append(Face(varargs.optint(2, 0)))
                    APPEND_IMAGE -> getImage(varargs.arg(2), varargs.arg(3))?.let { image -> it.append(image) }
                    APPEND_AT -> it.append(At((varargs.arg(2) as MiraiGroupMember).member))
                    APPEND_AT_ALL -> it.append(AtAll)
                    APPEND_JSON -> it.append(LightApp(varargs.optjstring(2, "")))
                    APPEND_SERVICE -> it.append(ServiceMessage(varargs.optint(2, 1), varargs.optjstring(3, "")))
                    APPEND_XML -> it.append(ServiceMessage(60, varargs.optjstring(3, "")))
                    //APPEND_POKE -> append( PokeMessage(varargs.optjstring(2,""),varargs.optint(3,0),varargs.optint(4,-1) ) )
                    APPEND_POKE -> it.append(PokeMessage.Poke)
                    APPEND_IMAGE_FLASH -> getImage(varargs.arg(2), varargs.arg(3))?.let { image -> it.append(image) }
                    SET_QUOTE -> it.append(QuoteReply(varargs.arg(2).checkMessageSource()))
                    RECALL -> it.chain[MessageSource]?.let { it.bot.recall(it) }
                    DOWNLOAD_IMAGE -> {
                        val imageUrl = it.chain[Image]?.queryUrl() ?: return@also
                        val path = varargs.checkjstring(2)
                        GlobalScope.launch {
                            val client = OkHttpClient()
                            val request = Request.Builder().url(imageUrl).build()
                            val data = client.newCall(request).execute().body?.byteStream()?.readAllBytes()
                            FileOutputStream(path).apply { write(data) }.close()
                        }
                    }
                }
            }
        }
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

    //重载操作符
    override fun add(value: LuaValue?): LuaValue = append(value)
    override fun concat(value: LuaValue?): LuaValue = append(value)
    override fun concatTo(lhs: LuaString): LuaValue? =
        MiraiMsg(PlainText(lhs.toString())).also { it.append(this.chain) }

    override fun lteq(rhs: LuaValue): LuaValue? = if (_eq_(rhs)) LuaValue.TRUE else LuaValue.FALSE
    override fun lteq_b(rhs: LuaValue): Boolean = _eq_(rhs)
    override fun eq(rhs: LuaValue): LuaValue? = if (_eq_(rhs)) LuaValue.TRUE else LuaValue.FALSE
    override fun eq_b(rhs: LuaValue): Boolean = _eq_(rhs)
    override fun raweq(rhs: LuaValue): Boolean = _eq_(rhs)
    private fun _eq_(rhs: LuaValue): Boolean = toString().equals(rhs.toString())

    override fun toString() = StringBuffer().let {
        chain.forEachContent { content ->
            it.append(content.toString())
        }
        it.toString()
    }
}