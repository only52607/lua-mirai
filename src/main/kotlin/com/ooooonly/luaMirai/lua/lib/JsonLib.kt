package com.ooooonly.luaMirai.lua.lib

import com.ooooonly.luakt.buildLuaTable
import com.ooooonly.luakt.luaFunctionOf
import org.json.JSONArray
import org.json.JSONObject
import org.luaj.vm2.*
import org.luaj.vm2.lib.TwoArgFunction

open class JsonLib : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        val globals = env?.checkglobals()
        val parseJson = luaFunctionOf { raw: String ->
            return@luaFunctionOf json2lua(JSONObject(raw))
        }
        val toJson = luaFunctionOf { raw: LuaTable ->
            return@luaFunctionOf raw.toJsonObject().toString()
        }
        globals?.set("Json", buildLuaTable {
            "parseJson" to parseJson
            "toJson" to toJson
        })
        LuaString.s_metatable[INDEX].set("parseJson", parseJson)
        return LuaValue.NIL
    }

    private fun json2lua(jsonObj: JSONObject): LuaTable = LuaTable().apply {
        jsonObj.keySet().forEach { key ->
            val value = jsonObj.get(key)
            val luaValue = value.toLuaValue()
            luaValue?.let {
                set(key, luaValue)
            }
        }
    }

    private fun jsonArr2lua(jsonArr: JSONArray): LuaTable = LuaTable.listOf(
        mutableListOf<LuaValue>().apply { jsonArr.forEach { it.toLuaValue()?.let(::add) } }.toTypedArray()
    )

    private fun Any.toLuaValue() = when (this::class) {
        Unit::class -> LuaValue.NIL
        Short::class -> LuaValue.valueOf((this as Short).toInt())
        Int::class -> LuaValue.valueOf(this as Int)
        Long::class -> LuaValue.valueOf(this.toString()) //luaj不支持long类型
        Float::class -> LuaValue.valueOf((this as Float).toDouble())
        Double::class -> LuaValue.valueOf(this as Double)
        Char::class -> LuaValue.valueOf(this.toString())
        String::class -> LuaValue.valueOf(this as String)
        Boolean::class -> LuaValue.valueOf(this as Boolean)
        JSONObject::class -> json2lua((this as JSONObject))
        JSONArray::class -> jsonArr2lua(this as JSONArray)
        else -> null//println(value::class.toString() + " " + value.toString())
    }

    private fun LuaValue.toKValue(): Any? =
        if (isboolean()) checkboolean()
        else if (isint()) checkint()
        else if (isstring()) checkjstring()
        else if (istable()) checktable().toJsonObject()
        else if (islong()) checklong()
        else if (isnumber()) checkdouble()
        else if (isnil()) null
        else toString()


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