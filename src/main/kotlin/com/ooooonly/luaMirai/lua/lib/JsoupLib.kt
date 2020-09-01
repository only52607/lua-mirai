package com.ooooonly.luaMirai.lua.lib

import com.ooooonly.luakt.asLuaValue
import com.ooooonly.luakt.edit
import com.ooooonly.luakt.luaFunctionOf
import com.ooooonly.luakt.luaTableOf
import org.jsoup.Jsoup
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction
import java.sql.DriverManager

class JsoupLib : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        val globals = env?.checkglobals()
        globals?.edit {
            "Jsoup" to luaTableOf {
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