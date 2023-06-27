package com.github.only52607.luamirai.lua

import com.github.only52607.luamirai.core.Script
import com.github.only52607.luamirai.core.ScriptConfiguration
import com.github.only52607.luamirai.core.ScriptProvider
import com.github.only52607.luamirai.core.ScriptSource
import com.github.only52607.luamirai.core.configuation.JsonScriptConfiguration
import com.google.auto.service.AutoService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@AutoService(ScriptProvider::class)
class LuaScriptProvider: ScriptProvider {
    override fun supportSource(source: ScriptSource): Boolean {
        return source.lang.lowercase() == "lua"
    }

    private suspend fun readConfiguration(
        source: ScriptSource
    ): ScriptConfiguration {
        val manifest = source.resourceFinder?.findResource("manifest.json")
        return if (manifest != null) {
            JsonScriptConfiguration.parse(manifest)
        } else {
            withContext(Dispatchers.IO) {
                BundledLuaScriptConfiguration.from(source.main)
            }
        }
    }

    override suspend fun load(source: ScriptSource): Script {
        return LuaScript(source, readConfiguration(source))
    }
}