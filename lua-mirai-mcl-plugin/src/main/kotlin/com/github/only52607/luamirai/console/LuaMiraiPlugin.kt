package com.github.only52607.luamirai.console

import com.google.auto.service.AutoService
import net.mamoe.mirai.console.command.CommandManager.INSTANCE.register
import net.mamoe.mirai.console.plugin.jvm.JvmPlugin
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info

@Suppress("unused")
@AutoService(JvmPlugin::class)
object LuaMiraiPlugin : KotlinPlugin(JvmPluginDescription.loadFromResource()) {

    private const val FILE_CONFIG = "lua-mirai.json"

    private val command: LuaMiraiCommand by lazy {
        LuaMiraiCommand(logger, resolveConfigFile(FILE_CONFIG))
    }

    override fun onEnable() {
        command.enable()
        command.register()
        logger.info { "lua-mirai 加载完毕，当前版本：${description.version}" }
    }

    override fun onDisable() {
        command.disable()
        logger.info { "lua-mirai 已停用" }
    }
}