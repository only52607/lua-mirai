package com.ooooonly.luaMirai.miraiconsole

import com.ooooonly.luaMirai.lua.BotScript
import com.ooooonly.luaMirai.lua.BotScriptManager
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.ConsoleCommandSender
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.utils.MiraiExperimentalApi
import net.mamoe.mirai.utils.MiraiLogger


@Suppress("unused")
@MiraiExperimentalApi
@ConsoleExperimentalApi
class LuaMiraiCommand(private val manager: BotScriptManager, private val logger: MiraiLogger) : CompositeCommand(
    LuaMiraiPlugin, "lua", // "lua" 指令前缀
    description = "lua mirai 指令集"
) {
/*
      [指令说明]

     全部指令需在控制台下输入

     列出所有已加载脚本（包括已执行的和未执行的） "/lua list"
     载入一个脚本，但不执行   "/lua add <脚本路径>"
     载入一个脚本，并且执行 "/lua load <脚本路径>"
     执行一个已经载入的脚本（如果已经被执行过，则忽略） "/lua execute <脚本编号>"
     重新执行一个脚本（该操作会先停用脚本并重新从文件中读取内容并执行） "/lua reload <脚本编号>"
     停用一个脚本（该操作会停止脚本内所有的事件监听器）"/lua stop <脚本编号>"
     删除一个脚本（仅移出脚本列表，不删除文件） "/lua delete <脚本编号>"

*/

    @SubCommand
    fun ConsoleCommandSender.list() {
        val list = manager.list()
        logger.info("已加载${list.size}个脚本：")
        list.forEachIndexed { i: Int, botScript: BotScript ->
            botScript.getInfo()?.let { info ->
                logger.info("[$i] ${info.name}")
            }
        }
    }

    @SubCommand
    fun ConsoleCommandSender.add(fileName: String) {
        manager.add(fileName)
        logger.info("添加脚本成功")
    }

    @SubCommand
    fun ConsoleCommandSender.load(fileName: String) {
        manager.load(fileName)
        logger.info("加载脚本成功")
    }

    @SubCommand
    fun ConsoleCommandSender.execute(scriptId: Int) {
        manager.execute(scriptId)
        logger.info("执行脚本成功")
    }

    @SubCommand
    fun ConsoleCommandSender.reload(scriptId: Int) {
        manager.reload(scriptId)
        logger.info("重载脚本成功")
    }

    @SubCommand
    fun ConsoleCommandSender.stop(scriptId: Int) {
        manager.stop(scriptId)
        logger.info("停用脚本成功")
    }

    @SubCommand
    fun ConsoleCommandSender.delete(scriptId: Int) {
        manager.delete(scriptId)
        logger.info("删除脚本成功")
    }
}