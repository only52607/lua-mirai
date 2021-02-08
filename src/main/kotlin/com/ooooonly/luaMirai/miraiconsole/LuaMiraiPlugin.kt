package com.ooooonly.luaMirai.miraiconsole

import com.ooooonly.luaMirai.lua.LuaMiraiBotScriptManager
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.utils.MiraiExperimentalApi
import net.mamoe.mirai.utils.info

@Suppress("unused")
@ConsoleExperimentalApi
@MiraiExperimentalApi
object LuaMiraiPlugin : KotlinPlugin(
    JvmPluginDescription.loadFromResource()
) {
    private val manager = LuaMiraiBotScriptManager

    override fun onEnable() {
        logger.info { "lua-mirai 加载成功，当前版本：${description.version}" }
        LuaMiraiCommand(manager, logger).register()
    }

    override fun onDisable() {
        manager.stopAll()
    }
}