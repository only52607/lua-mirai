package com.github.only52607.luamirai.lua.lib

import com.github.only52607.luakt.ValueMapper
import com.github.only52607.luakt.dsl.longValue
import com.github.only52607.luakt.dsl.oneArgLuaFunctionOf
import com.github.only52607.luakt.dsl.threeArgLuaFunctionOf
import com.github.only52607.luakt.dsl.twoArgLuaFunctionOf
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
    private val valueMapper: ValueMapper,
    private val scheduledExecutorService: ScheduledExecutorService = Executors.newScheduledThreadPool(2)
) : TwoArgFunction(), ScheduledExecutorService by scheduledExecutorService, ValueMapper by valueMapper {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        val globals = env?.checkglobals()
        globals?.apply {
            this["thread"] = oneArgLuaFunctionOf { func: LuaValue ->
                scheduledExecutorService.execute {
                    func.call()
                }
                return@oneArgLuaFunctionOf NIL
            }
            this["execute"] = this["thread"]
            this["launch"] = this["thread"]
            this["schedule"] = twoArgLuaFunctionOf { func: LuaValue, delay: LuaValue ->
                scheduledExecutorService.schedule(
                    { func.call() },
                    delay.longValue,
                    TimeUnit.MICROSECONDS
                )
                return@twoArgLuaFunctionOf NIL
            }
            this["scheduleAtFixedRate"] = threeArgLuaFunctionOf { func: LuaValue, initialDelay: LuaValue, period: LuaValue ->
                scheduledExecutorService.scheduleAtFixedRate(
                    { func.call() },
                    initialDelay.longValue,
                    period.longValue,
                    TimeUnit.MICROSECONDS
                )
                return@threeArgLuaFunctionOf NIL
            }
            this["scheduleWithFixedDelay"] = threeArgLuaFunctionOf { func: LuaValue, initialDelay: LuaValue, delay: LuaValue ->
                scheduledExecutorService.scheduleWithFixedDelay(
                    { func.call() },
                    initialDelay.longValue,
                    delay.longValue,
                    TimeUnit.MICROSECONDS
                )
                return@threeArgLuaFunctionOf NIL
            }
            this["sleep"] = oneArgLuaFunctionOf { time: LuaValue ->
                Thread.sleep(time.checklong())
                return@oneArgLuaFunctionOf NIL
            }
        }
        return LuaValue.NIL
    }
}