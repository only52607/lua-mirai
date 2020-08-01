package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.utils.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.runBlocking
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.ZeroArgFunction

open class LuaCoroutineJob(var job: Job) : LuaValue() {

    override fun getmetatable(): LuaValue = LuaTable().applyIndex {
        setFunction1ArgNoReturn("join") {
            runBlocking {
                it.checkIfType<LuaCoroutineJob>().job.join()
            }
        }

        setFunction1ArgNoReturn("cancel") {
            it.checkIfType<LuaCoroutineJob>().job.cancel()
        }
    }

    override fun typename(): String = "CoroutineJob"

    override fun type(): Int = 0
}