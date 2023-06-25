package com.github.only52607.luamirai.lua.lib

import com.github.only52607.luakt.ValueMapper
import com.github.only52607.luakt.dsl.applyFrom
import com.github.only52607.luamirai.lua.mirai.bot.BotBuilderFunction
import com.github.only52607.luamirai.lua.mirai.bot.BotStoreTable
import com.github.only52607.luamirai.lua.mirai.event.EventTable
import com.github.only52607.luamirai.lua.mirai.message.MessageConstructorsTable
import kotlinx.coroutines.CoroutineScope
import net.mamoe.mirai.Mirai
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

open class MiraiLib(
    private val coroutineScope: CoroutineScope
) : TwoArgFunction() {
    override fun call(modName: LuaValue?, env: LuaValue): LuaValue? {
        val globals = env.checkglobals()
        val botStoreTable = BotStoreTable(coroutineScope, valueMapper)
        globals.set("Bot", BotBuilderFunction(coroutineScope, valueMapper, botStoreTable::addBot))
        globals.set("Bots", botStoreTable)
        val constructors = MessageConstructorsTable(valueMapper)
        globals.set("Message", constructors)
        globals.applyFrom(constructors)
        globals.set("Event", EventTable(coroutineScope, valueMapper))
        globals.set("Mirai", valueMapper.mapToLuaValue(Mirai))
        return globals
    }
}