package com.github.only52607.luamirai.lua.lib

import com.github.only52607.luakt.ValueMapper
import com.github.only52607.luakt.dsl.luaTableOfStringKeys
import com.github.only52607.luakt.dsl.oneArgLuaFunctionOf
import com.github.only52607.luakt.dsl.stringValue
import org.jsoup.Jsoup
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

class JsoupLib(
    valueMapper: ValueMapper
) : TwoArgFunction(), ValueMapper by valueMapper {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        env?.checkglobals()?.apply {
            this["Jsoup"] = luaTableOfStringKeys(
                "parse" to oneArgLuaFunctionOf {
                    mapToLuaValue(Jsoup.parse(it.stringValue))
                }
            )
        }
        return LuaValue.NIL
    }
}