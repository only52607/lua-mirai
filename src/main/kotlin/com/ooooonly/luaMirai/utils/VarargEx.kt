package com.ooooonly.luaMirai.utils

import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs

inline fun <reified T : LuaValue> Varargs.checkArg(n: Int): T {
    var arg = this.arg(n)
    if (arg is T) return arg
    throw LuaError("Arg ${n} expected ${T::class.simpleName},but got ${this.javaClass.simpleName}!")
}