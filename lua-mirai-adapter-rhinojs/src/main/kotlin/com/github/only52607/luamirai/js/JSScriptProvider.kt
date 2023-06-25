package com.github.only52607.luamirai.js

import com.github.only52607.luamirai.core.Script
import com.github.only52607.luamirai.core.ScriptConfiguration
import com.github.only52607.luamirai.core.ScriptProvider
import com.github.only52607.luamirai.core.ScriptSource
import com.github.only52607.luamirai.core.configuation.JsonScriptConfiguration
import com.github.only52607.luamirai.core.configuation.MapScriptConfiguration
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JSScriptProvider: ScriptProvider {
    override fun supportSource(source: ScriptSource): Boolean {
        return source.lang.lowercase() == "js" || source.lang.lowercase() == "javascript"
    }

    private fun readConfiguration(
        source: ScriptSource
    ): ScriptConfiguration {
        val manifest = source.resourceFinder?.findResource("manifest.json")
        return if (manifest != null) {
            JsonScriptConfiguration.parse(manifest)
        } else {
            MapScriptConfiguration(emptyMap())
        }
    }

    override suspend fun load(source: ScriptSource): Script {
        return withContext(Dispatchers.IO) {
            JsMiraiScript(source, readConfiguration(source))
        }
    }
}