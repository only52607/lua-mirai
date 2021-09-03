package com.ooooonly.luaMirai.lua.lib

import kotlinx.serialization.json.*
import org.luaj.vm2.*

open class KtxJsonLib : JsonLib() {

    private val json = Json

    override fun LuaTable.asJsonString() = asJsonElement().toString()

    override fun String.asJsonLuaValue() = json.parseToJsonElement(this).asLuaValue()

    // JsonElement -> LuaValue

    private fun JsonElement.asLuaValue(): LuaValue = when(this) {
        is JsonPrimitive -> this.asPrimitiveLuaValue()
        is JsonObject -> this.asLuaTable()
        is JsonArray -> this.asLuaTable()
    }

    private fun JsonPrimitive.asPrimitiveLuaValue(): LuaValue {
        if (this is JsonNull) {
            return LuaValue.NIL
        }
        if (isString) {
            return LuaValue.valueOf(content)
        }
        kotlin.runCatching {
            return@asPrimitiveLuaValue LuaValue.valueOf(boolean)
        }
        kotlin.runCatching {
            return@asPrimitiveLuaValue LuaValue.valueOf(int)
        }
        kotlin.runCatching {
            return@asPrimitiveLuaValue LuaValue.valueOf(double)
        }
        return LuaValue.valueOf(content)
    }

    private fun JsonObject.asLuaTable(): LuaTable = LuaTable().apply {
        this@asLuaTable.forEach { (key, value) ->
            set(key, value.asLuaValue())
        }
    }

    private fun JsonArray.asLuaTable(): LuaTable = LuaTable.listOf(
        mutableListOf<LuaValue>().also { result ->
            this@asLuaTable.forEach { ele ->
                ele.asLuaValue().let(result::add)
            }
        }.toTypedArray()
    )

    // LuaValue -> JsonElement

    private fun LuaValue.asJsonElement(): JsonElement = when {
        isnil() -> JsonPrimitive(null as String?)
        isboolean() -> JsonPrimitive(checkboolean())
        isint() -> JsonPrimitive(checkint())
        islong() -> JsonPrimitive(checklong())
        isnumber() -> JsonPrimitive(checkdouble())
        isstring() -> JsonPrimitive(checkjstring())
        istable() -> checktable().asJsonArrayOrJsonObject()
        else -> JsonPrimitive(null as String?)
    }

    private fun LuaTable.asJsonArrayOrJsonObject(): JsonElement = if (this.length() != 0) {
        buildJsonArray {
            for (i in 1..this@asJsonArrayOrJsonObject.length()) {
                this@buildJsonArray.add(this@asJsonArrayOrJsonObject.get(i).asJsonElement())
            }
        }
    } else {
        buildJsonObject {
            this@asJsonArrayOrJsonObject.keys().forEach {
                this@buildJsonObject.put(it.tojstring(), this@asJsonArrayOrJsonObject.get(it).asJsonElement())
            }
        }
    }
}