package com.ooooonly.luaMirai.lua.bridge.coreimpl

import com.ooooonly.luaMirai.lua.bridge.base.BaseMessage
import com.ooooonly.luakt.asKValue
import com.ooooonly.luakt.luaFunctionOf
import com.ooooonly.luakt.luakotlin.KotlinClassInLua
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.uploadImage
import okhttp3.OkHttpClient
import okhttp3.Request
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaUserdata
import org.luaj.vm2.LuaValue
import java.io.File
import java.io.FileOutputStream
import java.net.URL

class MessageCoreImpl(val host: Message) : BaseMessage() {
    companion object {
        fun setMsgConstructor(table: LuaTable) {
            table.initMsgConstructor()
        }

        private fun LuaTable.initMsgConstructor() {
            set("Text", luaFunctionOf { content: LuaValue ->
                return@luaFunctionOf when (content) {
                    is MessageCoreImpl -> content
                    else -> MessageCoreImpl(PlainText(content.toString()))
                }
            })

            set("At", luaFunctionOf { target: MemberCoreImpl ->
                return@luaFunctionOf when (target) {
                    is MemberCoreImpl -> MessageCoreImpl(At(target.host))
                    else -> throw LuaError("At target must be Member!")
                }
            })

            set("Quote", luaFunctionOf { msg: MessageCoreImpl ->
                return@luaFunctionOf when (msg) {
                    is MessageCoreImpl -> MessageCoreImpl(QuoteReply(msg.host.asMessageChain().source))
                    else -> throw LuaError("Empty quote!")
                }
            })

            set("Image", luaFunctionOf { id: String ->
                return@luaFunctionOf MessageCoreImpl(Image(id))
            })

            set("ImageUrl", luaFunctionOf<String, LuaUserdata> { url: String, target: Any ->
                return@luaFunctionOf when (target) {
                    is FriendCoreImpl -> runBlocking { target.host.uploadImage(URL(url)) }
                    is GroupCoreImpl -> runBlocking { target.host.uploadImage(URL(url)) }
                    else -> EmptyMessageChain
                }.let { MessageCoreImpl(it) }
            })

            set("ImageFile", luaFunctionOf { filePath: String, target: Any ->
                return@luaFunctionOf when (target) {
                    is FriendCoreImpl -> runBlocking {
                        (target as FriendCoreImpl).host.uploadImage(
                            URL(
                                File(filePath).toURI().toURL().toString()
                            )
                        )
                    }
                    is GroupCoreImpl -> runBlocking {
                        (target as GroupCoreImpl).host.uploadImage(
                            URL(
                                File(filePath).toURI().toURL().toString()
                            )
                        )
                    }
                    else -> EmptyMessageChain
                }.let { MessageCoreImpl(it) }
            })

            set("FlashImage", luaFunctionOf { arg: MessageCoreImpl ->
                return@luaFunctionOf MessageCoreImpl(FlashImage(arg.host as Image))
            })

            set("AtAll", luaFunctionOf {
                return@luaFunctionOf MessageCoreImpl(AtAll)
            })

            set("Face", luaFunctionOf { arg: Int ->
                return@luaFunctionOf MessageCoreImpl(Face(arg))
            })

            set("Poke", luaFunctionOf { arg: LuaValue ->
                return@luaFunctionOf MessageCoreImpl(arg.asKValue(1).toPokeCode())
            })

            set("Forward", luaFunctionOf { table: LuaValue ->
                return@luaFunctionOf MessageCoreImpl((table as LuaTable).buildForwardMsg())
            })

            set("App", luaFunctionOf { code: String ->
                return@luaFunctionOf MessageCoreImpl(LightApp(code))
            })

            set("Service", luaFunctionOf { id: Int, content: String ->
                return@luaFunctionOf MessageCoreImpl(ServiceMessage(id, content))
            })
        }

        private fun Int.toPokeCode() = when (this) {
            0 -> PokeMessage.Poke
            1 -> PokeMessage.ShowLove
            2 -> PokeMessage.Like
            3 -> PokeMessage.Heartbroken
            4 -> PokeMessage.SixSixSix
            5 -> PokeMessage.FangDaZhao
            6 -> PokeMessage.BaoBeiQiu
            7 -> PokeMessage.ZhaoHuanShu
            8 -> PokeMessage.RangNiPi
            9 -> PokeMessage.JieYin
            10 -> PokeMessage.ShouLei
            11 -> PokeMessage.GouYin
            12 -> PokeMessage.ZhuaYiXia
            13 -> PokeMessage.SuiPing
            14 -> PokeMessage.QiaoMen
            15 -> PokeMessage.Rose
            else -> EmptyMessageChain
        }

        private fun LuaTable.buildForwardMsg() = run {
            val nodes = buildNodes()
            val displayStrategy = get("content")?.takeIf { it is LuaTable }?.checktable()?.buildDisplayStrategy(nodes)
                ?: ForwardMessage.DisplayStrategy.Default
            ForwardMessage(nodes, displayStrategy)
        }

        private fun LuaTable.buildDisplayStrategy(nodes: Collection<ForwardMessage.INode>) =
            object : ForwardMessage.DisplayStrategy() {
                override fun generateTitle(forward: ForwardMessage): String =
                    get("title")?.takeIf { it != LuaValue.NIL }?.tojstring() ?: "群聊的聊天记录"

                override fun generateBrief(forward: ForwardMessage): String =
                    get("brief")?.takeIf { it != LuaValue.NIL }?.tojstring() ?: "[聊天记录]"

                override fun generateSource(forward: ForwardMessage): String =
                    get("source")?.takeIf { it != LuaValue.NIL }?.tojstring() ?: "聊天记录"

                override fun generatePreview(forward: ForwardMessage): Sequence<String> =
                    get("preview")?.takeIf { it is LuaTable }?.checktable()?.buildStringSequence() ?: nodes.asSequence()
                        .map { it.senderName + ": " + it.message.contentToString() }

                override fun generateSummary(forward: ForwardMessage): String =
                    get("summary")?.takeIf { it != LuaValue.NIL }?.tojstring() ?: "查看 ${nodes.size} 条转发消息"
            }

        private fun LuaTable.buildNode() = ForwardMessage.Node(
            get("senderId")?.takeIf { it != LuaValue.NIL }?.tolong() ?: 0,
            get("time")?.takeIf { it != LuaValue.NIL }?.toint() ?: 0,
            get("senderName")?.takeIf { it != LuaValue.NIL }?.tojstring() ?: "",
            (get("message")?.takeIf { it != LuaValue.NIL } as? MessageCoreImpl)?.host ?: EmptyMessageChain
        )

        private fun LuaTable.buildNodes() = mutableListOf<ForwardMessage.Node>().apply {
            for (i in 1..this@buildNodes.length()) {
                this.add(this@buildNodes.checktable(i).buildNode())
            }
        }

        private fun LuaTable.buildStringSequence() = mutableListOf<String>().apply {
            for (i in 1..this@buildStringSequence.length()) {
                this.add(this@buildStringSequence.checkjstring(i))
            }
        }.asSequence()

        private val _regex = Regex("""\[mirai:(.*?):(.*?)\]""")
    }

