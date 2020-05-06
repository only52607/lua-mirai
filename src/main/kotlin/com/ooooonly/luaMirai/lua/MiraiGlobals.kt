package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.lua.lib.LuaJavaExLib
import com.ooooonly.luaMirai.lua.lib.MiraiBotLib
import com.ooooonly.luaMirai.lua.lib.NetLib
import net.mamoe.mirai.Bot
import org.luaj.vm2.*
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.*
import org.luaj.vm2.lib.jse.*

class MiraiGlobals : Globals {
    interface Printable {
        fun print(msg: String?)
    }

    private var eventTable: LuaTable? = null
        get() = this.get("Event") as LuaTable

    private var onLoadFun: LuaFunction? = null
        get() = eventTable?.let {
            it.get("onLoad")
        } as LuaFunction?

    private var onFinishFun: LuaFunction? = null
        get() = eventTable?.let {
            it.get("onFinish")
        } as LuaFunction?

    var standardOut = object : Printable {
        override fun print(msg: String?) = println(msg)
    }

    constructor(printable: Printable) {
        this()
        standardOut = printable
    }

    constructor() {
        this.load(JseBaseLib())
        this.load(PackageLib())
        this.load(Bit32Lib())
        this.load(TableLib())
        this.load(StringLib())
        this.load(CoroutineLib())
        this.load(JseMathLib())
        this.load(JseIoLib())
        this.load(JseOsLib())
        this.load(LuajavaLib())
        this.load(MiraiBotLib())
        this.load(NetLib())
        this.load(LuaJavaExLib())
        LoadState.install(this)
        LuaC.install(this)

        initStandardOut()
        initEventFun()
    }

    private fun initEventFun() {
        var eventTable = LuaTable()
        /*
        eventTable.set("onLoad", object : OneArgFunction() {
            override fun call(arg: LuaValue): LuaValue? {
                if (!(arg is LuaFunction)) throw LuaError("第一个参数必须为函数！")
                onLoadFun = arg
                return LuaValue.NIL
            }
        })
        eventTable.set("onFinish", object : OneArgFunction() {
            override fun call(arg: LuaValue?): LuaValue {
                if (!(arg is LuaFunction)) throw LuaError("第一个参数必须为函数！")
                onFinishFun = arg
                return LuaValue.NIL
            }
        })
        */

        this.set("Event", eventTable)
    }

    private fun initStandardOut() =
        this.set("print", object : VarArgFunction() {
            override fun onInvoke(args: Varargs?): Varargs {
                if (args == null) return LuaValue.NIL
                var sb = StringBuffer()
                val narg = args.narg()
                for (i in 1..narg) {
                    sb.append(args.arg(i).toString())
                    if (i != narg) sb.append(" ")
                }
                standardOut.print(sb.toString())
                return LuaValue.NIL
            }
        })

    fun onLoad(bot: Bot) = onLoadFun?.let {
        onLoadFun!!.call(MiraiBot(bot))
    }


    fun onLoad() = onLoadFun?.let {
        onLoadFun!!.call()
    }

    fun onFinish() = onFinishFun?.let {
        onFinishFun!!.call()
    }

}