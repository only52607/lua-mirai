package com.github.only52607.luamirai.lua.mirai.message

/**
 * ClassName: MessageWrapper
 * Description:
 * date: 2022/1/8 18:05
 * @author ooooonly
 * @version
 */
import com.ooooonly.luakt.mapper.ValueMapper
import com.ooooonly.luakt.mapper.userdata.*
import com.ooooonly.luakt.utils.asLuaValue
import com.ooooonly.luakt.utils.luaFunctionOf
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.message.code.CodableMessage
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.MessageSource.Key.recall
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue


@Suppress("unused")
class LuaKotlinMessage(
    val message: Message,
    luaKotlinMessageClass: LuaKotlinClass,
    val valueMapper: ValueMapper
) : LuaKotlinObject(message, luaKotlinMessageClass) {
    private fun Message.typename(): String = when (this) {
        is MessageChain -> "MessageChain"
        is MessageSource -> "MessageSource"
        is QuoteReply -> "QuoteReply"
        is PlainText -> "PlainText"
        is At -> "At"
        is Image -> "Image"
        is FlashImage -> "FlashImage"
        is AtAll -> "AtAll"
        is Face -> "Face"
        is VipFace -> "VipFace"
        is PokeMessage -> "PokeMessage"
        is ForwardMessage -> "ForwardMessage"
        is LightApp -> "LightApp"
        is Audio -> "Audio"
        is ServiceMessage -> "ServiceMessage"
        is ConstrainSingle -> this.key.toString()
        else -> this::class.simpleName ?: ""
    }

    override fun typename(): String = message.typename()

    /**
     * 1-based
     */
    private fun getMessageElement(index: Int) = when (message) {
        is MessageChain -> message.getOrNull(index - 1)?.asLuaValue(valueMapper) ?: LuaValue.NIL
        else -> throw LuaError("$message is not a MessageChain.")
    }

    private fun getMessageElement(type: String) = when (message) {
        is MessageChain -> message.find { it.typename() == type }.asLuaValue(valueMapper)
        else -> throw LuaError("$message is not a MessageChain.")
    }

    private fun getExtendFunction(name: String) = when(name) {
        "recall" -> luaFunctionOf(valueMapper) { message: MessageSource ->
            runBlocking { message.recall() }
        }
        else -> null
    }

    override fun get(key: LuaValue): LuaValue {
        if (message !is MessageChain) return when {
            key.isstring() -> getExtendFunction(key.checkjstring())
            else -> null
        } ?: super.get(key)
        return when {
            key.isnumber() -> getMessageElement(key.checkint())
            key.isstring() -> getMessageElement(key.checkjstring())
            else -> NIL
        }?.takeIf { !it.isnil() } ?: super.get(key)
    }

    override fun eq_b(value: LuaValue?): Boolean = message.toString() == value.toString()
    override fun eq(value: LuaValue?): LuaValue = if (eq_b(value)) TRUE else FALSE
    override fun rawlen(): Int = if (message is MessageChain) message.size else 1
    override fun length(): Int = rawlen()
    override fun len(): LuaValue = LuaValue.valueOf(rawlen())
    override fun checktable(): LuaTable =
        if (message is MessageChain) LuaValue.listOf(message.map { it.asLuaValue(valueMapper) }.toTypedArray())
        else throw LuaError("$message is not a MessageChain.")

    private fun LuaValue.checkMessage() = if (isuserdata()) checkuserdata() as Message else PlainText(toString())
    override fun add(rhs: LuaValue): LuaValue = (message + rhs.checkMessage()).asLuaValue(valueMapper)
    override fun concat(rhs: LuaValue): LuaValue = (message + rhs.checkMessage()).asLuaValue(valueMapper)
    override fun concatTo(lhs: LuaString): LuaValue = (PlainText(lhs.checkjstring()) + message).asLuaValue(valueMapper)
    override fun concatTo(lhs: LuaValue): LuaValue = (lhs.checkMessage() + message).asLuaValue(valueMapper)

    override fun tojstring(): String = toString()
    override fun tostring(): LuaValue = LuaValue.valueOf(toString())
    override fun toString(): String = when (message) {
        is CodableMessage -> message.serializeToMiraiCode()
        else -> super.toString()
    }
}