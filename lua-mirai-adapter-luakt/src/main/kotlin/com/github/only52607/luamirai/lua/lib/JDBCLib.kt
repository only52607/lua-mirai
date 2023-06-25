package com.github.only52607.luamirai.lua.lib

import com.github.only52607.luakt.CoerceKotlinToLua
import com.github.only52607.luakt.extension.OneArgFunction
import com.github.only52607.luakt.extension.stringValue
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction
import java.sql.DriverManager

class JDBCLib : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        Class.forName("org.sqlite.JDBC")
        env?.checkglobals()?.apply {
            this["SqliteConnection"] = OneArgFunction { url: LuaValue ->
                CoerceKotlinToLua.coerce(DriverManager.getConnection("jdbc:sqlite:${url.stringValue}"))
            }
            this["Connection"] = OneArgFunction { url: LuaValue ->
                CoerceKotlinToLua.coerce(DriverManager.getConnection(url.stringValue))
            }
        }
        return LuaValue.NIL
    }
}