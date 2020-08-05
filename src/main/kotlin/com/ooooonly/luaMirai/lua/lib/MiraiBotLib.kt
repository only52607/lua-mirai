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

        setFunction2Arg("ImageFile") { arg1, arg2 ->
            MiraiMsg.uploadImage(
                File(arg1.checkjstring()).toURI().toURL().toString().toLuaValue(),
                arg2.takeIf { it != LuaValue.NIL }
            )?.let { MiraiMsg(it) } ?: MiraiMsg()
        }

        setFunction0Arg("AtAll") {
            MiraiMsg(AtAll)
        }

        setFunction1Arg("Face") {
            MiraiMsg(Face(it.checkint()))
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
}

