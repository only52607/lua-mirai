package com.ooooonly.luaMirai.lua.lib

import com.ooooonly.luakt.buildLuaTable
import com.ooooonly.luakt.luaFunctionOf
import org.json.JSONArray
import org.json.JSONObject
import org.luaj.vm2.*
import org.luaj.vm2.lib.TwoArgFunction

abstract class JsonLib : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        val globals = env?.checkglobals()
        val parseJson = luaFunctionOf { raw: String ->
            return@luaFunctionOf raw.asJsonLuaValue()
        }
        val toJson = luaFunctionOf { raw: LuaTable ->
            return@luaFunctionOf raw.asJsonString()
        }
        globals?.set("Json", buildLuaTable {
            "parseJson" to parseJson
            "toJson" to toJson
        })
        LuaString.s_metatable[INDEX].set("parseJson", parseJson)
        return LuaValue.NIL
    }

    abstract fun LuaTable.asJsonString(): String
    abstract fun String.asJsonLuaValue(): LuaValue
}