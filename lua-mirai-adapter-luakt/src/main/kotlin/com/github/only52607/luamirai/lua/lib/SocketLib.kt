package com.github.only52607.luamirai.lua.lib

import com.github.only52607.luakt.ValueMapper
import com.github.only52607.luakt.dsl.intValue
import com.github.only52607.luakt.dsl.oneArgLuaFunctionOf
import org.luaj.vm2.*
import org.luaj.vm2.lib.TwoArgFunction
import java.net.ServerSocket

class SocketLib(
    valueMapper: ValueMapper
) : TwoArgFunction(), ValueMapper by valueMapper {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        env?.checkglobals()?.apply {
            this["ServerSocket"] = oneArgLuaFunctionOf { port: LuaValue ->
                mapToLuaValue(ServerSocket(port.intValue))
            }
        }
        return LuaValue.NIL
    }
}