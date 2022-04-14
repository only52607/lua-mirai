package com.github.only52607.luamirai.lua.lib

import com.github.only52607.luakt.dsl.*
import org.luaj.vm2.*
import org.luaj.vm2.lib.TwoArgFunction

abstract class JsonLib : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        val globals = env?.checkglobals()?: return NIL
        globals.apply {
            this["Json"] = luaTableOfStringKeys(
                "parseJson" to oneArgLuaFunctionOf {
                    it.stringValue.asJsonLuaValue()
                },
                "toJson" to oneArgLuaFunctionOf {
                    it.tableValue.asJsonString().luaValue
                }
            )
        }
        LuaString.s_metatable[INDEX].set("parseJson", globals["Json"]["parseJson"])
        return LuaValue.NIL
    }

    abstract fun LuaTable.asJsonString(): String
    abstract fun String.asJsonLuaValue(): LuaValue
}