package com.ooooonly.luaMirai.base

import java.io.File
import java.io.InputStream
import java.net.URL

/**
 * ClassName: BotScriptSourceFactory
 * Description:
 * date: 2021/8/26 18:49
 * @author ooooonly
 * @version
 */
interface BotScriptSourceFactory {
    suspend fun buildSource(file: File): BotScriptSource
    suspend fun buildSource(url: URL): BotScriptSource
    suspend fun buildSource(url: URL, chunkName: String): BotScriptSource
    suspend fun buildSource(content: String): BotScriptSource
    suspend fun buildSource(content: String, chunkName: String): BotScriptSource
    suspend fun buildSource(inputStream: InputStream): BotScriptSource
    suspend fun buildSource(inputStream: InputStream, chunkName: String): BotScriptSource
}

abstract class AbstractBotScriptSourceFactory: BotScriptSourceFactory {
    override suspend fun buildSource(url: URL) = buildSource(url, "$url")
    override suspend fun buildSource(content: String) = buildSource(content, content)
    override suspend fun buildSource(inputStream: InputStream) = buildSource(inputStream, "InputStream")
}