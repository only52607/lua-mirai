package com.github.only52607.luamirai.lua.lib

import com.github.only52607.luakt.CoerceKotlinToLua
import com.github.only52607.luakt.extension.OneArgFunction
import com.github.only52607.luakt.extension.intValue
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction
import java.net.ServerSocket

class SocketLib : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        env?.checkglobals()?.apply {
            this["ServerSocket"] = OneArgFunction { port: LuaValue ->
                CoerceKotlinToLua.coerce(ServerSocket(port.intValue))
            }
        }
        return LuaValue.NIL
    }
}