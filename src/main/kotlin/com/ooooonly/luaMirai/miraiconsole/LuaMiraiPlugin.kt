package com.ooooonly.luaMirai.miraiconsole

import com.ooooonly.luaMirai.lua.LuaMiraiBotScriptManager
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
object LuaMiraiPlugin : KotlinPlugin(
    try {
        JvmPluginDescription.loadFromResource()
    } catch (e: Exception) {
        JvmPluginDescription(
            id = "com.ooooonly.luaMirai",
            name = "lua-mirai",
            version = "<读取版本号失败>"
        )
    }
) {
    private lateinit var manager: LuaMiraiBotScriptManager
    private const val scriptConfigFile = "scripts.json"

    @MiraiExperimentalApi
    override fun onEnable() {
        logger.info { "lua-mirai 加载成功，当前版本：${description.version}" }
        manager = LuaMiraiBotScriptManager(resolveConfigFile(scriptConfigFile))
        try {
            manager.loadScriptsByConfig()
        } catch (e:Exception) {
            logger.error("配置文件加载失败！")
            e.printStackTrace()
        }

        LuaMiraiCommand(manager, logger).register()
    }

    override fun onDisable() {
        logger.info { "lua-mirai 已被停用" }
        manager.stopAll()
    }
}