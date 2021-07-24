package com.ooooonly.luaMirai.lua.lib.mirai

import com.ooooonly.luakt.*
import kotlinx.coroutines.CoroutineScope
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.SimpleLogger
import org.luaj.vm2.*
import org.luaj.vm2.lib.TwoArgFunction

@Suppress("unused")
class BotLib(private val coroutineScope: CoroutineScope) : TwoArgFunction() {
    private val botIdSet = mutableSetOf<Long>() // 记录已加载的bot

    override fun call(modName: LuaValue?, env: LuaValue): LuaValue? {
        val globals = env.checkglobals()
        val botsTable = LuaTable()
        globals.set("Bots", botsTable)
        // bot lib被加载后，先读取所有bot实例到记录
        Bot.instances.forEach {
            botsTable[it.id] = it
            botIdSet.add(it.id)
        }
        // 监听bot创建事件，当bot被创建后加入记录
        GlobalEventChannel.parentScope(coroutineScope).subscribeAlways<BotEvent> {
            if (!botIdSet.contains(this@subscribeAlways.bot.id)) {
                botsTable[this@subscribeAlways.bot.id] = this@subscribeAlways.bot
                botIdSet.add(this@subscribeAlways.bot.id)
            }
        }
        globals.edit {
            "Bot" to varArgFunctionOf { varargs: Varargs ->
                val account = varargs[0].checklong()
                val pwd = varargs[1].checkjstring()
                val config = if (varargs.narg() >= 3) {
                    if (varargs[2].istable()) varargs[2].checktable().toBotConfiguration().setScope()
                    else BotConfiguration.Default.copy().setDeviceFile(varargs[2].checkjstring()).setScope()
                } else {
                    BotConfiguration.Default.setScope()
                }
                val bot = BotFactory.newBot(account, pwd, config)
                botIdSet.add(account)
                botsTable[account] = bot
                return@varArgFunctionOf bot.asLuaValue()
            }
        }
        return LuaValue.NIL
    }

    private fun BotConfiguration.setDeviceFile(fileName: String) = apply {
        fileBasedDeviceInfo(fileName)
    }

    private fun BotConfiguration.setScope() = apply {
        parentCoroutineContext = coroutineScope.coroutineContext
    }

    private fun LuaTable.toBotConfiguration(): BotConfiguration = BotConfiguration.Default.copy().apply {
        getOrNull("protocol")?.let {
            protocol = it.toString().asMiraiProtocol()
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

    private fun String.asMiraiProtocol(): BotConfiguration.MiraiProtocol = when (this) {
        "ANDROID_PHONE" -> BotConfiguration.MiraiProtocol.ANDROID_PHONE
        "ANDROID_PAD" -> BotConfiguration.MiraiProtocol.ANDROID_PAD
        "ANDROID_WATCH" -> BotConfiguration.MiraiProtocol.ANDROID_WATCH
        else -> throw LuaError("No such protocol named $this.")
    }
}