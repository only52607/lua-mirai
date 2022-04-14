package com.github.only52607.luamirai.lua.mapper

import com.github.only52607.luakt.KValueMapper
import com.github.only52607.luakt.LuaValueMapper
import com.github.only52607.luakt.ValueMapper
import com.github.only52607.luakt.mappers.AbstractKValueMapper
import com.github.only52607.luakt.mappers.AbstractLuaValueMapper
import com.github.only52607.luakt.userdata.classes.LuaKotlinClass
import com.github.only52607.luakt.userdata.classes.registry.LuaKotlinClassRegistry
import com.github.only52607.luamirai.lua.mirai.message.LuaKotlinMessage
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChain
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.toMessageChain
import org.luaj.vm2.LuaValue
import kotlin.reflect.KClass
import kotlin.reflect.full.isSuperclassOf

/**
 * ClassName: LuaMiraiMessageMapper
 * Description:
 * date: 2022/1/8 17:58
 * @author ooooonly
 * @version
 */
class LuaMiraiMessageMapper(
    private val registry: LuaKotlinClassRegistry,
    private val valueMapper: ValueMapper,
    override var firstKValueMapper: KValueMapper? = null,
    override var nextKValueMapper: KValueMapper? = null
) : AbstractKValueMapper() {
    override fun mapToLuaValue(obj: Any?): LuaValue {
        if (obj is Message) {
            return LuaKotlinMessage(obj, registry.obtainLuaKotlinClass(obj::class) as LuaKotlinClass, valueMapper)
        }
        return nextMapToLuaValue(obj)
    }
}

class LuaValueToLuaMiraiMessageMapper(
    override var firstLuaValueMapper: LuaValueMapper? = null,
    override var nextLuaValueMapper: LuaValueMapper? = null
) : AbstractLuaValueMapper() {
    override fun mapToKValue(luaValue: LuaValue, targetClass: KClass<*>?): Any {
        targetClass ?: return Unit
        if (luaValue.isuserdata()) return nextMapToKValue(luaValue, targetClass)
        if (targetClass.isSuperclassOf(Message::class)) {
            return PlainText(luaValue.tojstring())
        }
        if (targetClass.isSuperclassOf(MessageChain::class)) {
            return PlainText(luaValue.tojstring()).toMessageChain()
        }
        return nextMapToKValue(luaValue, targetClass)
    }
}