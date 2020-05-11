package com.ooooonly.luaMirai.utils

import com.ooooonly.luaMirai.lua.LuaObject
import kotlinx.coroutines.runBlocking
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs

fun generateOpFunction(opcode: Int, block: suspend (Int, Varargs) -> Varargs): LuaObject.OpFunction =
    object : LuaObject.OpFunction(opcode) {
        override fun op(varargs: Varargs?): Varargs {
            var result: Varargs = LuaValue.NIL
            runBlocking {
                varargs?.let { result = block(opcode, it) }
            }
            return result
        }
    }