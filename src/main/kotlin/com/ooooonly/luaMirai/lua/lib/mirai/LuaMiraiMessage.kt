package com.ooooonly.luaMirai.lua.lib.mirai

import com.ooooonly.luakt.asLuaValue
import com.ooooonly.luakt.mapper.userdata.KotlinInstanceWrapper
import net.mamoe.mirai.message.code.CodableMessage
import net.mamoe.mirai.message.data.*
import org.luaj.vm2.LuaError
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

    /**
     * 1-based
     */
    private fun getMessageElement(index: Int) = when (message) {
        is MessageChain -> message.getOrNull(index - 1)?.asLuaValue() ?: LuaValue.NIL
        else -> throw LuaError("$message is not a MessageChain.")
    }

    private fun getMessageElement(type: String) = when (message) {
        is MessageChain -> message.find { it.typename() == type }.asLuaValue()
        else -> throw LuaError("$message is not a MessageChain.")
    }

    override fun get(key: LuaValue): LuaValue = when {
        key.isnumber() -> getMessageElement(key.checkint())
        key.isstring() -> getMessageElement(key.checkjstring())
        else -> NIL
    }?.takeIf { !it.isnil() } ?: super.get(key)

    override fun eq(value: LuaValue?): LuaValue = valueOf(message.toString() == value.toString())
    override fun rawlen(): Int = if (message is MessageChain) message.size else 1
    override fun length(): Int = rawlen()
    override fun len(): LuaValue = LuaValue.valueOf(rawlen())
    override fun checktable(): LuaTable =
        if (message is MessageChain) LuaValue.listOf(message.map { it.asLuaValue() }.toTypedArray())
        else throw LuaError("$message is not a MessageChain.")

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