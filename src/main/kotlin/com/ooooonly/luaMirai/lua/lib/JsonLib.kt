package com.ooooonly.luaMirai.lua.lib

import com.ooooonly.luaMirai.utils.setFunction1Arg
import org.json.JSONArray
import org.json.JSONObject
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

open class JsonLib() : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue): LuaValue {
        val globals: Globals = env.checkglobals()
        val jsonTable: LuaTable = LuaTable()
        jsonTable.apply {
            setFunction1Arg("parseJson") {
                json2lua(JSONObject(it.checkjstring()))
            }
            setFunction1Arg("toJson") {
                LuaValue.valueOf(it.checktable().toJsonObject().toString())
            }
        }
        globals.set("Json", jsonTable)

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


    private fun Any.toLuaValue() = when (this::class.java) {
        Int::class.java -> LuaValue.valueOf(this as Int)
        String::class.java -> LuaValue.valueOf(this as String)
        Boolean::class.java -> LuaValue.valueOf(this as Boolean)
        JSONObject::class.java -> json2lua((this as JSONObject))
        JSONArray::class.java -> jsonArr2lua(this as JSONArray)
        else -> null//println(value::class.toString() + " " + value.toString())
    }

    private fun LuaValue.toKValue(): Any? =
        if (isboolean()) checkboolean()
        else if (isint()) checkint()
        else if (isstring()) checkjstring()
        else if (istable()) checktable().toJsonObject()
        else null


    private fun LuaTable.toJsonObject() = if (this.length() != 0) {
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