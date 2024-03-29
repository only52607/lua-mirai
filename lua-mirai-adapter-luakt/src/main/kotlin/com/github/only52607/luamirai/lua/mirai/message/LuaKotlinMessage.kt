package com.github.only52607.luamirai.lua.mirai.message

/**
 * ClassName: MessageWrapper
 * Description:
 * date: 2022/1/8 18:05
 * @author ooooonly
 * @version
 */
import com.github.only52607.luakt.ValueMapper
import com.github.only52607.luakt.dsl.nullable
import com.github.only52607.luakt.userdata.classes.LuaKotlinClass
import com.github.only52607.luakt.userdata.objects.LuaKotlinObject
import net.mamoe.mirai.message.code.CodableMessage
import net.mamoe.mirai.message.data.*
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue

@Suppress("unused")
class LuaKotlinMessage(
    val message: Message,
    luaKotlinMessageClass: LuaKotlinClass,
    valueMapper: ValueMapper
) : LuaKotlinObject(message, luaKotlinMessageClass), ValueMapper by valueMapper {

    private val Message.luaValue: LuaValue
        get() = mapToLuaValue(this@Message)

    override fun typename(): String = when (message) {
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
        is ConstrainSingle -> message.key.toString()
        else -> this::class.simpleName ?: ""
    }

    /**
     * 1-based
     */
    private fun getMessageElement(index: Int) = when (message) {
        is MessageChain -> message.getOrNull(index - 1)?.luaValue ?: LuaValue.NIL
        else -> throw LuaError("$message is not a MessageChain")
    }

    private fun getMessageElement(type: String) = when (message) {
        is MessageChain -> message.find { typename() == type}?.luaValue
        else -> throw LuaError("$message is not a MessageChain")
    }

    override fun get(key: LuaValue): LuaValue {
        if (message !is MessageChain) return super.get(key)
        return when {
            key.isnumber() -> getMessageElement(key.checkint())
            key.isstring() -> getMessageElement(key.checkjstring())
            else -> NIL
        }?.nullable ?: super.get(key)
    }

    override fun eq_b(value: LuaValue?): Boolean = message.toString() == value.toString()
    override fun eq(value: LuaValue?): LuaValue = if (eq_b(value)) TRUE else FALSE
    override fun rawlen(): Int = if (message is MessageChain) message.size else 1
    override fun length(): Int = rawlen()
    override fun len(): LuaValue = LuaValue.valueOf(rawlen())
    override fun checktable(): LuaTable =
        if (message is MessageChain) LuaValue.listOf(message.map { it.luaValue }.toTypedArray())
        else throw LuaError("$message is not a MessageChain")

    private fun LuaValue.checkMessage() = if (isuserdata()) checkuserdata() as Message else PlainText(toString())
    override fun add(rhs: LuaValue): LuaValue = (message + rhs.checkMessage()).luaValue
    override fun concat(rhs: LuaValue): LuaValue = (message + rhs.checkMessage()).luaValue
    override fun concatTo(lhs: LuaString): LuaValue = (PlainText(lhs.checkjstring()) + message).luaValue
    override fun concatTo(lhs: LuaValue): LuaValue = (lhs.checkMessage() + message).luaValue

    override fun tojstring(): String = toString()
    override fun tostring(): LuaValue = LuaValue.valueOf(toString())
    override fun toString(): String = when (message) {
        is CodableMessage -> message.serializeToMiraiCode()
        else -> super.toString()
    }
}