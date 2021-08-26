package com.ooooonly.luaMirai.base

import java.lang.IndexOutOfBoundsException

/**
 * ClassName: AbstractBotScriptManager
 * Description:
 * date: 2021/7/23 22:13
 * @author ooooonly
 * @version
 */
abstract class AbstractBotScriptManager<S : BotScript, T: BotScriptSource> :
    BotScriptManager<S, T> {

    private val scripts: MutableList<S> = mutableListOf()

    protected fun appendScript(script: S): Int {
        scripts.add(script)
        return scripts.size - 1
    }

    override fun list() = scripts.toList()

    override fun getScript(index: Int) =
        scripts.getOrNull(index) ?: throw IndexOutOfBoundsException("Index $index out of script bounds!")

    override suspend fun load(source: T): Int {
        return add(source).also { getScript(it).load() }
    }

    override suspend fun execute(scriptId: Int) {
        getScript(scriptId).load()
    }

    override suspend fun reload(scriptId: Int) {
        getScript(scriptId).reload()
    }

    override suspend fun stop(scriptId: Int) {
        getScript(scriptId).stop()
    }

    override suspend fun delete(scriptId: Int) {
        getScript(scriptId).stop()
        scripts.removeAt(scriptId)
    }

    override suspend fun stopAll() {
        scripts.forEach { it.stop() }
    }

    override suspend fun reloadAll() {
        scripts.forEach { it.reload() }
    }

    override suspend fun deleteAll() {
        stopAll()
        scripts.clear()
    }
}