package com.ooooonly.luaMirai.lua.bridge.base

import org.luaj.vm2.LuaValue

abstract class BaseMsg : LuaValue() {
    abstract var type: String
    abstract fun recall()
    abstract fun downloadImage(path: String)
    abstract fun getImageUrl(): String
    abstract fun toTable(): Array<out BaseMsg>
    override fun typename(): String = this::class.simpleName ?: ""
    override fun type(): Int = this::class.hashCode()
}