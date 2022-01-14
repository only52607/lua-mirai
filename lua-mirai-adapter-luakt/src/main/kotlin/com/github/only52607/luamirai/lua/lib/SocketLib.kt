package com.github.only52607.luamirai.lua.lib

import com.ooooonly.luakt.mapper.ValueMapper
import com.ooooonly.luakt.utils.buildLuaTable
import com.ooooonly.luakt.utils.luaFunctionOf
import com.ooooonly.luakt.utils.provideScope
import org.luaj.vm2.*
import org.luaj.vm2.lib.TwoArgFunction
import java.net.ServerSocket
import java.net.Socket

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