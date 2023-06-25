package com.github.only52607.luamirai.core

/**
 * ClassName: BotScriptHeader
 * Description:
 * date: 2022/1/13 14:28
 * @author ooooonly
 * @version
 */
interface ScriptConfiguration {
    fun getString(key: String): String {
        return getStringOrNull(key) ?: throw MissingConfigurationException(key)
    }

    fun getStringOrNull(key: String): String?
}

class MissingConfigurationException(key: String): Exception("Missing configuration '$key'")