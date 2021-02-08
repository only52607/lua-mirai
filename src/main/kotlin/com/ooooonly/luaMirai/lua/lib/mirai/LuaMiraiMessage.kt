package com.ooooonly.luaMirai.lua.lib.mirai

import com.ooooonly.luakt.asLuaValue
import com.ooooonly.luakt.mapper.userdata.KotlinInstanceWrapper
import net.mamoe.mirai.message.code.CodableMessage
import net.mamoe.mirai.message.data.*
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue

@Suppress("unused")
class LuaMiraiMessage(val message: Message) : KotlinInstanceWrapper(message) {

    private fun Message.typename(): String = when (this) {
        is MessageChain -> "MessageChain"
        is MessageSource -> "MessageSource"
        is ConstrainSingle -> this.key.toString()
        else -> this::class.simpleName ?: ""
    }

    override fun typename(): String = message.typename()

    override fun rawget(key: String?): LuaValue = when (message) {
        is MessageChain -> message.find { it.typename() == key }.asLuaValue()
        else -> message.takeIf { it.typename() == key }?.asLuaValue() ?: LuaValue.NIL
    }

    override fun rawget(key: Int): LuaValue = when (message) {
        is MessageChain -> message.getOrNull(key - 1)?.asLuaValue() ?: LuaValue.NIL
        else -> message.takeIf { key == 1 }?.asLuaValue() ?: LuaValue.NIL
    }

    override fun get(key: String?): LuaValue = rawget(key)

    override fun get(key: Int): LuaValue = rawget(key)

    override fun get(key: LuaValue): LuaValue = when {
        key.isnumber() -> rawget(key.checkint())
        key.isstring() -> rawget(key.checkjstring())
        else -> NIL
    }

    override fun rawlen(): Int = if (message is MessageChain) message.size else 1
    override fun length(): Int = rawlen()
    override fun len(): LuaValue = LuaValue.valueOf(rawlen())
    override fun checktable(): LuaTable =
        if (message is MessageChain) LuaValue.listOf(message.map { it.asLuaValue() }.toTypedArray())
        else LuaValue.listOf(arrayOf(this))

    private fun LuaValue.checkMessage() = if (isuserdata()) checkuserdata() as Message else PlainText(toString())
    override fun add(rhs: LuaValue): LuaValue = (message + rhs.checkMessage()).asLuaValue()
    override fun concat(rhs: LuaValue): LuaValue = (message + rhs.checkMessage()).asLuaValue()
    override fun concatTo(lhs: LuaString): LuaValue = (PlainText(lhs.checkjstring()) + message).asLuaValue()
    override fun concatTo(lhs: LuaValue): LuaValue = (lhs.checkMessage() + message).asLuaValue()

    override fun tojstring(): String = toString()
    override fun tostring(): LuaValue = LuaValue.valueOf(toString())
    override fun toString(): String = when (message) {
        is CodableMessage -> message.serializeToMiraiCode()
        else -> super.toString()
    }
}