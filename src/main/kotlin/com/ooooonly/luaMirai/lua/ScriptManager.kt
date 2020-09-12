package com.ooooonly.luaMirai.lua

import com.ooooonly.luakt.getOrNull
import kotlinx.coroutines.*
import net.mamoe.mirai.Bot
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaClosure
import org.luaj.vm2.LuaTable
import java.io.File
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

object ScriptManager : CoroutineScope {
    private val scripts = mutableListOf<LuaScript>()
    override val coroutineContext: CoroutineContext = EmptyCoroutineContext
    private const val RUNTIME_LIMIT = 2000L //限时暂时不起作用

    fun listScript(): List<LuaScript> = scripts

    private suspend fun loadScript(file: File): LuaScript {
        val globals = MiraiCoreGlobals()
        try {
            (globals.loadfile(file.absolutePath) as LuaClosure).callSuspend(RUNTIME_LIMIT)
        } catch (e: Exception) {
            invalidateGlobals(globals)
            throw e
        }
        BotReceiverManager.addReceiver(globals)
        return LuaScript(file, globals)
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
                    call() // 可能会无法结束
                }
            }
        }
    }
}

class LuaScript(
    var file: File,
    var globals: MiraiCoreGlobals,
    val info: ScriptInfo = ScriptInfo.fromFileAndGlobal(file, globals)
)

data class ScriptInfo(
    val name: String = "",
    val version: String = "",
    val author: String = "",
    val description: String = "",
    val usage: String = "",
    val file: String = ""
) {
    companion object {
        private const val defaultVersion = "1.0"
        private const val defaultAuthor = "lua-mirai"
        private const val defaultDescription = "无说明"
        private const val defaultUsage = "无"
        fun fromFileAndGlobal(file: File, globals: Globals): ScriptInfo {
            val info = globals.getOrNull("Info") ?: return ScriptInfo(
                file.name, defaultVersion, defaultAuthor, defaultDescription,
                defaultUsage, file.name
            )
            val infoTable = info as LuaTable
            val name = infoTable.getOrNull("name")?.toString() ?: file.name
            val version = infoTable.getOrNull("version")?.toString() ?: defaultVersion
            val author = infoTable.getOrNull("author")?.toString() ?: defaultAuthor
            val description = infoTable.getOrNull("description")?.toString() ?: defaultDescription
            val usage = infoTable.getOrNull("usage")?.toString() ?: defaultUsage
            return ScriptInfo(name, version, author, description, usage, file.name)
        }
    }
}