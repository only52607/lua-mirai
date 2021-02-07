package com.ooooonly.luaMirai.lua.lib.mirai

import com.ooooonly.luakt.asLuaValue
import com.ooooonly.luakt.mapper.userdata.KotlinInstanceWrapper
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue

@Suppress("unused")
class LuaMiraiMessage(val message: Message) : KotlinInstanceWrapper(message) {
    private fun checkForMessageType(type: String) = when (message) {
        is MessageChain -> message.find { it::class.simpleName == type }.asLuaValue()
        else -> message.takeIf { it::class.simpleName == type }?.asLuaValue() ?: LuaValue.NIL
    }

    private fun checkForMessageIndex(index: Int) = when (message) {
        is MessageChain -> message.getOrNull(index - 1)?.asLuaValue() ?: LuaValue.NIL
        else -> message.takeIf { index == 1 }?.asLuaValue() ?: LuaValue.NIL
    }

    override fun get(key: LuaValue): LuaValue = when {
        key.isstring() -> checkForMessageType(key.checkjstring())
        key.isint() -> checkForMessageIndex(key.checkint())
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
}