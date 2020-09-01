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
import net.mamoe.mirai.utils.SimpleLogger
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
                    if (varargs.narg() >= 3) {
                        if (varargs[2].istable()) (varargs[2] as LuaTable).toBotConfiguration()
                        else BotConfiguration.Default.copy().apply { fileBasedDeviceInfo(varargs[2].asKValue()) }
                    } else BotConfiguration.Default
                return@luaFunctionOf BotCoreImpl(Bot(user, pwd, config))
            })
        }

        private val instances = mutableMapOf<Bot, BotCoreImpl>()
        fun getInstance(host: Bot): BotCoreImpl {
            if (!instances.contains(host)) instances[host] = BotCoreImpl(host)
            return instances[host]!!
        }

        private fun LuaTable.toBotConfiguration(): BotConfiguration = BotConfiguration.Default.copy().apply {
            getOrNull("protocol")?.let {
                protocol = it.toString().toMiraiProtocol()
            }
            getOrNull("fileBasedDeviceInfo")?.let {
                fileBasedDeviceInfo(it.toString())
            }
            getOrNull("heartbeatPeriodMillis")?.let {
                heartbeatPeriodMillis = it.checklong()
            }
            getOrNull("heartbeatTimeoutMillis")?.let {
                heartbeatTimeoutMillis = it.checklong()
            }
            getOrNull("firstReconnectDelayMillis")?.let {
                firstReconnectDelayMillis = it.checklong()
            }
            getOrNull("reconnectPeriodMillis")?.let {
                reconnectPeriodMillis = it.checklong()
            }
            getOrNull("reconnectionRetryTimes")?.let {
                reconnectionRetryTimes = it.checkint()
            }
            getOrNull("noNetworkLog")?.let {
                if (it.checkboolean()) noNetworkLog()
            }
            getOrNull("noBotLog")?.let {
                if (it.checkboolean()) noBotLog()
            }
            getOrNull("botLogger")?.let {
                val luaLogger = it.checkclosure()
                botLoggerSupplier = {
                    SimpleLogger { message, _ ->
                        luaLogger.call(message)
                    }
                }
            }
            getOrNull("networkLogger")?.let {
                val luaLogger = it.checkclosure()
                networkLoggerSupplier = {
                    SimpleLogger { message, _ ->
                        luaLogger.call(message)
                    }
                }
            }
        }

        private fun String.toMiraiProtocol(): BotConfiguration.MiraiProtocol = when (this) {
            "ANDROID_PHONE" -> BotConfiguration.MiraiProtocol.ANDROID_PHONE
            "ANDROID_PAD" -> BotConfiguration.MiraiProtocol.ANDROID_PAD
            "ANDROID_WATCH" -> BotConfiguration.MiraiProtocol.ANDROID_WATCH
            else -> throw LuaError("No such Protocol")
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