package com.github.only52607.luamirai.console

import com.github.only52607.luamirai.core.integration.BotScriptList
import com.github.only52607.luamirai.core.integration.BotScriptSourceList
import com.github.only52607.luamirai.core.script.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.ConsoleCommandSender
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.utils.MiraiExperimentalApi
import net.mamoe.mirai.utils.MiraiInternalApi
import net.mamoe.mirai.utils.MiraiLogger
import java.io.File
import java.net.URL

private const val LUA = "lua"

@OptIn(
    MiraiExperimentalApi::class,
    ConsoleExperimentalApi::class,
    MiraiInternalApi::class,
    kotlinx.serialization.ExperimentalSerializationApi::class
)
@Suppress("UNUSED")
class LuaMiraiCommand(
    private val logger: MiraiLogger,
    private val configFile: File?
): CompositeCommand(
        owner = LuaMiraiPlugin,
        primaryName = "lua",
        description = "lua mirai 指令集"
    ) {
    private val scriptList = BotScriptList()

    private val sourceList = BotScriptSourceList()

    private val sourceAutoStart = mutableMapOf<BotScriptSource, Boolean>()

    fun enable() {
        runBlocking { loadScriptsByConfigFile() }
    }

    fun disable() {
        runBlocking {
            scriptList.forEach { if (it.running) it.stop() }
        }
    }

    @SubCommand
    @Description("打开lua mirai开发文档")
    fun ConsoleCommandSender.doc() {
        val website = "https://ooooonly.gitee.io/lua-mirai-doc/#/"
        kotlin.runCatching {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler $website")
        }
        logger.info(website)
    }

    // Source Commands

    @SubCommand("source list")
    @Description("列出所有脚本源")
    fun ConsoleCommandSender.list() {
        sourceList.forEachIndexed { index, botScriptSource ->
            logger.info("[$index] $botScriptSource")
        }
    }

    @SubCommand("source add")
    @Description("新增脚本源")
    suspend fun ConsoleCommandSender.add(@Name("文件名或URL") fileName: String, @Name("立即执行") execute: Boolean = false) {
        val source = if (fileName.contains(":/")) {
            BotScriptSource.FileSource(File(fileName), LUA)
        } else {
            BotScriptSource.URLSource(URL(fileName), LUA)
        }
        sourceList.add(source)
        logger.info("添加脚本源[${sourceList.size - 1}] $fileName 成功")
        if (execute) {
            scriptList.addFromSource(source)
        }
        updateConfig()
    }

    @SubCommand("source remove")
    @Description("删除指定位置上的脚本源")
    fun ConsoleCommandSender.remove(@Name("索引") index: Int) {
        sourceList.removeAt(index)
        updateConfig()
    }

    // Script Commands

    @SubCommand("script list")
    @Description("列出正在运行的脚本")
    suspend fun ConsoleCommandSender.listScript() {
        scriptList.forEachIndexed { index, botScript ->
            logger.info("[$index] $botScript 来自 ${botScript.source}")
        }
    }

    @SubCommand("script stop")
    @Description("停用一个脚本（该操作会停止脚本以及脚本内注册的所有事件监听器）")
    suspend fun ConsoleCommandSender.stop(@Name("脚本编号") scriptId: Int) {
        scriptList[scriptId].stop()
        logger.info("停用脚本[${scriptList[scriptId]}]成功")
        scriptList.removeAt(scriptId)
    }

    @SubCommand("script info")
    @Description("查看脚本信息")
    fun ConsoleCommandSender.info(@Name("脚本编号") scriptId: Int) {
        val source = scriptList[scriptId].header
        source ?: return
        logger.info("名称：${source["name"]}")
        logger.info("版本：${source["version"]}")
        logger.info("作者：${source["author"]}")
        logger.info("描述：${source["description"]}")
    }


    /**
     * 从配置文件读取已加载脚本信息
     */
    private suspend fun loadScriptsByConfigFile() {
        if (configFile == null || !configFile.exists()) return
        val jsonConfigArray = Json.parseToJsonElement(configFile.readText()).jsonArray
        jsonConfigArray.forEachIndexed { _, itemElement ->
            kotlin.runCatching {
                val item = itemElement.jsonObject
                val source: BotScriptSource = when (val typeName = item["type"]?.jsonPrimitive?.contentOrNull) {
                    "file" -> BotScriptSource.FileSource(File(item["file"]?.jsonPrimitive?.contentOrNull!!), LUA)
                    "content" -> BotScriptSource.StringSource(item["content"]?.jsonPrimitive?.contentOrNull!!, LUA)
                    "url" -> BotScriptSource.URLSource(URL(item["url"]?.jsonPrimitive?.contentOrNull!!), LUA)
                    else -> throw IllegalArgumentException("Unsupported script type $typeName")
                }
                if (item["enable"]?.jsonPrimitive?.booleanOrNull == true) {
                    scriptList.addFromSource(source)
                }
            }
        }
    }

    private val jsonFormat by lazy {
        Json { prettyPrint = true }
    }

    /**
     * 写出脚本信息到配置文件
     */
    private fun updateConfig() {
        configFile ?: return
        val configArray = buildJsonArray {
            sourceList.forEach { source: BotScriptSource ->
                add(buildJsonObject {
                    when (source) {
                        is BotScriptSource.FileSource -> {
                            this@buildJsonObject.put("type", "file")
                            this@buildJsonObject.put("file", source.file.path)
                        }
                        is BotScriptSource.StringSource -> {
                            this@buildJsonObject.put("type", "content")
                            this@buildJsonObject.put("content", source.content)
                        }
                        is BotScriptSource.URLSource -> {
                            this@buildJsonObject.put("type", "url")
                            this@buildJsonObject.put("url", source.url.toString())
                        }
                        else -> {}
                    }
                    this@buildJsonObject.put("enable", sourceAutoStart[source] == true)
                })
            }
        }
        configFile.writeText(jsonFormat.encodeToString(configArray))
    }
}