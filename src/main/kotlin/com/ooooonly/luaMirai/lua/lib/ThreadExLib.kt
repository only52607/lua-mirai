package com.ooooonly.luaMirai.lua.lib

import com.ooooonly.luakt.edit
import com.ooooonly.luakt.luaFunctionOf
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaClosure
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

open class ThreadExLib : TwoArgFunction() {
    override fun call(modName: LuaValue?, env: LuaValue?): LuaValue {
        val globals: Globals? = env?.checkglobals()
        globals?.edit {
            "sleep" to luaFunctionOf { time: Long ->
                runBlocking { delay(time) }
            }
            "launch" to luaFunctionOf { cls: LuaClosure ->
                return@luaFunctionOf GlobalScope.launch {
                    cls.invoke()
                }
            }
        }
        return LuaValue.NIL
    }
}