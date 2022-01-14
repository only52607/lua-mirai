package com.github.only52607.luamirai.lua.lib

import com.ooooonly.luakt.mapper.ValueMapper
import org.json.JSONArray
import org.json.JSONObject
import org.luaj.vm2.*

open class OrgJsonLib(
    valueMapper: ValueMapper
) : JsonLib(valueMapper) {

    override fun LuaTable.asJsonString() = toJsonObject().toString()

    override fun String.asJsonLuaValue() = JSONObject(this).asLuaTable()

    // JsonObject -> LuaValue

    private fun JSONObject.asLuaTable(): LuaTable = LuaTable().apply {
        this@asLuaTable.keySet().forEach { key ->
            val value = this@asLuaTable.get(key)
            val luaValue = value.asLuaValue()
            luaValue?.let {
                set(key, luaValue)
            }
        }
    }

    private fun JSONArray.asLuaTable(): LuaTable = LuaTable.listOf(
        mutableListOf<LuaValue>().also { result ->
            this@asLuaTable.forEach { ele ->
                ele.asLuaValue()?.let(result::add)
            }
        }.toTypedArray()
    )

    private fun Any.asLuaValue() = when (this::class) {
        Unit::class -> LuaValue.NIL
        Short::class -> LuaValue.valueOf((this as Short).toInt())
        Int::class -> LuaValue.valueOf(this as Int)
        Long::class -> LuaValue.valueOf(this.toString()) //luaj不支持long类型
        Float::class -> LuaValue.valueOf((this as Float).toDouble())
        Double::class -> LuaValue.valueOf(this as Double)
        Char::class -> LuaValue.valueOf(this.toString())
        String::class -> LuaValue.valueOf(this as String)
        Boolean::class -> LuaValue.valueOf(this as Boolean)
        JSONObject::class -> (this as JSONObject).asLuaTable()
        JSONArray::class -> (this as JSONArray).asLuaTable()
        else -> null //println(value::class.toString() + " " + value.toString())
    }

    // LuaValue -> JsonObject

    private fun LuaValue.toKValue(): Any? =
        when {
            isboolean() -> checkboolean()
            isint() -> checkint()
            isstring() -> checkjstring()
            istable() -> checktable().toJsonObject()
            islong() -> checklong()
            isnumber() -> checkdouble()
            isnil() -> null
            else -> toString()
        }

    private fun LuaTable.toJsonObject(): Any = if (this.length() != 0) {
        JSONArray().apply {
            for (i in 1..this@toJsonObject.length()) {
                this.put(this@toJsonObject.get(i).toKValue())
            }
        }
    } else {
        JSONObject().apply {
            this@toJsonObject.keys().forEach {
                this.put(it.tojstring(), this@toJsonObject.get(it).toKValue())
            }
        }
    }
}