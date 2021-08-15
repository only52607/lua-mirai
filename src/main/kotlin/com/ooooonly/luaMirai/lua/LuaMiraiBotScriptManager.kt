package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.base.AbstractBotScriptManager
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import net.mamoe.mirai.utils.MiraiInternalApi
import java.io.File
import java.io.FileNotFoundException

@Suppress("unused")
@MiraiInternalApi
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
        val format = Json { prettyPrint = true }
        configFile.writeText(format.encodeToString(configArray))
    }

    override fun add(file: File): Int {
        if (!file.exists()) throw FileNotFoundException("文件 ${file.absolutePath} 不存在！")
        val script = LuaMiraiScript(file)
        scripts.add(script)
        script.create()
        return scripts.size - 1
    }
}