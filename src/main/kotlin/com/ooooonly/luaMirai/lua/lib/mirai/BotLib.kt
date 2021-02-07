package com.ooooonly.luaMirai.lua.lib.mirai

import com.ooooonly.luakt.*
import kotlinx.coroutines.CoroutineScope
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.SimpleLogger
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.TwoArgFunction

@Suppress("unused")
class BotLib(private val coroutineScope: CoroutineScope) : TwoArgFunction() {
    override fun call(modName: LuaValue?, env: LuaValue): LuaValue? {
        val globals = env.checkglobals()
        globals.edit {
            "Bot" to varArgFunctionOf { varargs: Varargs ->
                val user = varargs[0].asKValue<Long>()
                val pwd = varargs[1].asKValue<String>()
                val config = BotConfiguration.Default.setScope()
                if (varargs.narg() >= 3) {
                    if (varargs[2].istable()) varargs[2].checktable().toBotConfiguration().setScope()
                    else BotConfiguration.Default.copy().setDeviceFile(varargs[2].asKValue()).setScope()
                }
                return@varArgFunctionOf BotFactory.newBot(user, pwd, config).asLuaValue()
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