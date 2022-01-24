package com.github.only52607.luamirai.lua.lib

import com.github.only52607.luakt.ValueMapper
import com.github.only52607.luakt.utils.provideScope
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction
import java.sql.DriverManager

class JDBCLib(
    private val valueMapper: ValueMapper
) : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        val globals = env?.checkglobals()
        Class.forName("org.sqlite.JDBC")
        valueMapper.provideScope {
            globals?.edit {
                "SqliteConnection" to luaFunctionOf { url: String ->
                    return@luaFunctionOf DriverManager.getConnection("jdbc:sqlite:$url")
                }
                "Connection" to luaFunctionOf { url: String ->
                    return@luaFunctionOf DriverManager.getConnection(url)
                }
            }
        }
        return LuaValue.NIL
    }
}