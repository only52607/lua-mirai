package com.github.only52607.luamirai.lua.mirai.bot

import com.github.only52607.luakt.ValueMapper
import com.github.only52607.luakt.dsl.*
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
    valueMapper: ValueMapper,
    private val onCreateBot: (Bot) -> Unit
) : VarArgFunction(), ValueMapper by valueMapper {
    override fun onInvoke(args: Varargs?): Varargs {
        args ?: return NIL
        val account = args.arg(1).checklong()
        val pwd = args.arg(2).checkjstring()
        val config = if (args.narg() >= 3) {
            if (args.arg(3).istable())
                args.arg(3).checktable().toBotConfiguration().setScope()
            else
                BotConfiguration.Default.copy().setDeviceFile(args.arg(3).checkjstring()).setScope()
        } else {
            BotConfiguration.Default.setScope()
        }
        val bot = BotFactory.newBot(account, pwd, config)
        onCreateBot(bot)
        return mapToLuaValue(bot)
    }

    private fun BotConfiguration.setDeviceFile(fileName: String) = apply {
        fileBasedDeviceInfo(fileName)
    }

    private fun BotConfiguration.setScope() = apply {
        parentCoroutineContext = coroutineScope.coroutineContext
    }

    private fun LuaTable.toBotConfiguration(): BotConfiguration {
        val configuration = BotConfiguration.Default.copy()
        configuration.apply {
            protocol = this@toBotConfiguration["protocol"].stringValueOrNull?.asMiraiProtocol() ?: protocol
            heartbeatPeriodMillis =
                this@toBotConfiguration["heartbeatPeriodMillis"].longValueOrNull ?: heartbeatPeriodMillis
            heartbeatTimeoutMillis =
                this@toBotConfiguration["heartbeatTimeoutMillis"].longValueOrNull ?: heartbeatTimeoutMillis
            reconnectionRetryTimes =
                this@toBotConfiguration["reconnectionRetryTimes"].intValueOrNull ?: reconnectionRetryTimes
            get("fileBasedDeviceInfo")?.stringValueOrNull?.let { fileBasedDeviceInfo(it) }
            get("noNetworkLog")?.booleanValueOrNull?.takeIf { it }?.let { noNetworkLog() }
            get("noBotLog")?.booleanValueOrNull?.takeIf { it }?.let { noBotLog() }
            get("botLogger")?.functionValueOrNull?.let { luaFunc ->
                botLoggerSupplier = {
                    SimpleLogger { message, _ ->
                        luaFunc.call(message)
                    }
                }
            }
            get("networkLogger")?.functionValueOrNull?.let { luaFunc ->
                networkLoggerSupplier = {
                    SimpleLogger { message, _ ->
                        luaFunc.call(message)
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