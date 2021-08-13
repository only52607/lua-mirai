package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.AbstractBotScriptManager
import kotlinx.serialization.json.*
import java.io.File
import java.io.FileNotFoundException

@Suppress("unused")
class LuaMiraiBotScriptManager(private val configFile: File?) : AbstractBotScriptManager<LuaMiraiScript>() {
    /**
     * 从配置文件读取已加载脚本信息
     */
    fun loadScriptsByConfig() {
        if (configFile == null || !configFile.exists()) return
        val jsonConfigArray = Json.parseToJsonElement(configFile.readText()).jsonArray
        jsonConfigArray.forEachIndexed { _, itemElement -> kotlin.runCatching {
            val item = itemElement.jsonObject
            val fileName = item["file"]?.jsonPrimitive?.contentOrNull!!
            if (item["enable"]?.jsonPrimitive?.booleanOrNull == true) {
                load(File(fileName))
            } else {
                add(File(fileName))
            }
        } }
    }

    /**
     * 写出脚本信息到配置文件
     */
    fun updateConfig() {
        if (configFile == null) return
        val configArray = buildJsonArray {
            scripts.forEach { script ->
                add(buildJsonObject {
                    this@buildJsonObject.put("file", script.info.file)
                    this@buildJsonObject.put("enable", script.isLoaded)
                })
            }
        }
        configFile.writeText(configArray.toString())
    }

    override fun add(file: File): Int {
        if (!file.exists()) throw FileNotFoundException("文件 ${file.absolutePath} 不存在！")
        val script = LuaMiraiScript(file)
        scripts.add(script)
        script.create()
        return scripts.size - 1
    }
}