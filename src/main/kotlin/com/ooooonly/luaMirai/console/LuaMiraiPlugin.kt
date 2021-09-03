package com.ooooonly.luaMirai.console

import com.ooooonly.luaMirai.lua.LuaMiraiBotScriptManager
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.utils.MiraiExperimentalApi
import net.mamoe.mirai.utils.MiraiInternalApi
import net.mamoe.mirai.utils.info

@Suppress("unused")
@ConsoleExperimentalApi
@MiraiInternalApi
object LuaMiraiPlugin: KotlinPlugin(
    try {
        JvmPluginDescription.loadFromResource()
    } catch (e: Exception) {
        JvmPluginDescription(
            id = "com.ooooonly.luaMirai",
            name = "lua-mirai",
            version = "0.0"
        )
    }
){
    private val manager: LuaMiraiBotScriptManager by lazy {
        LuaMiraiBotScriptManager()
    }

    @MiraiExperimentalApi
    private val command: LuaMiraiCommand by lazy {
        LuaMiraiCommand(manager, logger, resolveConfigFile(scriptConfigFile))
    }

    private const val scriptConfigFile = "scripts.json"

    @MiraiExperimentalApi
    override fun onEnable() {
        try {
            launch {
                command.loadScripts()
            }
        } catch (e: Exception) {
            logger.error("配置文件加载失败！")
            e.printStackTrace()
        }
        command.register()
        logger.info { "lua-mirai 加载成功，当前版本：${description.version}" }
    }

    override fun onDisable() {
        launch {
            manager.deleteAll()
        }
        logger.info { "lua-mirai 已停用" }
    }
}