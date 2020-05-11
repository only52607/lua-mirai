package com.ooooonly.luaMirai.utils

import com.ooooonly.luaMirai.lua.MiraiMsg
import com.ooooonly.luaMirai.lua.MiraiSource
import net.mamoe.mirai.message.data.*
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaValue

inline fun <reified T : LuaValue> LuaValue.takeIfType(): T? {
    if (this is T) return this
    return null
}

inline fun <reified T : LuaValue> LuaValue.checkIfType(): T {
    if (this is T) return this
    throw LuaError("Expected ${T::class.simpleName},but got ${this.javaClass.simpleName}!")
}

inline fun <reified T : LuaValue> LuaValue.takeUnlessType(): LuaValue? {
    if (this !is T) return this
    return null
}

inline fun <reified T : LuaValue> LuaValue.checkUnlessType(): LuaValue {
    if (this !is T) return this
    throw LuaError("Parameters can't be ${T::class.simpleName}!")
}

inline fun LuaValue.checkMessageChain(): Message {
    return when (this) {
        is LuaString -> PlainText(this.checkjstring())
        is MiraiMsg -> this.chain
        else -> throw LuaError("Message expected MiraiMsg or String,but got ${this.javaClass.simpleName}!")
    }
}

inline fun LuaValue.checkMessageSource(): MessageSource {
    return when (this) {
        is MiraiSource -> this.source
        is MiraiMsg -> this.chain[MessageSource] ?: throw LuaError("MiraiMsg does not contain a source!")
        else -> throw LuaError("Quote must be MiraiSource or MiraiMsg")
    }
}


fun Int.toLuaValue(): LuaValue {
    return LuaValue.valueOf(this)
}

fun String.toLuaValue(): LuaValue {
    return LuaValue.valueOf(this)
}

fun Boolean.toLuaValue(): LuaValue {
    return LuaValue.valueOf(this)
}

fun Long.toLuaValue(): LuaValue {
    return LuaValue.valueOf(this.toString())
}