package com.github.only52607.luamirai.lua.mapper

import com.github.only52607.luakt.KValueMapper
import com.github.only52607.luakt.ValueMapper
import com.github.only52607.luakt.mappers.AbstractKValueMapper
import com.github.only52607.luakt.userdata.classes.LuaKotlinClassRegistry
import com.github.only52607.luamirai.lua.mirai.message.LuaKotlinMessage
import net.mamoe.mirai.message.data.Message
import org.luaj.vm2.LuaValue

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
            return LuaKotlinMessage(obj, registry.obtainLuaKotlinClass(obj::class), valueMapper)
        }
        return nextMapToLuaValue(obj)
    }
}