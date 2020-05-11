package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.lua.lib.LuaJavaExLib
import com.ooooonly.luaMirai.lua.lib.MiraiBotLib
import com.ooooonly.luaMirai.lua.lib.NetLib
import com.ooooonly.luaMirai.lua.lib.ThreadExLib
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
        get() =
            get("Event").also {
                if (!(it is LuaTable)) return null
            } as LuaTable

    private var onLoadFun: LuaFunction? = null
        get() = eventTable?.let {
            it.get("onLoad").also {
                if (!(it is LuaFunction)) return null
            } as LuaFunction
        }

    private var onFinishFun: LuaFunction? = null
        get() = eventTable?.let {
            it.get("onFinish").also {
                if (!(it is LuaFunction)) return null
            } as LuaFunction
        }

    constructor(printable: Printable) : this() {
        set("print", object : VarArgFunction() {
            override fun onInvoke(args: Varargs?): Varargs = LuaValue.NIL.also {
                args?.let { args ->
                    StringBuffer().let { sb ->
                        val narg = args.narg()
                        for (i in 1..narg) {
                            sb.append(args.arg(i).toString())
                            if (i != narg) sb.append(" ")
                        }
                        printable.print(sb.toString())
                    }
                }
            }
        })
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
        LoadState.install(this)
        LuaC.install(this)

        initEventTable()
        initMsgConstructor()
    }

    private fun initEventTable() = LuaTable().let { set("Event", it) }
    private fun initMsgConstructor() {
        set("At", object : OneArgFunction() {
            override fun call(arg: LuaValue?): LuaValue = arg?.let {
                when (it) {
                    is MiraiGroupMember -> MiraiMsg(At(it.member))
                    else -> MiraiMsg()
                }
            } ?: MiraiMsg()
        })
        set("Quote", object : OneArgFunction() {
            override fun call(arg: LuaValue?): LuaValue = arg?.let {
                when (it) {
                    is MiraiMsg -> it.chain[MessageSource]?.let { MiraiMsg(QuoteReply(it)) } ?: MiraiMsg()
                    else -> MiraiMsg()
                }
            } ?: MiraiMsg()
        })
        set("Image", object : TwoArgFunction() {
            override fun call(arg1: LuaValue?, arg2: LuaValue?): LuaValue = arg1?.let {
                MiraiMsg.getImage(arg1, arg2)?.let { MiraiMsg(it) }
            } ?: MiraiMsg()
        })
        set("AtAll", object : ZeroArgFunction() {
            override fun call(): LuaValue? = MiraiMsg(AtAll)
        })
        set("Face", object : OneArgFunction() {
            override fun call(arg: LuaValue?): LuaValue = arg?.let {
                MiraiMsg(Face(it.checkint()))
            } ?: MiraiMsg()
        })
    }

    fun onLoad(bot: Bot) = onLoadFun?.let {
        mBot = MiraiBot(bot, idCounter++)
        it.call(mBot)
    }

    fun onLoad() = onLoadFun?.call()
    fun onFinish() = onFinishFun?.call()
    fun unSubsribeAll() = mBot?.unSubsribeAll()
}