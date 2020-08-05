package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.lua.lib.*
import com.ooooonly.luaMirai.utils.*
import kotlinx.serialization.json.Json
import net.mamoe.mirai.Bot
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.BotConfiguration
import org.luaj.vm2.*
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.*
import org.luaj.vm2.lib.jse.*
import java.io.File

open class MiraiGlobals() : Globals() {
    var mBot: MiraiBot? = null

    companion object {
        var idCounter = 0
    }

    interface Printable {
        fun print(msg: String?)
    }

    private val eventTable: LuaTable?
        get() = get("Event").takeIf {
            it is LuaTable
        } as LuaTable?

    private val onLoadFun: LuaFunction?
        get() = eventTable?.let {
            it.get("onLoad").takeIf {
                it is LuaFunction
            } as LuaFunction?
        }

    private val onFinishFun: LuaFunction?
        get() = eventTable?.let {
            it.get("onFinish").takeIf {
                it is LuaFunction
            } as LuaFunction?
        }

    constructor(printable: Printable) : this({ content ->
        printable.print(content)
    })

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
        load(JsonLib())
        LoadState.install(this)
        LuaC.install(this)

        set("Event", LuaTable())
    }

    fun onLoad(bot: Bot) = onLoadFun?.let {
        mBot = MiraiBot(bot, idCounter++)
        it.call(mBot)
    }

    fun onLoad() = onLoadFun?.call()
    fun onFinish() = onFinishFun?.call()
    fun unSubsribeAll() = mBot?.unSubsribeAll()
}