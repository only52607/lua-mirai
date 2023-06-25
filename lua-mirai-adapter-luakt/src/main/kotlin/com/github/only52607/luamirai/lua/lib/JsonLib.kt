package com.github.only52607.luamirai.lua.lib

import com.github.only52607.luakt.CoerceKotlinToLua
import com.github.only52607.luakt.extension.OneArgFunction
import com.github.only52607.luakt.extension.luaTableOfStringKeys
import com.github.only52607.luakt.extension.stringValue
import com.github.only52607.luakt.extension.tableValue
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

abstract class JsonLib : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        val globals = env?.checkglobals()?: return NIL
        globals.apply {
            this["Json"] = luaTableOfStringKeys(
                "parseJson" to OneArgFunction {
                    it.stringValue.asJsonLuaValue()
                },
                "toJson" to OneArgFunction {
                    CoerceKotlinToLua.coerce(it.tableValue.asJsonString())
                }
            )
        }
        LuaString.s_metatable[INDEX].set("parseJson", globals["Json"]["parseJson"])
        return LuaValue.NIL
    }

    abstract fun LuaTable.asJsonString(): String
    abstract fun String.asJsonLuaValue(): LuaValue
}