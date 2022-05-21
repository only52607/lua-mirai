package com.github.only52607.luamirai.configuration

import com.github.only52607.luamirai.core.script.BotScriptSource
import kotlinx.serialization.Serializable
import java.io.InputStream
import java.nio.charset.Charset

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
    override val inputStream: InputStream
        get() = source.inputStream

    override fun toString(): String {
        return source.toString()
    }
}