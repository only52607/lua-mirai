package com.github.only52607.luamirai.core.configuation

import com.github.only52607.luamirai.core.ScriptConfiguration
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.InputStream
import java.nio.charset.Charset

class JsonScriptConfiguration(
    private val obj: JsonObject
): ScriptConfiguration {
    companion object {
        private val json = Json
        fun parse(jsonText: String): JsonScriptConfiguration {
            return JsonScriptConfiguration(json.parseToJsonElement(jsonText).jsonObject)
        }

        fun parse(inputStream: InputStream, charset: Charset = Charsets.UTF_8): JsonScriptConfiguration {
            return parse(String(inputStream.readBytes(), charset))
        }
    }

    override fun getStringOrNull(key: String): String? {
        return obj[key]?.jsonPrimitive?.content
    }
}