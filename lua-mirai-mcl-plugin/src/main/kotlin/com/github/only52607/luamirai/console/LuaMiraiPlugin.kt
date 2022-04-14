package com.github.only52607.luamirai.console

import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.utils.info

@Suppress("unused")
@OptIn(
    ConsoleExperimentalApi::class
)
object LuaMiraiPlugin : KotlinPlugin(LuaMiraiPluginDescription) {
    init {
        Class.forName("com.github.only52607.luamirai.lua.LuaMiraiScriptBuilder")
    }

    private const val scriptConfigFile = "scripts.json"

    private val command: LuaMiraiCommand by lazy {
        LuaMiraiCommand(logger, resolveConfigFile(scriptConfigFile))
    }

    override fun onEnable() {
        command.enable()
        command.register()
        logger.info { "lua-mirai 加载成功，当前版本：${description.version}" }
    }

    override fun onDisable() {
        command.disable()
        logger.info { "lua-mirai 已停用" }
    }
}