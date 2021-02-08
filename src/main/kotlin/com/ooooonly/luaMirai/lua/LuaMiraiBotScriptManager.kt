package com.ooooonly.luaMirai.lua

import net.mamoe.mirai.utils.MiraiExperimentalApi
import java.io.File

@Suppress("unused")
@MiraiExperimentalApi
object LuaMiraiBotScriptManager : BotScriptManager {
    private val scripts: MutableList<BotScript> = mutableListOf()

    private fun getScriptByIndex(index: Int) = scripts.getOrNull(index) ?: throw Exception("脚本编号[$index]不存在")

    override fun list() = scripts

    override fun add(fileName: String) {
        scripts.add(LuaMiraiScript(sourceFile = File(fileName)))
    }

    override fun load(fileName: String) {
        val script = LuaMiraiScript(sourceFile = File(fileName))
        scripts.add(script)
        script.load()
    }

    override fun execute(scriptId: Int) {
        getScriptByIndex(scriptId).load()
    }

    override fun reload(scriptId: Int) {
        getScriptByIndex(scriptId).reload()
    }

    override fun stop(scriptId: Int) {
        getScriptByIndex(scriptId).stop()
    }

    override fun delete(scriptId: Int) {
        getScriptByIndex(scriptId).stop()
        scripts.removeAt(scriptId)
    }

    override fun stopAll() {
        scripts.forEach { it.stop() }
    }
}