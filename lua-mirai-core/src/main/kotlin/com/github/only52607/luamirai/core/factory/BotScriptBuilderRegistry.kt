package com.github.only52607.luamirai.core.factory

import com.github.only52607.luamirai.core.script.BotScript
import com.github.only52607.luamirai.core.script.BotScriptSource
import java.util.concurrent.ConcurrentHashMap

/**
 * ClassName: BotScriptBuilderRegistry
 * Description:
 * date: 2022/1/13 20:49
 * @author ooooonly
 * @version
 */
class BotScriptBuilderRegistry {
    companion object {
        private val builders = ConcurrentHashMap<String, BotScriptBuilder>()

        fun registerBotScriptBuilder(scriptLang: String, builder: BotScriptBuilder) {
            builders[scriptLang.lowercase()] = builder
        }

        fun getBotScriptBuilder(scriptLang: String): BotScriptBuilder {
            return builders[scriptLang.lowercase()] ?: throw ScriptLangNotFoundException(scriptLang)
        }
    }
}

suspend fun BotScriptSource.buildBotScript(): BotScript =
    BotScriptBuilderRegistry.getBotScriptBuilder(this.lang).buildBotScript(this)

class ScriptLangNotFoundException(lang: String) : Exception("Script lang for $lang has not been register")
