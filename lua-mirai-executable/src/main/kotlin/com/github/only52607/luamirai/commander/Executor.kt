package com.github.only52607.luamirai.commander

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.file
import com.github.only52607.luamirai.core.factory.BotScriptBuilderRegistry
import com.github.only52607.luamirai.core.factory.buildBotScript
import com.github.only52607.luamirai.core.script.BotScriptSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * ClassName: Executor
 * Description:
 * date: 2021/8/1 13:56
 * @author ooooonly
 * @version
 */
class Executor : CliktCommand(help = "运行脚本", name = "exec") {
    private val file by argument(help = "文件路径").file(mustExist = true, mustBeReadable = true, canBeDir = false)

    override fun run() {
        println("----------Start running script----------")
        runBlocking {
            val script = BotScriptSource.FileSource(file, "lua").buildBotScript()
            println(script)
            script.start()
        }
        println("----------Script exited----------")
    }
}