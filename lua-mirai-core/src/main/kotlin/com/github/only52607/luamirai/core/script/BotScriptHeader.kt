package com.github.only52607.luamirai.core.script

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * ClassName: BotScriptHeader
 * Description:
 * date: 2022/1/13 14:28
 * @author ooooonly
 * @version
 */
interface BotScriptHeader : Map<String, String> {
    companion object {
        fun fromJsonManifest(content: String): BotScriptHeader {
            val json = Json
            val manifest: JsonObject = json.parseToJsonElement(content).jsonObject
            return object : BotScriptHeader, Map<String, String> by (buildMap {
                manifest.forEach { key, element ->
                    if (key == "header") {
                        element.jsonObject.forEach { headerKey, value ->
                            this@buildMap[headerKey] = value.jsonPrimitive.content
                        }
                    } else {
                        this@buildMap[key] = element.jsonPrimitive.content
                    }
                }
            }) {}
        }
    }
}

interface MutableBotScriptHeader : MutableMap<String, String>, BotScriptHeader

internal class MutableBotScriptHeaderImpl : MutableBotScriptHeader, MutableMap<String, String> by mutableMapOf()

fun mutableBotScriptHeaderOf(builder: MutableBotScriptHeader.() -> Unit = {}): MutableBotScriptHeader =
    MutableBotScriptHeaderImpl().apply(builder)