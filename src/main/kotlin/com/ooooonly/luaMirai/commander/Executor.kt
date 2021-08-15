package com.ooooonly.luaMirai.commander

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.file
import com.ooooonly.luaMirai.lua.LuaSource
import com.ooooonly.luaMirai.lua.LuaMiraiScript
import net.mamoe.mirai.utils.MiraiInternalApi

/**
 * ClassName: Executor
 * Description:
 * date: 2021/8/1 13:56
 * @author ooooonly
 * @version
 */
class Executor : CliktCommand(help = "运行脚本", name = "exec") {
    private val file by argument(help = "文件路径").file(mustExist = true, mustBeReadable = true, canBeDir = false)

    @MiraiInternalApi
    override fun run() {
        val script = LuaMiraiScript(LuaSource.LuaFileSource(file))
        script.create()
        script.load()
    }
}