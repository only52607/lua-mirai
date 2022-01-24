package com.github.only52607.luamirai.lua.lib

import com.github.only52607.luakt.ValueMapper
import com.github.only52607.luakt.utils.setFrom
import com.github.only52607.luamirai.lua.mirai.bot.BotBuilderFunction
import com.github.only52607.luamirai.lua.mirai.bot.BotStoreTable
import com.github.only52607.luamirai.lua.mirai.event.EventTable
import com.github.only52607.luamirai.lua.mirai.message.MessageConstructorsTable
import kotlinx.coroutines.CoroutineScope
import net.mamoe.mirai.Mirai
import net.mamoe.mirai.utils.MiraiExperimentalApi
import net.mamoe.mirai.utils.MiraiInternalApi
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

@OptIn(MiraiExperimentalApi::class, MiraiInternalApi::class)
open class MiraiLib(
    private val coroutineScope: CoroutineScope,
    private val valueMapper: ValueMapper
) : TwoArgFunction() {
    override fun call(modName: LuaValue?, env: LuaValue): LuaValue? {
        val globals = env.checkglobals()
        val botStoreTable = BotStoreTable(coroutineScope, valueMapper)
        globals.set("Bot", BotBuilderFunction(coroutineScope, valueMapper, botStoreTable::addBot))
        globals.set("Bots", botStoreTable)
        val constructors = MessageConstructorsTable(valueMapper)
        globals.set("Message", constructors)
        globals.setFrom(constructors)
        globals.set("Event", EventTable(coroutineScope, valueMapper))
        globals.set("Mirai", valueMapper.mapToLuaValue(Mirai))
        return globals
    }
}