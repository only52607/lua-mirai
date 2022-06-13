package com.github.only52607.luamirai.core

import com.github.only52607.luamirai.core.script.BotScript
import com.github.only52607.luamirai.core.script.BotScriptHeader
import com.github.only52607.luamirai.core.script.BotScriptSource
import java.util.concurrent.ConcurrentHashMap

/**
 * ClassName: BotScriptBuilder
 * Description:
 * date: 2022/1/13 20:48
 * @author ooooonly
 * @version
 */
abstract class BotScriptBuilder(
    val botScriptSource: BotScriptSource
) {

    companion object {
        fun interface Factory {
            fun getBuilder(source: BotScriptSource): BotScriptBuilder
        }

        private val factories = ConcurrentHashMap<String, Factory>()

        fun register(scriptLang: String, factory: Factory) {
            factories[scriptLang.lowercase()] = factory
        }

        fun fromSource(source: BotScriptSource): BotScriptBuilder {
            return factories[source.lang.lowercase()]?.getBuilder(source)
                ?: throw ScriptLangNotFoundException(source.lang)
        }
    }

    abstract val lang: String

    abstract suspend fun buildInstance(): BotScript

    abstract suspend fun readHeader(): BotScriptHeader
}

class ScriptLangNotFoundException(lang: String) : Exception("Script lang for $lang has not been register")