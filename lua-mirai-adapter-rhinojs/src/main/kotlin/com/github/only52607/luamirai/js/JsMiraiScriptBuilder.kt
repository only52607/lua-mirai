package com.github.only52607.luamirai.js

import com.github.only52607.luamirai.core.BotScriptBuilder
import com.github.only52607.luamirai.core.script.BotScript
import com.github.only52607.luamirai.core.script.BotScriptHeader
import com.github.only52607.luamirai.core.script.BotScriptSource
import com.github.only52607.luamirai.core.script.mutableBotScriptHeaderOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * ClassName: JsMiraiScriptBuilder
 * Description:
 * date: 2022/6/14 22:33
 * @author ooooonly
 * @version
 */
@Suppress("unused")
class JsMiraiScriptBuilder(
    val source: BotScriptSource
) : BotScriptBuilder(source) {
    companion object {
        const val LANG = "js"

        init {
            register(LANG, ::JsMiraiScriptBuilder)
        }
    }

    override val lang: String
        get() = LANG

    override suspend fun buildInstance(): BotScript {
        return withContext(Dispatchers.IO) {
            JsMiraiScript(source, readHeader())
        }
    }

    override suspend fun readHeader(): BotScriptHeader {
        val manifestInputStream =
            source.resourceFinder?.findResource("manifest.json") ?: return mutableBotScriptHeaderOf()
        return BotScriptHeader.fromJsonManifest(String(manifestInputStream.readBytes()))
    }
}