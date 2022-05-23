package com.github.only52607.luamirai.configuration

import com.github.only52607.luamirai.core.script.BotScriptSource
import kotlinx.serialization.Serializable

/**
 * ClassName: ConfigurableScriptSource
 * Description:
 * date: 2022/5/21 10:42
 * @author ooooonly
 * @version
 */
@Serializable(ConfigurableScriptSourceSerializer::class)
class ConfigurableScriptSource(
    source: BotScriptSource,
    var alias: String? = null,
    var autoStart: Boolean = false
) : BotScriptSource.Wrapper(source)