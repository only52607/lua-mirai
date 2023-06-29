package com.github.only52607.luamirai.core

import java.util.*
import kotlin.reflect.KClass

interface ScriptProvider {
    fun supportSource(source: ScriptSource): Boolean
    suspend fun load(source: ScriptSource): Script
}

suspend fun ScriptSource.load(classes: Collection<KClass<in ScriptProvider>>): Script {
    for (clazz in classes) {
        val provider = clazz.constructors.first().call() as ScriptProvider
        if (provider.supportSource(this)) {
            return provider.load(this)
        }
    }
    throw Exception("No providers supported for $this, ${classes.size} providers tried.")
}

suspend fun ScriptSource.load(): Script {
    val s: ServiceLoader<ScriptProvider> = ServiceLoader.load(ScriptProvider::class.java)
    val iterator: Iterator<ScriptProvider> = s.iterator()
    var cnt = 0
    while (iterator.hasNext()) {
        cnt++
        val provider: ScriptProvider = iterator.next()
        if (provider.supportSource(this)) {
            return provider.load(this)
        }
    }
    throw Exception("No providers supported for $this, $cnt providers tried.")
}