package com.github.only52607.luamirai.lua.lib

import com.ooooonly.luakt.mapper.ValueMapper
import com.ooooonly.luakt.utils.buildLuaTable
import com.ooooonly.luakt.utils.luaFunctionOf
import com.ooooonly.luakt.utils.provideScope
import org.luaj.vm2.*
import org.luaj.vm2.lib.TwoArgFunction

abstract class JsonLib(
    private val valueMapper: ValueMapper
) : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        val globals = env?.checkglobals()
        valueMapper.provideScope {
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
        }
        return LuaValue.NIL
    }

    abstract fun LuaTable.asJsonString(): String
    abstract fun String.asJsonLuaValue(): LuaValue
}