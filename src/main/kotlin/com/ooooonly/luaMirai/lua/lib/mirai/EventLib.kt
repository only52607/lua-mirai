package com.ooooonly.luaMirai.lua.lib.mirai

import com.ooooonly.luakt.*
import kotlinx.coroutines.CoroutineScope
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.EventChannel
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.event.events.UserEvent
import net.mamoe.mirai.utils.MiraiInternalApi
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaFunction
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction
import kotlin.reflect.full.allSuperclasses


@Suppress("unused")
class EventLib(private val coroutineScope: CoroutineScope) : TwoArgFunction() {
    @MiraiInternalApi
    override fun call(modName: LuaValue?, env: LuaValue): LuaValue? {
        val globals = env.checkglobals()
        globals.edit {
            "Event" to eventTable
        }
        return LuaValue.NIL
    }

    private val eventTable: LuaTable
        get() = buildLuaTable {
            "subscribe" to subscriber
        }

    private val subscriber
        get() = varArgFunctionOf { varargs ->
            var channel = GlobalEventChannel.parentScope(coroutineScope)
            val varargsList = varargs.toList()
            varargsList.take(varargs.narg() - 1).forEachIndexed { i, rule ->
                if (rule.isfunction()) {
                    channel = channel.filterByLuaFunction(rule.checkfunction())
                } else if (rule.isstring()) {
                    channel = channel.filter { event ->
                        event::class.allSuperclasses.any { clazz -> clazz.simpleName == rule.checkjstring() }
                    }
                } else if (rule.isuserdata()) {
                    channel = when (val obj = rule.checkuserdata()) {
                        is Bot -> {
                            channel.filter {
                                it is BotEvent && it.bot.id == obj.id
                            }
                        }
                        is Contact -> {
                            channel.filter {
                                it is UserEvent && it.user.id == obj.id
                            }
                        }
                        else -> throw LuaError("Unknown filter rule in arg $i.")
                    }
                } else throw LuaError("Unknown filter rule in arg $i.")
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
}