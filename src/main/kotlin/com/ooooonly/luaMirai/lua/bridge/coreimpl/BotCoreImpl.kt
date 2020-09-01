package com.ooooonly.luaMirai.lua.bridge.coreimpl

import com.ooooonly.luaMirai.lua.MiraiCoreGlobalsManger
import com.ooooonly.luaMirai.lua.bridge.EventConstants
import com.ooooonly.luaMirai.lua.bridge.base.BaseBot
import com.ooooonly.luaMirai.lua.bridge.base.BaseFriend
import com.ooooonly.luakt.*
import kotlinx.coroutines.*
import net.mamoe.mirai.*
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.message.FriendMessageEvent
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.utils.BotConfiguration
import org.luaj.vm2.*

class BotCoreImpl(val host: Bot) : BaseBot() {
    override var id: Long
        get() = host.id
        set(value) {
            throw UnsupportSetterLuaError
        }
    override var isActive: Boolean
        get() = host.isActive
        set(value) {
            throw UnsupportSetterLuaError
        }
    override var isOnline: Boolean
        get() = host.isOnline
        set(value) {
            throw UnsupportSetterLuaError
        }
    override var selfQQ: BaseFriend
        get() = FriendCoreImpl(host.selfQQ)
        set(value) {
            throw UnsupportSetterLuaError
        }

    override fun login() = also {
        runBlocking {
            host.login()
        }
    }

    companion object {
        fun setBotFactory(table: LuaTable) {
            table.set("Bot", luaFunctionOf { varargs: Varargs ->
                val user = varargs[0].asKValue<Long>()
                val pwd = varargs[1].asKValue<String>()
                val config =
                    if (varargs.narg() >= 3) BotConfiguration.Default.apply { fileBasedDeviceInfo(varargs[2].asKValue()) }
                    else BotConfiguration.Default
                return@luaFunctionOf BotCoreImpl(Bot(user, pwd, config))
            })
        }

        private val instances = mutableMapOf<Bot, BotCoreImpl>()
        fun getInstance(host: Bot): BotCoreImpl {
            if (!instances.contains(host)) instances[host] = BotCoreImpl(host)
            return instances[host]!!
        }
    }

    override fun getFriend(id: Long): FriendCoreImpl = FriendCoreImpl(host.getFriend(id))

    override fun getGroup(id: Long): GroupCoreImpl = GroupCoreImpl(host.getGroup(id))

    override fun getFriends(): Array<FriendCoreImpl> = host.friends.map { FriendCoreImpl(it) }.toTypedArray()

    override fun getGroups(): Array<GroupCoreImpl> = host.groups.map { GroupCoreImpl(it) }.toTypedArray()

    override fun launch(block: LuaClosure): Job = host.launch { block.invoke() }

    override fun containsFriend(id: Long): Boolean = host.containsFriend(id)

    override fun containsGroup(id: Long): Boolean = host.containsGroup(id)

    override fun subscribe(eventName: String, block: LuaClosure): CompletableJob {
        //println("subscribe:$eventId scriptId:${MiraiCoreGlobals.checkScriptId(block)}")
        val event = EventConstants.events[eventName] ?: throw LuaError("No such event name!")
        val job = host.subscribeAlways(event) {
            block.invoke(this)
        }
        MiraiCoreGlobalsManger.checkGlobals(block)?.addListener(job)
        return job
    }

    override fun join() = runBlocking {
        host.join()
    }

    override fun close() = runBlocking {
        host.close()
    }

}