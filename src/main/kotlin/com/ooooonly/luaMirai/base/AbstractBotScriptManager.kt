package com.ooooonly.luaMirai.base

import java.lang.IndexOutOfBoundsException

/**
 * ClassName: AbstractBotScriptManager
 * Description:
 * date: 2021/7/23 22:13
 * @author ooooonly
 * @version
 */
abstract class AbstractBotScriptManager<Script : BotScript<Info>, Info, Source> :
    BotScriptManager<Script, Info, Source> {

    private val scripts: MutableList<Script> = mutableListOf()

    protected fun appendScript(script: Script): Int {
        scripts.add(script)
        return scripts.size - 1
    }

    override fun list() = scripts.toList()

    override fun getScript(index: Int) =
        scripts.getOrNull(index) ?: throw IndexOutOfBoundsException("Index $index out of script bounds!")

    override fun load(source: Source): Int {
        return add(source).also { getScript(it).load() }
    }

    override fun execute(scriptId: Int) {
        getScript(scriptId).load()
    }

    override fun reload(scriptId: Int) {
        getScript(scriptId).reload()
    }

    override fun stop(scriptId: Int) {
        getScript(scriptId).stop()
    }

    override fun delete(scriptId: Int) {
        getScript(scriptId).destroy()
        scripts.removeAt(scriptId)
    }

    override fun stopAll() {
        scripts.forEach { it.stop() }
    }

    override fun reloadAll() {
        scripts.forEach { it.reload() }
    }

    override fun deleteAll() {
        stopAll()
        scripts.clear()
    }
}