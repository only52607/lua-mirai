package com.github.only52607.luamirai.lua.lib

import com.github.only52607.luakt.ValueMapper
import com.github.only52607.luakt.utils.luaFunctionOf
import org.luaj.vm2.*
import org.luaj.vm2.lib.TwoArgFunction
import java.net.ServerSocket

class SocketLib(
    private val valueMapper: ValueMapper
) : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        val globals = env?.checkglobals()
        globals?.set("ServerSocket", luaFunctionOf(valueMapper) { port: Int ->
            return@luaFunctionOf ServerSocket(port)
        })
        return LuaValue.NIL
    }

}