package com.github.only52607.luamirai.core

interface ScriptProvider {
    fun supportSource(source: ScriptSource): Boolean
    suspend fun fromSource(source: ScriptSource): Script
}