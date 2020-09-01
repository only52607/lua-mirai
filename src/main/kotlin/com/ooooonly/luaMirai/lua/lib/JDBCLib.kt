package com.ooooonly.luaMirai.lua.lib

import com.ooooonly.luakt.edit
import com.ooooonly.luakt.luaFunctionOf
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction
import java.sql.DriverManager

class JDBCLib : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        val globals = env?.checkglobals()
        Class.forName("org.sqlite.JDBC")
        globals?.edit {
            "SqliteConnection" to luaFunctionOf { url: String ->
                return@luaFunctionOf DriverManager.getConnection("jdbc:sqlite:$url")
            }
        }
        return LuaValue.NIL
    }
}