package com.github.only52607.luamirai.core.integration.impl

import com.github.only52607.luamirai.core.factory.buildBotScript
import com.github.only52607.luamirai.core.integration.BotScriptList
import com.github.only52607.luamirai.core.script.BotScript
import com.github.only52607.luamirai.core.script.BotScriptSource

/**
 * ClassName: BotScriptListImpl
 * Description:
 * date: 2021/7/23 22:13
 * @author ooooonly
 * @version
 */
internal class BotScriptListImpl : BotScriptList, MutableList<BotScript> by mutableListOf() {
    override suspend fun addFromSource(source: BotScriptSource): BotScript {
        val script = source.buildBotScript()
        add(script)
        return script
    }
}