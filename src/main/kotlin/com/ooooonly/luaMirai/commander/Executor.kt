package com.ooooonly.luaMirai.commander

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.file
import com.ooooonly.luaMirai.base.BotScriptFactory
import com.ooooonly.luaMirai.base.ScriptLang
import com.ooooonly.luaMirai.lua.LuaSource
import com.ooooonly.luaMirai.lua.LuaMiraiScript
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

    override fun run() {
        GlobalScope.launch {
            val script = BotScriptFactory.buildBotScript(ScriptLang.Lua, file = file)
            script.load()
        }
    }
}