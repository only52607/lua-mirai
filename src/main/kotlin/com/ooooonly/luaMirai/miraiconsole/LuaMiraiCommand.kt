package com.ooooonly.luaMirai.miraiconsole

import com.ooooonly.luaMirai.BotScript
import com.ooooonly.luaMirai.lua.LuaMiraiBotScriptManager
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.ConsoleCommandSender
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.utils.MiraiExperimentalApi
import net.mamoe.mirai.utils.MiraiInternalApi
import net.mamoe.mirai.utils.MiraiLogger
import java.io.File


@Suppress("unused")
@MiraiExperimentalApi
@ConsoleExperimentalApi
@MiraiInternalApi
class LuaMiraiCommand(private val manager: LuaMiraiBotScriptManager, private val logger: MiraiLogger) :
    CompositeCommand(
        owner = LuaMiraiPlugin,
        primaryName = "lua",
        description = "lua mirai 指令集"
    ) {

    @SubCommand
    @Description("打开lua mirai开发文档")
    fun ConsoleCommandSender.doc() {
        val website = "https://ooooonly.gitee.io/lua-mirai-doc/#/"
        kotlin.runCatching {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler $website")
        }
        logger.info(website)
    }

    @SubCommand
    @Description("以 '[脚本编号] 文件名' 的形式列出所有已加载脚本")
    fun ConsoleCommandSender.list() {
        val list = manager.list()
        logger.info("已加载${list.size}个脚本：")
        list.forEachIndexed { i: Int, botScript: BotScript ->
            botScript.info?.let { info ->
                logger.info("[$i]\t${info.name}\t${if (botScript.isLoaded) "已启用" else "未启用"}")
            }
        }
    }

    @SubCommand
    @Description("载入一个脚本，但不执行")
    fun ConsoleCommandSender.add(@Name("文件名") fileName: String) {
        try {
            manager.add(File(fileName))
            logger.info("添加脚本[$fileName]成功")
            manager.updateConfig()
        } catch (e: Exception) {
            logger.error(e.message)
        }
    }

    @SubCommand
    @Description("载入一个脚本，并且执行")
    fun ConsoleCommandSender.load(@Name("文件名") fileName: String) {
        try {
            manager.load(File(fileName))
            logger.info("加载脚本[$fileName]成功")
            manager.updateConfig()
        } catch (e: Exception) {
            logger.error(e.message)
        }
    }

    @SubCommand
    @Description("执行一个已经载入的脚本（如果已经被执行过，则忽略）")
    fun ConsoleCommandSender.execute(@Name("脚本编号") scriptId: Int) {
        try {
            manager.execute(scriptId)
            logger.info("执行脚本[$scriptId]成功")
            manager.updateConfig()
        } catch (e: Exception) {
            logger.error(e.message)
        }
    }

    @SubCommand
    @Description("重新执行一个脚本（该操作会先停用脚本并重新从文件中读取内容并执行）")
    fun ConsoleCommandSender.reload(@Name("脚本编号") scriptId: Int) {
        try {
            manager.reload(scriptId)
            logger.info("重载脚本[$scriptId]成功")
            manager.updateConfig()
        } catch (e: Exception) {
            logger.error(e.message)
        }
    }

    @SubCommand
    @Description("停用一个脚本（该操作会停止脚本以及脚本内注册的所有事件监听器）")
    fun ConsoleCommandSender.stop(@Name("脚本编号") scriptId: Int) {
        try {
            manager.stop(scriptId)
            logger.info("停用脚本[$scriptId]成功")
            manager.updateConfig()
        } catch (e: Exception) {
            logger.error(e.message)
        }
    }

    @SubCommand
    @Description("删除一个脚本（仅移出脚本列表，不删除文件）")
    fun ConsoleCommandSender.delete(@Name("脚本编号") scriptId: Int) {
        try {
            manager.delete(scriptId)
            logger.info("删除脚本[$scriptId]成功")
            manager.updateConfig()
        } catch (e: Exception) {
            logger.error(e.message)
        }
    }
}