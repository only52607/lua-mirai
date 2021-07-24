package com.ooooonly.luaMirai

import com.ooooonly.luaMirai.lua.LuaMiraiScript
import java.io.File
import java.lang.IndexOutOfBoundsException

/**
 * ClassName: AbstractBotScriptManager
 * Description:
 * date: 2021/7/23 22:13
 * @author ooooonly
 * @version
 */
abstract class AbstractBotScriptManager<S: BotScript>: BotScriptManager {

    protected val scripts: MutableList<S> = mutableListOf()

    override fun list() = scripts

    override fun getScript(index: Int) =
        scripts.getOrNull(index) ?: throw IndexOutOfBoundsException("Index ${index} out of script bounds!")

    override fun load(file: File): Int {
        return add(file).also { getScript(it).load() }
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