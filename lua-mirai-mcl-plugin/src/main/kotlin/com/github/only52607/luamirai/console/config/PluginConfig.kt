package com.github.only52607.luamirai.console.config

import com.github.only52607.luamirai.configuration.ConfigurableScriptSource

/**
 * ClassName: PluginConfig
 * Description:
 * date: 2022/5/21 12:12
 * @author ooooonly
 * @version
 */
@kotlinx.serialization.Serializable
class PluginConfig(
    val sources: List<ConfigurableScriptSource>
)