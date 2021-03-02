package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.BotScriptManager
import kotlinx.serialization.json.*
import java.io.File
import java.lang.IndexOutOfBoundsException

@Suppress("unused")
class LuaMiraiBotScriptManager(private val configFile: File?) : BotScriptManager {
    private val scripts: MutableList<LuaMiraiScript> = mutableListOf()
    init {
        kotlin.runCatching {
            loadScriptsFromConfigFile()
        }
    }

    private fun loadScriptsFromConfigFile() {
        if (configFile == null || !configFile.exists()) return
        val jsonConfigArray = Json.parseToJsonElement(configFile.readText()).jsonArray
        jsonConfigArray.forEach { itemElement ->
            kotlin.runCatching {
                val item = itemElement.jsonObject
                if (item["enable"]?.jsonPrimitive?.booleanOrNull == true)
                    load(item["file"]!!.jsonPrimitive.content)
                else
                    add(item["file"]!!.jsonPrimitive.content)
            }
        }
    }

    fun updateConfig() {
        if (configFile == null) return
        kotlin.runCatching {
            val configArray = buildJsonArray {
                scripts.forEach { script ->
                    add(buildJsonObject {
                        this@buildJsonObject.put("file", script.getInfo().file)
                        this@buildJsonObject.put("enable", script.isLoaded())
                    })
                }
            }
            configFile.writeText(configArray.toString())
        }
    }

    private fun getScriptByIndex(index: Int) =
        scripts.getOrNull(index) ?: throw IndexOutOfBoundsException("脚本编号[$index]不存在")

    override fun list() = scripts

    override fun add(fileName: String) {
        val script = LuaMiraiScript(sourceFile = File(fileName))
        scripts.add(script)
        script.create()
    }

    override fun load(fileName: String) {
        val script = LuaMiraiScript(sourceFile = File(fileName))
        scripts.add(script)
        script.create()
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
        getScriptByIndex(scriptId).destroy()
        scripts.removeAt(scriptId)
    }

    override fun stopAll() {
        scripts.forEach { it.stop() }
    }
}