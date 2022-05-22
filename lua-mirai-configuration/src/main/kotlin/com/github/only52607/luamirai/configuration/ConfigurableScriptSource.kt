package com.github.only52607.luamirai.configuration

import com.github.only52607.luamirai.core.script.BotScriptSource
import kotlinx.serialization.Serializable
import java.io.InputStream

/**
 * ClassName: ConfigurableScriptSource
 * Description:
 * date: 2022/5/21 10:42
 * @author ooooonly
 * @version
 */
@Serializable(ConfigurableScriptSourceSerializer::class)
class ConfigurableScriptSource(
    val source: BotScriptSource,
    var alias: String? = null,
    var autoStart: Boolean = false
) : BotScriptSource(
    lang = source.lang,
    name = source.name,
    size = source.size,
    charset = source.charset,
) {
    override val mainInputStream: InputStream
        get() = source.mainInputStream

    override fun toString(): String {
        return source.toString()
    }
}