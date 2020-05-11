package com.ooooonly.luaMirai.lua

import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.ZeroArgFunction

class LuaCoroutineJob(var job: Job) : LuaValue() {

    override fun getmetatable(): LuaValue {
        var metatable = LuaTable()
        var index = LuaTable()
        index.set("join", object : OneArgFunction() {
            override fun call(self: LuaValue): LuaValue = LuaValue.NIL.also {
                when (self) {
                    is LuaCoroutineJob -> runBlocking {
                        self.job.join()
                    }
                    else -> throw LuaError("Object must be LuaCoroutineJob")
                }
            }
        })
        index.set("cancel", object : OneArgFunction() {
            override fun call(self: LuaValue): LuaValue = LuaValue.NIL.also {
                when (self) {
                    is LuaCoroutineJob -> runBlocking {
                        self.job.cancel()
                    }
                    else -> throw LuaError("Object must be LuaCoroutineJob")
                }
            }
        })

        metatable.set(INDEX, index)
        return metatable
    }

    override fun typename(): String = "CoroutineJob"

    override fun type(): Int = 0
}