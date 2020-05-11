package com.ooooonly.luaMirai.utils

import org.luaj.vm2.LuaValue

inline fun <reified T : LuaValue> LuaValue.takeIfType(): T? {
    if (this is T) return this
    return null
}

inline fun <reified T : LuaValue> LuaValue.takeUnlessType(): LuaValue? {
    if (this !is T) return this
    return null
}