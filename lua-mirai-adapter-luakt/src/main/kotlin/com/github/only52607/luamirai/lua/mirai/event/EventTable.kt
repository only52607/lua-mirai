package com.github.only52607.luamirai.lua.mirai.event

import com.github.only52607.luakt.ValueMapper
import com.github.only52607.luakt.dsl.unpackVarargs
import com.github.only52607.luakt.dsl.varArgFunctionOf
import kotlinx.coroutines.CoroutineScope
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.GlobalEventChannel
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaTable

/**
 * ClassName: EventTable
 * Description:
 * date: 2022/1/10 23:04
 * @author ooooonly
 * @version
 */

class EventTable(
    coroutineScope: CoroutineScope,
    valueMapper: ValueMapper
) : LuaTable(), ValueMapper by valueMapper, CoroutineScope by coroutineScope {
    init {
        this["subscribe"] = subscriberFunction
    }

    private val subscriberFunction
        get() = varArgFunctionOf { varargs ->
            var channel = GlobalEventChannel.parentScope(this)
            val varargsList = varargs.unpackVarargs()
            varargsList.take(varargs.narg() - 1).forEachIndexed { i, rule ->
                channel = when {
                    rule.isfunction() -> channel.filterByLuaFunction(rule.checkfunction(), this)
                    rule.isstring() -> channel.filterByEventName(rule.checkjstring())
                    rule.isuserdata() -> channel.filterByUserData(rule.checkuserdata())
                    else -> throw LuaError("Unknown filter rule in arg $i.")
                }
            }
            val listener = varargs.checkfunction(varargs.narg())
            return@varArgFunctionOf mapToLuaValue(channel.exceptionHandler {
                it.printStackTrace()
            }.subscribeAlways<Event> {
                listener.invoke(mapToLuaValue(it))
            })
        }
}