package com.github.only52607.luamirai.core.integration

import com.github.only52607.luamirai.core.integration.impl.BotScriptListImpl
import com.github.only52607.luamirai.core.script.BotScript
import com.github.only52607.luamirai.core.script.BotScriptSource

interface BotScriptList : MutableList<BotScript> {
    /**
     * 创建脚本
     * 返回脚本索引
     */
    suspend fun addFromSource(source: BotScriptSource)
}

fun BotScriptList(): BotScriptList = BotScriptListImpl()