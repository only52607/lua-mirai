package com.ooooonly.luaMirai.miraiconsole

import com.ooooonly.luaMirai.lua.LuaMiraiBotScriptManager
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.utils.info

@Suppress("unused")
@ConsoleExperimentalApi
object LuaMiraiPlugin : KotlinPlugin(
//    JvmPluginDescription.loadFromResource()
    JvmPluginDescription(
        id = "com.ooooonly.luaMirai",
        name = "lua-mirai",
        version = "2.0.8"
    )
) {
    private lateinit var manager: LuaMiraiBotScriptManager
    private const val scriptConfigFile = "scripts.json"

    override fun onEnable() {
        logger.info { "lua-mirai 加载成功，当前版本：${description.version}" }
        manager = LuaMiraiBotScriptManager(resolveConfigFile(scriptConfigFile))
        LuaMiraiCommand(manager, logger).register()
    }

    override fun onDisable() {
        logger.info { "lua-mirai 已被停用" }
        manager.stopAll()
    }
}