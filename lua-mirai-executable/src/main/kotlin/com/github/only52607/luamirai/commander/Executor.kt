package com.github.only52607.luamirai.commander

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.file
import com.github.only52607.luamirai.core.load
import com.github.only52607.luamirai.core.source.LMPKSource
import com.github.only52607.luamirai.core.source.TextFileSource
import kotlinx.coroutines.runBlocking

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
            val source = when {
                file.isDirectory -> LMPKSource.fromDirectory(file)
                file.name.endsWith(".zip") || file.name.endsWith(".lmpk") -> LMPKSource.fromZipFile(file)
                else -> TextFileSource(file)
            }
            val script = source.load()
            println(script)
            script.start().join()
        }
    }
}