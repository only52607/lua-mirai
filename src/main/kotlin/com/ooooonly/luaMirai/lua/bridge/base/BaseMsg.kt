package com.ooooonly.luaMirai.lua.bridge.base

import com.ooooonly.luaMirai.lua.bridge.coreimpl.MsgCoreImpl
import com.ooooonly.luakt.luaFunctionOf
import com.ooooonly.luakt.luaTableOf
import com.ooooonly.luakt.setAll
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaUserdata
import org.luaj.vm2.LuaValue


abstract class BaseMsg : LuaUserdata(null) {
    init {
        m_metatable = luaTableOf {
            INDEX to luaTableOf {
                "recall" to luaFunctionOf { obj: BaseMsg ->
                    obj.recall()
                }
                "downloadImage" to luaFunctionOf { obj: BaseMsg, filePath: String ->
                    obj.downloadImage(filePath)
                }
                "getImageUrl" to luaFunctionOf { obj: BaseMsg ->
                    return@luaFunctionOf obj.getImageUrl()
                }
                "toTable" to luaFunctionOf { obj: BaseMsg ->
                    return@luaFunctionOf obj.toTable()
                }
            }.setAll(LuaString.s_metatable[INDEX] as LuaTable)
        }
        m_instance = this
    }

    override fun checkjstring(): String? = toString()
    override fun checkstring(): LuaString? = LuaString.valueOf(toString())
    override fun tostring(): LuaValue? = LuaString.valueOf(toString())

    abstract var type: String
    abstract var params: LuaTable

    abstract fun recall()
    abstract fun downloadImage(path: String)
    abstract fun getImageUrl(): String
    abstract fun toTable(): Array<out BaseMsg>

    abstract fun append(msg: LuaValue?): BaseMsg
    abstract fun append(msg: BaseMsg?): BaseMsg

    abstract fun appendTo(msg: LuaValue?): BaseMsg
    abstract fun appendTo(msg: BaseMsg?): BaseMsg

    abstract override fun toString(): String
    abstract override fun length(): Int

    //重载操作符
    override fun add(value: LuaValue?): LuaValue = if (value is BaseMsg) append(value) else append(value)
    override fun concat(value: LuaValue?): LuaValue = if (value is BaseMsg) append(value) else append(value)
    override fun concatTo(lhs: LuaValue?): LuaValue? = if (lhs is BaseMsg) appendTo(lhs) else appendTo(lhs)

    override fun lteq(rhs: LuaValue): LuaValue? = if (_eq_(rhs)) LuaValue.TRUE else LuaValue.FALSE
    override fun lteq_b(rhs: LuaValue): Boolean = _eq_(rhs)
    override fun eq(rhs: LuaValue): LuaValue? = if (_eq_(rhs)) LuaValue.TRUE else LuaValue.FALSE
    override fun eq_b(rhs: LuaValue): Boolean = _eq_(rhs)
    override fun raweq(rhs: LuaValue): Boolean = _eq_(rhs)
    private fun _eq_(rhs: LuaValue): Boolean = toString().equals(rhs.toString())
}