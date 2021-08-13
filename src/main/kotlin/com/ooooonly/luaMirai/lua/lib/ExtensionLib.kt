package com.ooooonly.luaMirai.lua.lib

import com.ooooonly.luakt.edit
import com.ooooonly.luakt.luaFunctionOf
import org.luaj.vm2.LuaFunction
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction
import kotlin.concurrent.thread

/**
 * ClassName: ExtensionLib
 * Description:
 * date: 2021/8/13 23:11
 * @author ooooonly
 * @version
 */
class ExtensionLib : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        val globals = env?.checkglobals()
        globals?.edit {
            "thread" to luaFunctionOf { func: LuaFunction ->
                return@luaFunctionOf thread {
                    func.call()
                }
            }
            "sleep" to luaFunctionOf { time: Long ->
                Thread.sleep(time)
            }
        }
        return LuaValue.NIL
    }
}