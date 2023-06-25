package com.github.only52607.luamirai.lua.lib

import com.github.only52607.luakt.CoerceKotlinToLua
import com.github.only52607.luakt.extension.OneArgFunction
import com.github.only52607.luakt.extension.luaTableOfStringKeys
import com.github.only52607.luakt.extension.stringValue
import org.jsoup.Jsoup
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class JsoupLib : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        env?.checkglobals()?.apply {
            this["Jsoup"] = luaTableOfStringKeys(
                "parse" to OneArgFunction {
                    CoerceKotlinToLua.coerce(Jsoup.parse(it.stringValue))
                }
            )
        }
        return LuaValue.NIL
    }
}