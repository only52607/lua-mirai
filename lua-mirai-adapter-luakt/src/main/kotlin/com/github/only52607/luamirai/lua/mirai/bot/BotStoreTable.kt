package com.github.only52607.luamirai.lua.mirai.bot

import com.ooooonly.luakt.mapper.ValueMapper
import kotlinx.coroutines.CoroutineScope
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.BotEvent
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue

/**
 * ClassName: BotStoreTable
 * Description:
 * date: 2022/1/10 23:21
 * @author ooooonly
 * @version
 */
class BotStoreTable(
    coroutineScope: CoroutineScope,
    private val valueMapper: ValueMapper
) : LuaTable() {
    private val botIdSet = mutableSetOf<Long>() // 记录已加载的bot

    init {
        Bot.instances.forEach(::addBot)
        GlobalEventChannel.parentScope(coroutineScope)
            .subscribeAlways<BotEvent> { addBot(this@subscribeAlways.bot) }
    }

    fun addBot(bot: Bot): LuaValue {
        if (!botIdSet.contains(bot.id)) {
            val botLuaValue = valueMapper.mapToLuaValue(bot)
            set(bot.id.toString(), botLuaValue)
            botIdSet.add(bot.id)
            return botLuaValue
        }
        return get(bot.id.toString())
    }
}