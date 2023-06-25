package com.github.only52607.luamirai.console

/**
 * ClassName: PluginConfig
 * Description:
 * date: 2022/5/21 12:12
 * @author ooooonly
 * @version
 */
@kotlinx.serialization.Serializable
data class PluginConfig(
    val sources: List<ScriptSource>
) {
    @kotlinx.serialization.Serializable
    data class ScriptSource(
        val type: String,
        val value: String,
        val lang: String,
        var alias: String? = null,
        var autoStart: Boolean
    )
}