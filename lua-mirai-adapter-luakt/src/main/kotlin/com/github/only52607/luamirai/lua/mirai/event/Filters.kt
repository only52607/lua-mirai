package com.github.only52607.luamirai.lua.mirai.event

import com.github.only52607.luamirai.lua.utils.miraiEventNames
import com.ooooonly.luakt.mapper.ValueMapper
import com.ooooonly.luakt.utils.asLuaValue
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.EventChannel
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.event.events.UserEvent
import net.mamoe.mirai.utils.MiraiExperimentalApi
import net.mamoe.mirai.utils.MiraiInternalApi
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaFunction

/**
 * ClassName: Filters
 * Description:
 * date: 2022/1/10 23:01
 * @author ooooonly
 * @version
 */

fun EventChannel<*>.filterByLuaFunction(luaFunction: LuaFunction, valueMapper: ValueMapper): EventChannel<*> = filter {
    val result = luaFunction.invoke(it.asLuaValue(valueMapper))
    return@filter !result.isnil(0)
}

@OptIn(MiraiExperimentalApi::class, MiraiInternalApi::class)
fun EventChannel<*>.filterByEventName(eventName: String): EventChannel<*> = filter { event ->
    val storeEventName = miraiEventNames[event::class]
    if (storeEventName != null) {
        return@filter storeEventName == eventName
    }
    return@filter event::class.simpleName == eventName
}

fun EventChannel<*>.filterByUserData(obj: Any): EventChannel<*> =
    when (obj) {
        is Bot -> {
            filter {
                it is BotEvent && it.bot.id == obj.id
            }
        }
        is Contact -> {
            filter {
                it is UserEvent && it.user.id == obj.id
            }
        }
        else -> throw LuaError("Unknown userdata filter rule ${obj}.")
    }