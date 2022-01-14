package com.github.only52607.luamirai.console

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.util.ConsoleExperimentalApi

/**
 * ClassName: LuaMiraiPluginDescription
 * Description:
 * date: 2022/1/14 21:31
 * @author ooooonly
 * @version
 */
@ConsoleExperimentalApi
val LuaMiraiPluginDescription by lazy {
    try {
        JvmPluginDescription.loadFromResource()
    } catch (e: Exception) {
        JvmPluginDescription(
            id = "com.github.only52607.luamirai",
            name = "lua-mirai",
            version = "0.0"
        )
    }
}