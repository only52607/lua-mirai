package com.github.only52607.luamirai.lua.mirai

/**
 * ClassName: MessageWrapper
 * Description:
 * date: 2022/1/8 18:05
 * @author ooooonly
 * @version
 */
import com.github.only52607.luakt.KotlinInstance
import com.github.only52607.luakt.extension.nullable
import net.mamoe.mirai.message.code.CodableMessage
import net.mamoe.mirai.message.data.*
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue

@Suppress("unused")
class LuaMessage(
    private val message: Message
) : KotlinInstance(message) {
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
        is MessageChain -> message.getOrNull(index - 1)?.let(::LuaMessage) ?: LuaValue.NIL
        else -> throw LuaError("$message is not a MessageChain")
    }

    private fun getMessageElement(type: String) = when (message) {
        is MessageChain -> message.find { typename() == type}?.let(::LuaMessage)
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

    private fun LuaValue.checkMessage() = if (isuserdata()) checkuserdata() as Message else PlainText(toString())

    override fun eq_b(value: LuaValue?): Boolean = message.toString() == value.toString()

    override fun eq(value: LuaValue?): LuaValue = if (eq_b(value)) TRUE else FALSE

    override fun rawlen(): Int = if (message is MessageChain) message.size else 1

    override fun length(): Int = rawlen()

    override fun len(): LuaValue = LuaValue.valueOf(rawlen())

    override fun checktable(): LuaTable =
        if (message is MessageChain) LuaValue.listOf(message.map(::LuaMessage).toTypedArray())
        else throw LuaError("$message is not a MessageChain")

    override fun add(rhs: LuaValue): LuaValue = (message + rhs.checkMessage()).let(::LuaMessage)

    override fun concat(rhs: LuaValue): LuaValue = (message + rhs.checkMessage()).let(::LuaMessage)

    override fun concatTo(lhs: LuaString): LuaValue = (PlainText(lhs.checkjstring()) + message).let(::LuaMessage)

    override fun concatTo(lhs: LuaValue): LuaValue = (lhs.checkMessage() + message).let(::LuaMessage)

    override fun tojstring(): String = toString()

    override fun tostring(): LuaValue = LuaValue.valueOf(toString())

    override fun toString(): String = when (message) {
        is CodableMessage -> message.serializeToMiraiCode()
        else -> super.toString()
    }
}