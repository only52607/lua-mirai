package com.github.only52607.luamirai.lua.lib

import com.github.only52607.luakt.ValueMapper
import com.github.only52607.luakt.utils.provideScope
import org.luaj.vm2.LuaFunction
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
) : TwoArgFunction(), ScheduledExecutorService by scheduledExecutorService {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        val globals = env?.checkglobals()
        valueMapper.provideScope {
            globals?.edit {
                listOf("thread", "execute", "launch") nto luaFunctionOf { func: LuaFunction ->
                    return@luaFunctionOf scheduledExecutorService.execute {
                        func.call()
                    }
                }
                "schedule" to luaFunctionOf { func: LuaFunction, delay: Long ->
                    return@luaFunctionOf scheduledExecutorService.schedule(
                        { func.call() },
                        delay,
                        TimeUnit.MICROSECONDS
                    )
                }
                "scheduleAtFixedRate" to luaFunctionOf { func: LuaFunction, initialDelay: Long, period: Long ->
                    return@luaFunctionOf scheduledExecutorService.scheduleAtFixedRate(
                        { func.call() },
                        initialDelay,
                        period,
                        TimeUnit.MICROSECONDS
                    )
                }
                "scheduleWithFixedDelay" to luaFunctionOf { func: LuaFunction, initialDelay: Long, delay: Long ->
                    return@luaFunctionOf scheduledExecutorService.scheduleWithFixedDelay(
                        { func.call() },
                        initialDelay,
                        delay,
                        TimeUnit.MICROSECONDS
                    )
                }
                "sleep" to luaFunctionOf { time: Long ->
                    Thread.sleep(time)
                }
            }
        }
        return LuaValue.NIL
    }
}