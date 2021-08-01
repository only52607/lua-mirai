package com.ooooonly.luaMirai.commander

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.options.required
import com.github.ajalt.clikt.parameters.types.file
import com.ooooonly.luaMirai.lua.LuaMiraiScript
import java.io.File

/**
 * ClassName: Executor
 * Description:
 * date: 2021/8/1 13:56
 * @author ooooonly
 * @version
 */
class Executor : CliktCommand(help = "运行脚本", name = "exec") {
    val file by argument(help = "文件路径").file(mustExist = true, mustBeReadable = true, canBeDir = false)

    override fun run() {
        val script = LuaMiraiScript(sourceFile = file)
        script.create()
        script.load()
    }
}