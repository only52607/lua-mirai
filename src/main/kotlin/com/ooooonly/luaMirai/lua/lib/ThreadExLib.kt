package com.ooooonly.luaMirai.lua.lib

import com.ooooonly.luaMirai.utils.setFunction1ArgNoReturn
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

open class ThreadExLib : TwoArgFunction() {
    override fun call(modName: LuaValue?, env: LuaValue?): LuaValue {
        var globals: Globals? = env?.checkglobals()
        globals?.run {
            setFunction1ArgNoReturn("sleep") {
                runBlocking {
                    delay(it.optlong(0))
                }
            }
        }

        return LuaValue.NIL
    }
}