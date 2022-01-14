package com.github.only52607.luamirai.lua.mirai.bot

import com.ooooonly.luakt.mapper.ValueMapper
import com.ooooonly.luakt.utils.get
import com.ooooonly.luakt.utils.provideScope
import kotlinx.coroutines.CoroutineScope
import net.mamoe.mirai.Bot
import net.mamoe.mirai.BotFactory
import net.mamoe.mirai.utils.BotConfiguration
import net.mamoe.mirai.utils.SimpleLogger
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaTable
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.VarArgFunction

/**
 * ClassName: BotBuilderFunction
 * Description:
 * date: 2022/1/10 23:14
 * @author ooooonly
 * @version
 */
class BotBuilderFunction(
    private val coroutineScope: CoroutineScope,
    private val valueMapper: ValueMapper,
    private val onCreateBot: (Bot) -> Unit
) : VarArgFunction() {
    override fun onInvoke(args: Varargs?): Varargs {
        args ?: return NIL
        val account = args[0].checklong()
        val pwd = args[1].checkjstring()
        val config = if (args.narg() >= 3) {
            if (args[2].istable())
                args[2].checktable().toBotConfiguration().setScope()
            else
                BotConfiguration.Default.copy().setDeviceFile(args[2].checkjstring()).setScope()
        } else {
            BotConfiguration.Default.setScope()
        }
        val bot = BotFactory.newBot(account, pwd, config)
        onCreateBot(bot)
        return valueMapper.mapToLuaValue(bot)
    }

    private fun BotConfiguration.setDeviceFile(fileName: String) = apply {
        fileBasedDeviceInfo(fileName)
    }

    private fun BotConfiguration.setScope() = apply {
        parentCoroutineContext = coroutineScope.coroutineContext
    }

    private fun LuaTable.toBotConfiguration(): BotConfiguration {
        val configuration = BotConfiguration.Default.copy()
        valueMapper.provideScope {
            configuration.apply {
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
        }
        return configuration
    }

    private fun String.asMiraiProtocol(): BotConfiguration.MiraiProtocol = when (this) {
        "ANDROID_PHONE" -> BotConfiguration.MiraiProtocol.ANDROID_PHONE
        "ANDROID_PAD" -> BotConfiguration.MiraiProtocol.ANDROID_PAD
        "ANDROID_WATCH" -> BotConfiguration.MiraiProtocol.ANDROID_WATCH
        else -> throw LuaError("No such protocol named $this.")
    }
}