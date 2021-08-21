package com.ooooonly.luaMirai.lua.lib.mirai

import com.ooooonly.luaMirai.utils.miraiEventNames
import com.ooooonly.luakt.*
import com.ooooonly.luakt.mapper.ValueMapperChain
import com.ooooonly.luakt.mapper.userdata.KotlinInstanceWrapper
import kotlinx.coroutines.CoroutineScope
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.ContactOrBot
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.EventChannel
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.event.events.UserEvent
import net.mamoe.mirai.utils.MiraiExperimentalApi
import net.mamoe.mirai.utils.MiraiInternalApi
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaFunction
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

@MiraiInternalApi
@Suppress("unused")
@OptIn(MiraiExperimentalApi::class, MiraiInternalApi::class)
class EventLib(private val coroutineScope: CoroutineScope) : TwoArgFunction() {
    @OptIn(MiraiExperimentalApi::class, MiraiInternalApi::class)
    override fun call(modName: LuaValue?, env: LuaValue): LuaValue? {
        val globals = env.checkglobals()
        globals.edit {
            "Event" to eventTable
        }
        ValueMapperChain.addKValueMapperBefore { obj, _ ->
            if (obj is ContactOrBot) return@addKValueMapperBefore KotlinInstanceWrapper(obj).apply {
                m_metatable = buildLuaTable {
                    INDEX to eventTable
                }
            }
            return@addKValueMapperBefore null
        }
        return LuaValue.NIL
    }

    @OptIn(MiraiExperimentalApi::class, MiraiInternalApi::class)
    private val eventTable: LuaTable by lazy {
        buildLuaTable {
            "subscribe" to subscriber
        }
    }

    @OptIn(MiraiExperimentalApi::class, MiraiInternalApi::class)
    private val subscriber
        get() = varArgFunctionOf { varargs ->
            var channel = GlobalEventChannel.parentScope(coroutineScope)
            val varargsList = varargs.toList()
            varargsList.take(varargs.narg() - 1).forEachIndexed { i, rule ->
                channel = when {
                    rule.isfunction() -> channel.filterByLuaFunction(rule.checkfunction())
                    rule.isstring() -> channel.filterByEventName(rule.checkjstring())
                    rule.isuserdata() -> channel.filterByUserData(rule.checkuserdata())
                    else -> throw LuaError("Unknown filter rule in arg $i.")
                }
            }
            val listener = varargs.checkfunction(varargs.narg())
            return@varArgFunctionOf channel.exceptionHandler {
                it.printStackTrace()
            }.subscribeAlways<Event> {
                listener.invoke(it.asLuaValue())
            }.asLuaValue()
        }

    private fun EventChannel<*>.filterByLuaFunction(luaFunction: LuaFunction): EventChannel<*> = filter {
        val result = luaFunction.invoke(it.asLuaValue())
        return@filter !result.isnil(0)
    }

    @OptIn(MiraiExperimentalApi::class, MiraiInternalApi::class)
    private fun EventChannel<*>.filterByEventName(eventName: String): EventChannel<*> = filter { event ->
        val storeEventName = miraiEventNames[event::class]
        if (storeEventName != null) {
            return@filter storeEventName == eventName
        }
        return@filter event::class.simpleName == eventName
    }

    private fun EventChannel<*>.filterByUserData(obj: Any): EventChannel<*> =
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
}