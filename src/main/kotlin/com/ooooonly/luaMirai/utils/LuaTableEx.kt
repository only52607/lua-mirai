package com.ooooonly.luaMirai.utils

import org.luaj.vm2.Globals
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.*


fun LuaTable.setInt(key: String, value: Int) = set(key, LuaValue.valueOf(value))
fun LuaTable.setLong(key: String, value: Long) = set(key, LuaValue.valueOf(value.toString()))
fun LuaTable.setString(key: String, value: String) = set(key, LuaValue.valueOf(value))
fun LuaTable.setBoolean(key: String, value: Boolean) = set(key, LuaValue.valueOf(value))

fun LuaTable.asIndex(): LuaTable {
    var t = LuaTable()
    t.set(LuaValue.INDEX, this)
    return t
}

fun LuaTable.applyIndex(block: LuaTable.() -> Unit): LuaTable {
    set(LuaValue.INDEX, LuaTable().apply { block() })
    return this
}

fun LuaTable.copyFrom(table: LuaTable) {
    var keys: Array<LuaValue> = table.keys()
    keys.forEach {
        set(it, table.get(it))
    }
}

fun LuaTable.setFunction(key: String, luaFun: (Varargs) -> Varargs) {
    set(key, object : VarArgFunction() {
        override fun onInvoke(args: Varargs?): Varargs {
            return args?.let { luaFun(it) } ?: LuaValue.NIL
        }
    })
}

fun LuaTable.setFunctionNoReturn(key: String, luaFun: (Varargs) -> Unit) {
    set(key, object : VarArgFunction() {
        override fun onInvoke(args: Varargs?): Varargs {
            args?.let { luaFun(it) }
            return LuaValue.NIL
        }
    })
}

fun LuaTable.setFunction0Arg(key: String, luaFun: () -> LuaValue) {
    set(key, object : ZeroArgFunction() {
        override fun call(): LuaValue {
            return luaFun() ?: LuaValue.NIL
        }
    })
}

fun LuaTable.setFunction0Arg0Return(key: String, luaFun: () -> Unit) {
    set(key, object : ZeroArgFunction() {
        override fun call(): LuaValue {
            luaFun()
            return LuaValue.NIL
        }
    })
}

fun LuaTable.setFunction1Arg(key: String, luaFun: (LuaValue) -> LuaValue) {
    set(key, object : OneArgFunction() {
        override fun call(arg: LuaValue?): LuaValue {
            return arg?.let { luaFun(it) } ?: LuaValue.NIL
        }
    })
}

fun LuaTable.setFunction1ArgNoReturn(key: String, luaFun: (LuaValue) -> Unit) {
    set(key, object : OneArgFunction() {
        override fun call(arg: LuaValue?): LuaValue {
            arg?.let { luaFun(it) }
            return LuaValue.NIL
        }
    })
}

fun LuaTable.setFunction2Arg(key: String, luaFun: (LuaValue, LuaValue) -> LuaValue) {
    set(key, object : TwoArgFunction() {
        override fun call(arg1: LuaValue?, arg2: LuaValue?): LuaValue {
            if (arg1 == null || arg2 == null) return LuaValue.NIL
            return luaFun(arg1, arg2)
        }
    })
}

fun LuaTable.setFunction2ArgNoReturn(key: String, luaFun: (LuaValue, LuaValue) -> Unit) {
    set(key, object : TwoArgFunction() {
        override fun call(arg1: LuaValue?, arg2: LuaValue?): LuaValue {
            if (arg1 == null || arg2 == null) return LuaValue.NIL
            luaFun(arg1, arg2)
            return LuaValue.NIL
        }
    })
}

fun LuaTable.setFunction3Arg(key: String, luaFun: (LuaValue, LuaValue, LuaValue) -> LuaValue) {
    set(key, object : ThreeArgFunction() {
        override fun call(arg1: LuaValue?, arg2: LuaValue?, arg3: LuaValue?): LuaValue {
            if (arg1 == null || arg2 == null || arg3 == null) return LuaValue.NIL
            return luaFun(arg1, arg2, arg3)
        }
    })
}

fun LuaTable.setFunction3ArgNoReturn(key: String, luaFun: (LuaValue, LuaValue, LuaValue) -> Unit) {
    set(key, object : ThreeArgFunction() {
        override fun call(arg1: LuaValue?, arg2: LuaValue?, arg3: LuaValue?): LuaValue {
            if (arg1 == null || arg2 == null || arg3 == null) return LuaValue.NIL
            luaFun(arg1, arg2, arg3)
            return LuaValue.NIL
        }
    })
}