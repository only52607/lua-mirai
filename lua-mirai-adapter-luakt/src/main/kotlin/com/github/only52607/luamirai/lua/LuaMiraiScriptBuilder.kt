package com.github.only52607.luamirai.lua

import com.github.only52607.luamirai.core.factory.BotScriptBuilder
import com.github.only52607.luamirai.core.factory.BotScriptBuilderRegistry
import com.github.only52607.luamirai.core.script.BotScript
import com.github.only52607.luamirai.core.script.BotScriptSource

/**
 * ClassName: LuaMiraiScriptBuilder
 * Description:
 * date: 2022/1/13 21:37
 * @author ooooonly
 * @version
 */
@Suppress("unused")
object LuaMiraiScriptBuilder : BotScriptBuilder {
    init {
        BotScriptBuilderRegistry.registerBotScriptBuilder("lua", LuaMiraiScriptBuilder)
    }

    override suspend fun buildBotScript(source: BotScriptSource): BotScript {
        return LuaMiraiScript(source)
    }
}