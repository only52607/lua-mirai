package com.github.only52607.luamirai.lua

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
import java.io.InputStream

/**
 * ClassName: LuaMiraiScriptBuilder
 * Description:
 * date: 2022/1/13 21:37
 * @author ooooonly
 * @version
 */
@Suppress("unused")
class LuaMiraiScriptBuilder(
    val source: BotScriptSource
) : BotScriptBuilder(source) {
    companion object {
        const val LANG = "lua"
        const val MAX_SCRIPT_HEADER = 8192

        init {
            register(LANG, ::LuaMiraiScriptBuilder)
        }
    }

    override val lang: String
        get() = LANG

    private suspend fun readHeaderFromCodeInputStream(inputStream: InputStream): BotScriptHeader {
        return withContext(Dispatchers.IO) {
            LuaHeaderReader.readHeader(inputStream)
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

    override suspend fun buildInstance(): BotScript {
        return withContext(Dispatchers.IO) {
            LuaMiraiScript(source, readHeader(), botScriptSource.mainInputStream)
        }
    }

    override suspend fun readHeader(): BotScriptHeader {
        val manifestInputStream = source.resourceFinder?.findResource("manifest.json")
        return if (manifestInputStream != null) {
            readHeaderFromJsonManifest(String(manifestInputStream.readBytes()))
        } else {
            readHeaderFromCodeInputStream(botScriptSource.mainInputStream)
        }
    }
}