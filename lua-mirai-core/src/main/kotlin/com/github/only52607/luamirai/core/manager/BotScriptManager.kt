package com.github.only52607.luamirai.core.manager

import com.github.only52607.luamirai.core.factory.BotScriptBuilderRegistry
import com.github.only52607.luamirai.core.script.BotScript
import java.io.File
import java.io.InputStream
import java.lang.IndexOutOfBoundsException
import java.net.URL

/**
 * ClassName: BotScriptManager
 * Description:
 * date: 2021/7/23 22:13
 * @author ooooonly
 * @version
 */
class BotScriptManager : IBotScriptManager {

    private val scripts: MutableList<BotScript> = mutableListOf()

    private fun appendScript(script: BotScript): Int {
        scripts.add(script)
        return scripts.size - 1
    }

    override fun list() = scripts.toList()

    override fun get(index: Int) =
        scripts.getOrNull(index) ?: throw IndexOutOfBoundsException("Index $index out of script bounds!")

    override suspend fun add(scriptLang: String, file: File, chunkName: String): Int {
        val script = BotScriptBuilderRegistry.getBotScriptBuilder(scriptLang).buildBotScript(file, chunkName)
        scripts.add(script)
        return scripts.size - 1
    }

    override suspend fun add(scriptLang: String, file: File): Int {
        val script = BotScriptBuilderRegistry.getBotScriptBuilder(scriptLang).buildBotScript(file)
        scripts.add(script)
        return scripts.size - 1
    }

    override suspend fun add(scriptLang: String, url: URL, chunkName: String): Int {
        val script = BotScriptBuilderRegistry.getBotScriptBuilder(scriptLang).buildBotScript(url, chunkName)
        scripts.add(script)
        return scripts.size - 1
    }

    override suspend fun add(scriptLang: String, url: URL): Int {
        val script = BotScriptBuilderRegistry.getBotScriptBuilder(scriptLang).buildBotScript(url)
        scripts.add(script)
        return scripts.size - 1
    }

    override suspend fun add(scriptLang: String, content: String, chunkName: String): Int {
        val script = BotScriptBuilderRegistry.getBotScriptBuilder(scriptLang).buildBotScript(content, chunkName)
        scripts.add(script)
        return scripts.size - 1
    }

    override suspend fun add(scriptLang: String, inputStream: InputStream, chunkName: String): Int {
        val script = BotScriptBuilderRegistry.getBotScriptBuilder(scriptLang).buildBotScript(inputStream, chunkName)
        scripts.add(script)
        return scripts.size - 1
    }

    override suspend fun add(scriptLang: String, inputStream: InputStream): Int {
        val script = BotScriptBuilderRegistry.getBotScriptBuilder(scriptLang).buildBotScript(inputStream)
        scripts.add(script)
        return scripts.size - 1
    }


    override suspend fun start(scriptId: Int) {
        get(scriptId).start()
    }

    override suspend fun restart(scriptId: Int) {
        get(scriptId).restart()
    }

    override suspend fun stop(scriptId: Int) {
        get(scriptId).stop()
    }

    override suspend fun remove(scriptId: Int) {
        get(scriptId).stop()
        scripts.removeAt(scriptId)
    }

    override suspend fun stopAll() {
        scripts.forEach { it.stop() }
    }

    override suspend fun restartAll() {
        scripts.forEach { it.restart() }
    }

    override suspend fun removeAll() {
        stopAll()
        scripts.clear()
    }
}