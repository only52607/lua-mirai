package com.ooooonly.luaMirai.lua.bridge.coreimpl

import com.ooooonly.luaMirai.lua.MiraiMsg
import com.ooooonly.luaMirai.lua.bridge.base.BaseMsg
import com.ooooonly.luakt.asKValue
import com.ooooonly.luakt.luaFunctionOf
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

class MsgCoreImpl(val host: Message) : BaseMsg() {
    companion object {
        fun setMsgConstructor(table: LuaTable) {
            table.initMsgConstructor()
        }

        private fun LuaTable.initMsgConstructor() {
            set("Msg", luaFunctionOf { content: LuaValue ->
                return@luaFunctionOf when (content) {
                    is MsgCoreImpl -> content
                    else -> MsgCoreImpl(PlainText(content.toString()))
                }
            })

            set("At", luaFunctionOf { target: MemberCoreImpl ->
                return@luaFunctionOf when (target) {
                    is MemberCoreImpl -> MsgCoreImpl(At(target.host))
                    else -> throw LuaError("At target must be Member!")
                }
            })

            set("Quote", luaFunctionOf { msg: MsgCoreImpl ->
                return@luaFunctionOf when (msg) {
                    is MsgCoreImpl -> MsgCoreImpl(QuoteReply(msg.host.asMessageChain().source))
                    else -> throw LuaError("Empty quote!")
                }
            })

            set("Image", luaFunctionOf { id: String ->
                return@luaFunctionOf MsgCoreImpl(Image(id))
            })

            set("ImageUrl", luaFunctionOf<String, LuaUserdata> { url: String, target: Any ->
                return@luaFunctionOf when (target) {
                    is FriendCoreImpl -> runBlocking { target.host.uploadImage(URL(url)) }
                    is GroupCoreImpl -> runBlocking { target.host.uploadImage(URL(url)) }
                    else -> EmptyMessageChain
                }.let { MsgCoreImpl(it) }
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
                }.let { MsgCoreImpl(it) }
            })

            set("FlashImage", luaFunctionOf { arg: MsgCoreImpl ->
                return@luaFunctionOf MsgCoreImpl(FlashImage(arg.host as Image))
            })

            set("AtAll", luaFunctionOf {
                return@luaFunctionOf MsgCoreImpl(AtAll)
            })

            set("Face", luaFunctionOf { arg: Int ->
                return@luaFunctionOf MsgCoreImpl(Face(arg))
            })

            set("Poke", luaFunctionOf { arg: LuaValue ->
                return@luaFunctionOf MsgCoreImpl(arg.asKValue(1).toPokeCode())
            })

            set("Forward", luaFunctionOf { table: LuaValue ->
                return@luaFunctionOf MsgCoreImpl((table as LuaTable).buildForwardMsg())
            })

            set("App", luaFunctionOf { code: String ->
                return@luaFunctionOf MsgCoreImpl(LightApp(code))
            })

            set("Service", luaFunctionOf { id: Int, content: String ->
                return@luaFunctionOf MsgCoreImpl(ServiceMessage(id, content))
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
            (get("message")?.takeIf { it != LuaValue.NIL } as? MiraiMsg)?.chain ?: EmptyMessageChain
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
    }

    override var type: String
        get() = host::class.simpleName ?: ""
        set(value) {}
    override var params: LuaTable
        get() = LuaTable()
        set(value) {}

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

    override fun toTable(): Array<MsgCoreImpl> {
        if (host !is MessageChain) throw LuaError("Could not case a single message to table!")
        return host.map { MsgCoreImpl(it) }.toTypedArray()
    }

    override fun append(msg: LuaValue?): MsgCoreImpl = MsgCoreImpl(host + PlainText(msg.toString()))

    override fun append(msg: BaseMsg?): MsgCoreImpl = MsgCoreImpl(host + (msg as MsgCoreImpl).host)

    override fun appendTo(msg: LuaValue?): MsgCoreImpl = MsgCoreImpl(PlainText(msg.toString()) + host)

    override fun appendTo(msg: BaseMsg?): MsgCoreImpl = MsgCoreImpl((msg as MsgCoreImpl).host + host)

    override fun toString(): String = StringBuffer().let {
        host.asMessageChain().forEachContent { content ->
            it.append(content.toString())
        }
        it.toString()
    }

    override fun length(): Int = host.asMessageChain().size
}