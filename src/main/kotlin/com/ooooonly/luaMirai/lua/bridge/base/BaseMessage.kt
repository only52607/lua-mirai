package com.ooooonly.luaMirai.lua.bridge.base

import com.ooooonly.luakt.luaTableOf
import com.ooooonly.luakt.luakotlin.KotlinInstanceInLua
import com.ooooonly.luakt.setAll
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue


abstract class BaseMessage : KotlinInstanceInLua() {
    init {
        m_instance = this
        m_metatable = luaTableOf {
        }.setAll(LuaString.s_metatable[INDEX] as LuaTable)
    }
    /*MessageSource成员*/
    abstract val id: Int
    abstract val internalId: Int
    abstract val timestamp: Int //即MessageSource.time
    abstract val fromId: Long
    abstract val targetId: Long

    override fun get(key: LuaValue): LuaValue =
        m_metatable?.run { get(key) }?.takeIf { it != LuaValue.NIL } ?: super.get(key)

    abstract var type: String
    abstract var params: Array<String>

    abstract fun recall()
    abstract fun downloadImage(path: String)
    abstract fun getImageUrl(): String
    abstract fun toTable(): Array<out BaseMessage>

    abstract fun append(msg: LuaValue?): BaseMessage
    abstract fun append(message: BaseMessage?): BaseMessage

    abstract fun appendTo(msg: LuaValue?): BaseMessage
    abstract fun appendTo(message: BaseMessage?): BaseMessage

    abstract override fun toString(): String
    abstract override fun length(): Int

    //重载操作符
    override fun add(value: LuaValue?): LuaValue = if (value is BaseMessage) append(value) else append(value)
    override fun concat(value: LuaValue?): LuaValue = if (value is BaseMessage) append(value) else append(value)
    override fun concatTo(lhs: LuaString?): LuaValue? = if (lhs is BaseMessage) appendTo(lhs) else appendTo(lhs)
    override fun concatTo(lhs: LuaValue?): LuaValue? = if (lhs is BaseMessage) appendTo(lhs) else appendTo(lhs)

    override fun lteq(rhs: LuaValue): LuaValue? = if (_eq_(rhs)) LuaValue.TRUE else LuaValue.FALSE
    override fun lteq_b(rhs: LuaValue): Boolean = _eq_(rhs)
    override fun eq(rhs: LuaValue): LuaValue? = if (_eq_(rhs)) LuaValue.TRUE else LuaValue.FALSE
    override fun eq_b(rhs: LuaValue): Boolean = _eq_(rhs)
    override fun raweq(rhs: LuaValue): Boolean = _eq_(rhs)
    private fun _eq_(rhs: LuaValue): Boolean = toString().equals(rhs.toString())

    override fun checkjstring(): String? = toString()
    override fun checkstring(): LuaString? = LuaString.valueOf(toString())
    override fun tostring(): LuaValue? = LuaString.valueOf(toString())
}