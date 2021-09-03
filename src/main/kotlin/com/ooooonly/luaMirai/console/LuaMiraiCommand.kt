package com.ooooonly.luaMirai.console

import com.ooooonly.luaMirai.lua.LuaMiraiBotScriptManager
import com.ooooonly.luaMirai.lua.LuaMiraiScript
import com.ooooonly.luaMirai.lua.LuaSource
import com.ooooonly.luaMirai.lua.LuaSourceFactory
import kotlinx.coroutines.launch
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


@Suppress("unused")
@MiraiExperimentalApi
@ConsoleExperimentalApi
@MiraiInternalApi
class LuaMiraiCommand(
    private val manager: LuaMiraiBotScriptManager,
    private val logger: MiraiLogger,
    private val configFile: File?
): CompositeCommand(
        owner = LuaMiraiPlugin,
        primaryName = "lua",
        description = "lua mirai 指令集"
    ) {

    @SubCommand
    @Description("打开lua mirai开发文档")
    fun ConsoleCommandSender.doc() {
        val website = "https://ooooonly.gitee.io/lua-mirai-doc/#/"
        kotlin.runCatching {
            Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler $website")
        }
        logger.info(website)
    }

    @SubCommand
    @Description("以 '[脚本编号] 文件名' 的形式列出所有已加载脚本")
    fun ConsoleCommandSender.list() {
        val list = manager.list()
        logger.info("已加载${list.size}个脚本：")
        list.forEachIndexed { i: Int, botScript: LuaMiraiScript ->
            logger.info(
                "[$i]\t${botScript.header["name"] ?: "未知脚本"}\t${if (botScript.isLoaded) "已启用" else "未启用"}"
            )
        }
    }

    @SubCommand
    @Description("载入一个脚本，但不执行")
    fun ConsoleCommandSender.add(@Name("文件名") fileName: String) {
        try {
            val index = manager.add(LuaSource.LuaFileSource(fileName))
            logger.info("添加脚本[$index] $fileName 成功")
            updateConfig()
        } catch (e: Exception) {
            logger.error(e.message)
        }
    }

    @SubCommand
    @Description("载入一个远程脚本，但不执行")
    fun ConsoleCommandSender.addRemote(@Name("脚本URL") path: String) {
        try {
            launch {
                val index = manager.add(LuaSourceFactory.buildSource(URL(path)) as LuaSource)
                logger.info("添加脚本[$index] $path 成功")
                updateConfig()
            }
        } catch (e: Exception) {
            logger.error(e.message)
        }
    }

    @SubCommand
    @Description("载入一个脚本，并且执行")
    fun ConsoleCommandSender.load(@Name("文件名") fileName: String) {
        try {
            launch {
                val index = manager.load(LuaSourceFactory.buildSource(File(fileName)))
                logger.info("加载脚本[$index] $fileName 成功")
                updateConfig()
            }
        } catch (e: Exception) {
            logger.error(e.message)
        }
    }

    @SubCommand
    @Description("载入一个远程脚本，并且执行")
    fun ConsoleCommandSender.loadRemote(@Name("脚本URL") path: String) {
        try {
            launch {
                val index = manager.load(LuaSourceFactory.buildSource(URL(path)) as LuaSource)
                logger.info("加载脚本[$index] $path 成功")
                updateConfig()
            }
        } catch (e: Exception) {
            logger.error(e.message)
        }
    }

    @SubCommand
    @Description("执行一个已经载入的脚本（如果已经被执行过，则忽略）")
    fun ConsoleCommandSender.execute(@Name("脚本编号") scriptId: Int) {
        try {
            launch {
                manager.execute(scriptId)
                logger.info("执行脚本[$scriptId]成功")
                updateConfig()
            }
        } catch (e: Exception) {
            logger.error(e.message)
        }
    }

    @SubCommand
    @Description("重新执行一个脚本（该操作会先停用脚本并重新从文件中读取内容并执行）")
    fun ConsoleCommandSender.reload(@Name("脚本编号") scriptId: Int) {
        try {
            launch {
                manager.reload(scriptId)
                logger.info("重载脚本[$scriptId]成功")
                updateConfig()
            }
        } catch (e: Exception) {
            logger.error(e.message)
        }
    }

    @SubCommand
    @Description("停用一个脚本（该操作会停止脚本以及脚本内注册的所有事件监听器）")
    fun ConsoleCommandSender.stop(@Name("脚本编号") scriptId: Int) {
        try {
            launch {
                manager.stop(scriptId)
                logger.info("停用脚本[$scriptId]成功")
                updateConfig()
            }
        } catch (e: Exception) {
            logger.error(e.message)
        }
    }

    @SubCommand
    @Description("删除一个脚本（仅移出脚本列表，不删除文件）")
    fun ConsoleCommandSender.delete(@Name("脚本编号") scriptId: Int) {
        try {
            launch {
                manager.delete(scriptId)
                logger.info("删除脚本[$scriptId]成功")
                updateConfig()
            }
        } catch (e: Exception) {
            logger.error(e.message)
        }
    }


    @SubCommand
    @Description("查看脚本信息")
    fun ConsoleCommandSender.info(@Name("脚本编号") scriptId: Int) {
        try {
            val source = manager.getScript(scriptId).header
            logger.info("名称：${source["name"]}")
            logger.info("版本：${source["version"]}")
            logger.info("作者：${source["author"]}")
            logger.info("描述：${source["description"]}")
        } catch (e: Exception) {
            logger.error(e.message)
        }
    }

    suspend fun loadScripts() {
        loadScriptsByConfigFile()
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
                val index: Int = when (item["type"]?.jsonPrimitive?.contentOrNull) {
                    "file" -> {
                        val fileName = item["file"]?.jsonPrimitive?.contentOrNull!!
                        manager.add(LuaSourceFactory.buildSource(File(fileName)))
                    }
                    "content" -> {
                        val content = item["content"]?.jsonPrimitive?.contentOrNull!!
                        manager.add(LuaSourceFactory.buildSource(content) as LuaSource)
                    }
                    "url" -> {
                        val urlString = item["url"]?.jsonPrimitive?.contentOrNull!!
                        manager.add(LuaSourceFactory.buildSource(URL(urlString)) as LuaSource)
                    }
                    else -> throw IllegalArgumentException("Illegal script type.")
                }
                if (item["enable"]?.jsonPrimitive?.booleanOrNull == true) {
                    manager.getScript(index).load()
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
        if (configFile == null) return
        val configArray = buildJsonArray {
            manager.list().forEach { script ->
                add(buildJsonObject {
                    when (val source = script.source) {
                        is LuaSource.LuaFileSource -> {
                            this@buildJsonObject.put("type", "file")
                            this@buildJsonObject.put("file", source.file.path)
                        }
                        is LuaSource.LuaContentSource -> {
                            this@buildJsonObject.put("type", "content")
                            this@buildJsonObject.put("content", source.content)
                        }
                        is LuaSource.LuaURLSource -> {
                            this@buildJsonObject.put("type", "url")
                            this@buildJsonObject.put("url", source.url.toString())
                        }
                    }
                    this@buildJsonObject.put("enable", script.isLoaded)
                })
            }
        }
        configFile.writeText(jsonFormat.encodeToString(configArray))
    }
}