package com.github.only52607.luamirai.js

import com.github.only52607.luamirai.core.BotScriptBuilder
import com.github.only52607.luamirai.core.script.BotScript
import com.github.only52607.luamirai.core.script.BotScriptHeader
import com.github.only52607.luamirai.core.script.BotScriptSource
import com.github.only52607.luamirai.core.script.mutableBotScriptHeaderOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * ClassName: JsMiraiScriptBuilder
 * Description:
 * date: 2022/6/14 22:33
 * @author ooooonly
 * @version
 */
class JsMiraiScriptBuilder(
    val source: BotScriptSource
) : BotScriptBuilder(source) {
    companion object {
        const val LANG = "js"

        init {
            register(LANG, ::JsMiraiScriptBuilder)
        }
    }

    override val lang: String
        get() = LANG

    override suspend fun buildInstance(): BotScript {
        return withContext(Dispatchers.IO) {
            JsMiraiScript(source, readHeader())
        }
    }

    private fun readHeaderFromJsonManifest(jsonString: String): BotScriptHeader {
        val json = Json
        val manifest: JsonObject = json.parseToJsonElement(jsonString).jsonObject
        return mutableBotScriptHeaderOf {
            manifest["header"]?.jsonObject?.forEach { key, value ->
                this@mutableBotScriptHeaderOf[key] = value.jsonPrimitive.content
            }
        }
    }

    override suspend fun readHeader(): BotScriptHeader {
        val manifestInputStream =
            source.resourceFinder?.findResource("manifest.json") ?: return mutableBotScriptHeaderOf()
        return readHeaderFromJsonManifest(String(manifestInputStream.readBytes()))
    }
}