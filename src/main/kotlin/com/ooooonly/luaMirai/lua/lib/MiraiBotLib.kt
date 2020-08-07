package com.ooooonly.luaMirai.lua.lib

import com.ooooonly.luaMirai.lua.MiraiBot
import com.ooooonly.luaMirai.lua.MiraiGroupMember
import com.ooooonly.luaMirai.lua.MiraiMsg
import com.ooooonly.luaMirai.lua.MiraiSource
import com.ooooonly.luaMirai.utils.*
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.BotConfiguration
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.VarArgFunction
import java.io.File


open class MiraiBotLib : BotLib() {
    override fun call(modName: LuaValue?, env: LuaValue): LuaValue? {
        val globals = env.checkglobals()
        globals.initBotFactory()
        globals.initMsgConstructor()
        return LuaValue.NIL
    }

    private fun LuaTable.initMsgConstructor() {
        set("Msg", object : VarArgFunction() {
            override fun onInvoke(args: Varargs): Varargs {
                val arg1 = args.arg1()
                if (arg1 is LuaTable) return MiraiMsg(arg1)
                else if (arg1 is LuaString) return MiraiMsg(arg1.toString())
                return MiraiMsg()
            }
        })
        setFunction1Arg("At") {
            it.takeIfType<MiraiGroupMember>()?.let {
                MiraiMsg(At(it.member))
            } ?: MiraiMsg()
        }

        setFunction1Arg("Quote") {
            when (it) {
                is MiraiMsg -> it.chain[MessageSource]?.let { MiraiMsg(QuoteReply(it)) } ?: MiraiMsg()
                is MiraiSource -> it.source?.let { MiraiMsg(QuoteReply(it)) }
                else -> MiraiMsg()
            }
        }

        setFunction2Arg("Image") { arg1, arg2 ->
            MiraiMsg.getImage(
                arg1,
                arg2.takeIf { it != LuaValue.NIL }
            )?.let { MiraiMsg(it) } ?: MiraiMsg()
        }

        setFunction2Arg("UploadImage") { arg1, arg2 ->
            MiraiMsg.uploadImage(
                arg1,
                arg2.takeIf { it != LuaValue.NIL }
            )?.let { MiraiMsg(it) } ?: MiraiMsg()
        }

        setFunction2Arg("ImageUrl") { arg1, arg2 ->
            MiraiMsg.uploadImage(
                arg1,
                arg2.takeIf { it != LuaValue.NIL }
            )?.let { MiraiMsg(it) } ?: MiraiMsg()
        }

        setFunction2Arg("ImageFile") { arg1, arg2 ->
            MiraiMsg.uploadImage(
                File(arg1.checkjstring()).toURI().toURL().toString().toLuaValue(),
                arg2.takeIf { it != LuaValue.NIL }
            )?.let { MiraiMsg(it) } ?: MiraiMsg()
        }

        setFunction1Arg("FlashImage") { arg1 ->
            MiraiMsg(FlashImage((arg1 as MiraiMsg).chain[Image]!!))
        }

        setFunction0Arg("AtAll") {
            MiraiMsg(AtAll)
        }

        setFunction1Arg("Face") {
            MiraiMsg(Face(it.checkint()))
        }

        setFunction1Arg("Poke") {
            MiraiMsg(getPoke(it.checkint()))
        }

        setFunction1Arg("Forward") {
            MiraiMsg(it.checktable().buildForwardMsg())
        }

        setFunction1Arg("App") {
            MiraiMsg(LightApp(it.checkjstring()))
        }

        setFunction2Arg("Service") { id, content ->
            MiraiMsg(ServiceMessage(id.checkint(), content.checkjstring()))
        }
    }

    private fun LuaTable.initBotFactory() {
        setFunction2Arg("Bot") { user, password ->
            return@setFunction2Arg MiraiBot(user.checklong(), password.checkjstring(), 0)
        }
        setFunction("Bot") {
            val user = it.checklong(1)
            val pwd = it.checkjstring(2)
            val config =
                if (it.narg() >= 3) BotConfiguration.Default.apply { fileBasedDeviceInfo(it.checkjstring(3)) } else BotConfiguration.Default
            return@setFunction MiraiBot(user, pwd, 0, config)
        }
    }

    private fun getPoke(code: Int) = when (code) {
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

