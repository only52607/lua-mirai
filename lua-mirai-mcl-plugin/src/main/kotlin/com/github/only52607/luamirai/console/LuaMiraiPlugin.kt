package com.github.only52607.luamirai.console

import com.github.only52607.luamirai.core.manager.BotScriptManager
import kotlinx.coroutines.launch
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.utils.MiraiExperimentalApi
import net.mamoe.mirai.utils.MiraiInternalApi
import net.mamoe.mirai.utils.info

@Suppress("unused")
@OptIn(
    ConsoleExperimentalApi::class,
    MiraiInternalApi::class,
    MiraiExperimentalApi::class
)
object LuaMiraiPlugin : KotlinPlugin(LuaMiraiPluginDescription) {
    init {
        Class.forName("com.github.only52607.luamirai.lua.LuaMiraiScriptBuilder")
    }

    private val manager: BotScriptManager by lazy {
        BotScriptManager()
    }

    @MiraiExperimentalApi
    private val command: LuaMiraiCommand by lazy {
        LuaMiraiCommand(manager, logger, resolveConfigFile(scriptConfigFile))
    }

    private const val scriptConfigFile = "scripts.json"

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
            manager.removeAll()
        }
        logger.info { "lua-mirai 已停用" }
    }
}