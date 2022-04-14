package com.github.only52607.luamirai.lua.lib

import com.github.only52607.luakt.ValueMapper
import com.github.only52607.luakt.dsl.oneArgLuaFunctionOf
import com.github.only52607.luakt.dsl.stringValue
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction
import java.sql.DriverManager

class JDBCLib(
    valueMapper: ValueMapper
) : TwoArgFunction(), ValueMapper by valueMapper {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        Class.forName("org.sqlite.JDBC")
        env?.checkglobals()?.apply {
            this["SqliteConnection"] = oneArgLuaFunctionOf { url: LuaValue ->
                mapToLuaValue(DriverManager.getConnection("jdbc:sqlite:${url.stringValue}"))
            }
            this["Connection"] = oneArgLuaFunctionOf { url: LuaValue ->
                mapToLuaValue(DriverManager.getConnection(url.stringValue))
            }
        }
        return LuaValue.NIL
    }
}