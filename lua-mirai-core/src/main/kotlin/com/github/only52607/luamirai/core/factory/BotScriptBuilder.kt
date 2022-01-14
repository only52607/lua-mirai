package com.github.only52607.luamirai.core.factory

import com.github.only52607.luamirai.core.script.BotScript
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

    suspend fun buildBotScript(file: File, chunkName: String): BotScript {
        throw NotImplementedError()
    }

    suspend fun buildBotScript(file: File): BotScript = buildBotScript(file, file.name)

    suspend fun buildBotScript(url: URL, chunkName: String): BotScript {
        throw NotImplementedError()
    }

    suspend fun buildBotScript(url: URL): BotScript = buildBotScript(url, "$url")

    suspend fun buildBotScript(content: String, chunkName: String): BotScript {
        throw NotImplementedError()
    }

    suspend fun buildBotScript(content: String): BotScript = buildBotScript(content, content)

    suspend fun buildBotScript(inputStream: InputStream, chunkName: String): BotScript {
        throw NotImplementedError()
    }

    suspend fun buildBotScript(inputStream: InputStream): BotScript = buildBotScript(inputStream, "[From InputStream]")
}