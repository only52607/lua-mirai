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
    val sources: List<ScriptSource> = mutableListOf()
) {
    companion object {
        const val SOURCE_TYPE_FILE = "file"
        const val SOURCE_TYPE_LMPK = "lmpk"
        const val SOURCE_TYPE_DIRECTORY = "directory"
    }

    @kotlinx.serialization.Serializable
    data class ScriptSource(
        val type: String,
        val value: String,
        val lang: String? = null,
        var alias: String? = null,
        var autoStart: Boolean = true
    )
}