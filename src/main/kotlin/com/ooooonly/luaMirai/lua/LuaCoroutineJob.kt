package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.utils.applyIndex
import com.ooooonly.luaMirai.utils.asIndex
import com.ooooonly.luaMirai.utils.setFunction1ArgNoReturn
import kotlinx.coroutines.Job
import kotlinx.coroutines.runBlocking
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.ZeroArgFunction

class LuaCoroutineJob(var job: Job) : LuaValue() {

    override fun getmetatable(): LuaValue = LuaTable().applyIndex {
        setFunction1ArgNoReturn("join") {
            when (it) {
                is LuaCoroutineJob -> runBlocking {
                    it.job.join()
                }
                else -> throw LuaError("Object must be LuaCoroutineJob")
            }
        }

        setFunction1ArgNoReturn("cancel") {
            when (it) {
                is LuaCoroutineJob -> runBlocking {
                    it.job.cancel()
                }
                else -> throw LuaError("Object must be LuaCoroutineJob")
            }
        }
    }

    override fun typename(): String = "CoroutineJob"

    override fun type(): Int = 0
}