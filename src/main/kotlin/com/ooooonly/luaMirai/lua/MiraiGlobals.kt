package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.lua.lib.*
import com.ooooonly.luaMirai.utils.*
import net.mamoe.mirai.Bot
import net.mamoe.mirai.message.data.*
import org.luaj.vm2.*
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.*
import org.luaj.vm2.lib.jse.*

class MiraiGlobals() : Globals() {
    var mBot: MiraiBot? = null

    companion object {
        var idCounter = 0
    }

    interface Printable {
        fun print(msg: String?)
    }

    private var eventTable: LuaTable? = null
        get() = get("Event").takeIf {
            it is LuaTable
        } as LuaTable?

    private var onLoadFun: LuaFunction? = null
        get() = eventTable?.let {
            it.get("onLoad").takeIf {
                it is LuaFunction
            } as LuaFunction?
        }

    private var onFinishFun: LuaFunction? = null
        get() = eventTable?.let {
            it.get("onFinish").takeIf {
                it is LuaFunction
            } as LuaFunction?
        }

    constructor(printable: Printable) : this() {
        setFunctionNoReturn("print") {
            StringBuffer().let { sb ->
                val narg = it.narg()
                for (i in 1..narg) {
                    sb.append(it.arg(i).toString())
                    if (i != narg) sb.append(" ")
                }
                printable.print(sb.toString())
            }
        }
    }

    constructor(printable: (String) -> Unit) : this() {
        setFunctionNoReturn("print") {
            StringBuffer().let { sb ->
                val narg = it.narg()
                for (i in 1..narg) {
                    sb.append(it.arg(i).toString())
                    if (i != narg) sb.append(" ")
                }
                printable(sb.toString())
            }
        }
    }

    init {
        load(JseBaseLib())
        load(PackageLib())
        load(Bit32Lib())
        load(TableLib())
        load(StringLib())
        load(CoroutineLib())
        load(JseMathLib())
        load(JseIoLib())
        load(JseOsLib())
        load(LuajavaLib())
        load(MiraiBotLib())
        load(NetLib())
        load(LuaJavaExLib())
        load(ThreadExLib())
        load(HttpLib())
        LoadState.install(this)
        LuaC.install(this)

        initEventTable()
        initMsgConstructor()
        initBotFactory()
    }

    private fun initEventTable() = set("Event", LuaTable())
    private fun initMsgConstructor() {
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

        setFunction0Arg("AtAll") {
            MiraiMsg(AtAll)
        }

        setFunction1Arg("Face") {
            MiraiMsg(Face(it.checkint()))
        }
    }

    private fun initBotFactory() {
        setFunction2Arg("Bot") { user, password ->
            return@setFunction2Arg MiraiBot(user.checklong(), password.checkjstring(), 0)
        }
    }

    fun onLoad(bot: Bot) = onLoadFun?.let {
        mBot = MiraiBot(bot, idCounter++)
        it.call(mBot)
    }

    fun onLoad() = onLoadFun?.call()
    fun onFinish() = onFinishFun?.call()
    fun unSubsribeAll() = mBot?.unSubsribeAll()
}