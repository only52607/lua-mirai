package com.github.only52607.luamirai.core

import java.util.*

interface ScriptProvider {
    fun supportSource(source: ScriptSource): Boolean
    suspend fun load(source: ScriptSource): Script
}

suspend fun ScriptSource.load(): Script {
    val s: ServiceLoader<ScriptProvider> = ServiceLoader.load(ScriptProvider::class.java)
    val iterator: Iterator<ScriptProvider> = s.iterator()
    while (iterator.hasNext()) {
        val provider: ScriptProvider = iterator.next()
        if (provider.supportSource(this)) {
            return provider.load(this)
        }
    }
    throw Exception("No supported provider for $this")
}