    private var _type: String? = null
    private var _params: Array<String>? = null
    private fun parseTypeParams() {
        val result = _regex.find(toString())
        _type = result?.groupValues?.get(1) ?: ""
        _params = result?.groupValues?.get(2)?.split(",")?.toTypedArray() ?: arrayOf()
    }

    override var type: String
        get() {
            if (host is MessageChain) return "chain"
            if (host is PlainText) return "plain"
            return _type ?: parseTypeParams().run { _type!! }
        }
        set(value) {}
    override var params: Array<String>
        get() {
            if (host is MessageChain) return arrayOf()
            if (host is PlainText) return arrayOf()
            return _params ?: parseTypeParams().run { _params!! }
        }
        set(value) {}

    init {
        kClassInLua = KotlinClassInLua.forKClass(this::class)
    }

    override fun recall() {
        if (host !is MessageChain) throw LuaError("You could not recall single message!")
        host.bot.launch {
            host.recall()
        }
    }

    override fun downloadImage(path: String) {
        runBlocking {
            val imageUrl = (host as Image).queryUrl()
            val client = OkHttpClient()
            val request = Request.Builder().url(imageUrl).build()
            val data = client.newCall(request).execute().body?.byteStream()?.readAllBytes()
            FileOutputStream(path).apply { write(data) }.close()
        }
    }

    override fun getImageUrl(): String = runBlocking {
        return@runBlocking (host as Image).queryUrl()
    }

    override fun toTable(): Array<MessageCoreImpl> {
        if (host !is MessageChain) throw LuaError("Could not case a single message to table!")
        return host.map { MessageCoreImpl(it) }.filter { it.host is MessageContent }.toTypedArray()
    }

    override fun append(msg: LuaValue?): MessageCoreImpl = MessageCoreImpl(host + PlainText(msg.toString()))

    override fun append(message: BaseMessage?): MessageCoreImpl =
        MessageCoreImpl(host + (message as MessageCoreImpl).host)

    override fun appendTo(msg: LuaValue?): MessageCoreImpl = MessageCoreImpl(PlainText(msg.toString()) + host)

    override fun appendTo(message: BaseMessage?): MessageCoreImpl =
        MessageCoreImpl((message as MessageCoreImpl).host + host)

    override fun toString(): String = StringBuffer().let {
        host.asMessageChain().forEachContent { content ->
            it.append(content.toString())
        }
        it.toString()
    }

    override fun length(): Int = host.asMessageChain().size
}