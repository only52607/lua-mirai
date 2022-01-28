package com.github.only52607.luamirai.lua.mirai.event

import com.github.only52607.luakt.ValueMapper
import com.github.only52607.luakt.utils.asLuaValue
import com.github.only52607.luakt.utils.provideScope
import com.github.only52607.luakt.utils.varArgFunctionOf
import com.github.only52607.luakt.utils.varargsToLuaValueList
import kotlinx.coroutines.CoroutineScope
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.utils.MiraiExperimentalApi
import net.mamoe.mirai.utils.MiraiInternalApi
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaTable

/**
 * ClassName: EventTable
 * Description:
 * date: 2022/1/10 23:04
 * @author ooooonly
 * @version
 */

@OptIn(MiraiExperimentalApi::class, MiraiInternalApi::class)
class EventTable(
    private val coroutineScope: CoroutineScope,
    private val valueMapper: ValueMapper
) : LuaTable() {
    init {
        valueMapper.provideScope {
            edit {
                "subscribe" to subscriberFunction
            }
        }
    }

    private val subscriberFunction
        get() = varArgFunctionOf { varargs ->
            var channel = GlobalEventChannel.parentScope(coroutineScope)
            val varargsList = varargs.varargsToLuaValueList()
            varargsList.take(varargs.narg() - 1).forEachIndexed { i, rule ->
                channel = when {
                    rule.isfunction() -> channel.filterByLuaFunction(rule.checkfunction(), valueMapper)
                    rule.isstring() -> channel.filterByEventName(rule.checkjstring())
                    rule.isuserdata() -> channel.filterByUserData(rule.checkuserdata())
                    else -> throw LuaError("Unknown filter rule in arg $i.")
                }
            }
            val listener = varargs.checkfunction(varargs.narg())
            return@varArgFunctionOf channel.exceptionHandler {
                it.printStackTrace()
            }.subscribeAlways<Event> {
                listener.invoke(it.asLuaValue(valueMapper))
            }.asLuaValue(valueMapper)
        }
}