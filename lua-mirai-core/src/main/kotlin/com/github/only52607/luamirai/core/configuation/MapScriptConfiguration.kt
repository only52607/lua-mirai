package com.github.only52607.luamirai.core.configuation

import com.github.only52607.luamirai.core.ScriptConfiguration

class MapScriptConfiguration(private val map: Map<String, String>): ScriptConfiguration {
    override fun getStringOrNull(key: String): String? {
        return map[key]
    }
}