package com.ooooonly.luaMirai.lua.lib

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction

class ThreadExLib : TwoArgFunction() {
    override fun call(modName: LuaValue?, env: LuaValue?): LuaValue {
        var globals: Globals? = env?.checkglobals()
        globals?.let {
            set("sleep", object : OneArgFunction() {
                override fun call(arg: LuaValue?): LuaValue = arg?.let {
                    runBlocking {
                        delay(arg.optlong(0))
                    }
                    LuaValue.NIL
                } ?: LuaValue.NIL
            })
        }
        return LuaValue.NIL
    }
}