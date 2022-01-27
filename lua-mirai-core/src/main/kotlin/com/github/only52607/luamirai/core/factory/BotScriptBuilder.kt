package com.github.only52607.luamirai.core.factory

import com.github.only52607.luamirai.core.script.BotScript
import com.github.only52607.luamirai.core.script.BotScriptSource
import kotlinx.coroutines.Job
import java.io.File
import java.io.InputStream
import java.net.URL

/**
 * ClassName: BotScriptBuilder
 * Description:
 * date: 2022/1/13 20:48
 * @author ooooonly
 * @version
 */
interface BotScriptBuilder {
    suspend fun buildBotScript(source: BotScriptSource): BotScript
}