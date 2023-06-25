package com.github.only52607.luamirai.lua.lib

import com.github.only52607.luakt.extension.OneArgFunction
import com.github.only52607.luakt.extension.ThreeArgFunction
import com.github.only52607.luakt.extension.TwoArgFunction
import com.github.only52607.luakt.extension.longValue
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

/**
 * ClassName: TaskLib
 * Description:
 * date: 2021/8/13 23:11
 * @author ooooonly
 * @version
 */
class TaskLib(
    private val scheduledExecutorService: ScheduledExecutorService = Executors.newScheduledThreadPool(2)
) : TwoArgFunction(), ScheduledExecutorService by scheduledExecutorService {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        val globals = env?.checkglobals()
        globals?.apply {
            this["thread"] = OneArgFunction { func: LuaValue ->
                scheduledExecutorService.execute {
                    func.call()
                }
                return@OneArgFunction NIL
            }
            this["execute"] = this["thread"]
            this["launch"] = this["thread"]
            this["schedule"] = TwoArgFunction { func: LuaValue, delay: LuaValue ->
                scheduledExecutorService.schedule(
                    { func.call() },
                    delay.longValue,
                    TimeUnit.MICROSECONDS
                )
                return@TwoArgFunction NIL
            }
            this["scheduleAtFixedRate"] = ThreeArgFunction { func: LuaValue, initialDelay: LuaValue, period: LuaValue ->
                scheduledExecutorService.scheduleAtFixedRate(
                    { func.call() },
                    initialDelay.longValue,
                    period.longValue,
                    TimeUnit.MICROSECONDS
                )
                return@ThreeArgFunction NIL
            }
            this["scheduleWithFixedDelay"] = ThreeArgFunction { func: LuaValue, initialDelay: LuaValue, delay: LuaValue ->
                scheduledExecutorService.scheduleWithFixedDelay(
                    { func.call() },
                    initialDelay.longValue,
                    delay.longValue,
                    TimeUnit.MICROSECONDS
                )
                return@ThreeArgFunction NIL
            }
            this["sleep"] = OneArgFunction { time: LuaValue ->
                Thread.sleep(time.checklong())
                return@OneArgFunction NIL
            }
        }
        return LuaValue.NIL
    }
}