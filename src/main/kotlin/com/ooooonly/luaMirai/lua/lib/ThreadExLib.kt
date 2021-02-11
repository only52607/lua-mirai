package com.ooooonly.luaMirai.lua.lib

import com.ooooonly.luakt.edit
import com.ooooonly.luakt.luaFunctionOf
import kotlinx.coroutines.*
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaClosure
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

open class ThreadExLib(private val coroutineScope: CoroutineScope) : TwoArgFunction() {
    override fun call(modName: LuaValue?, env: LuaValue?): LuaValue {
        val globals: Globals? = env?.checkglobals()
        globals?.edit {
            "sleep" to luaFunctionOf { time: Long ->
                runBlocking { delay(time) }
            }
            "launch" to luaFunctionOf { cls: LuaClosure ->
                return@luaFunctionOf coroutineScope.launch {
                    cls.invoke()
                }
            }
        }
        return LuaValue.NIL
    }
}