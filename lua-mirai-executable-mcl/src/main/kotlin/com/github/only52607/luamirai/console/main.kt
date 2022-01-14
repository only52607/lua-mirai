package com.github.only52607.luamirai.console

import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.enable
import net.mamoe.mirai.console.plugin.PluginManager.INSTANCE.load
import net.mamoe.mirai.console.terminal.MiraiConsoleTerminalLoader
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.utils.MiraiExperimentalApi

/**
 * ClassName: main
 * Description:
 * date: 2022/1/13 22:21
 * @author ooooonly
 * @version
 */

@ConsoleExperimentalApi
@MiraiExperimentalApi
fun Array<String>.main() {
    Class.forName("com.github.only52607.luamirai.lua.LuaMiraiScriptBuilder")
    MiraiConsoleTerminalLoader.startAsDaemon()
    LuaMiraiPlugin.load()
    LuaMiraiPlugin.enable()
}