package com.ooooonly.luaMirai.lua.lib

import com.ooooonly.luakt.*
import org.jsoup.Jsoup
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class JsoupLib : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        val globals = env?.checkglobals()
        globals?.edit {
            "Jsoup" to buildLuaTable {
                "parse" to luaFunctionOf { content: String ->
                    val result = Jsoup.parse(content)
//                    println("K:" + result.asLuaValue())
                    return@luaFunctionOf result
                }
            }
        }
        return LuaValue.NIL
    }
}