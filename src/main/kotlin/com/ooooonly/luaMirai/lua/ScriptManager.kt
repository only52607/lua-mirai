package com.ooooonly.luaMirai.lua

import kotlinx.coroutines.*
import net.mamoe.mirai.Bot
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaClosure
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaFunction
import java.io.File
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

object ScriptManager : CoroutineScope {
    private val scripts = mutableListOf<LuaScript>()
    override val coroutineContext: CoroutineContext = EmptyCoroutineContext
    private const val RUNTIME_LIMIT = 2000L

    fun listScript(): List<LuaScript> = scripts

    private suspend fun loadScript(file: File): LuaScript {
        val globals = MiraiCoreGlobals()
        val luaScript = LuaScript(file, globals)
        try {
            (globals.loadfile(file.absolutePath) as LuaClosure).callSuspend(RUNTIME_LIMIT)
        } catch (e: Exception) {
            invalidateGlobals(globals)
            throw e
        }
        BotReceiverManager.addReceiver(globals)
        return luaScript
    }

    private fun invalidateGlobals(globals: MiraiCoreGlobals) {
        BotReceiverManager.removeReceiver(globals)
        globals.destroy()
    }

    suspend fun addScript(file: File): Int {
        val luaScript = loadScript(file)
        scripts.add(luaScript)
        return scripts.size - 1
    }

    suspend fun reloadScript(index: Int) {
        val orignScript = scripts[index]
        invalidateGlobals(orignScript.globals)
        scripts[index] = loadScript(orignScript.file)
    }

    fun removeScript(index: Int) {
        invalidateGlobals(scripts[index].globals)
        scripts.removeAt(index)
    }

    fun loadBot(bot: Bot) {
        BotReceiverManager.noticeReceivers(bot)
    }

    private suspend fun LuaClosure.callSuspend(timeLimit: Long = Long.MAX_VALUE) {
        withTimeout(timeLimit) {
            launch {
                withContext(Dispatchers.IO) {
                    call()
                }
            }
        }
    }
}

class LuaScript(var file: File, var globals: MiraiCoreGlobals)