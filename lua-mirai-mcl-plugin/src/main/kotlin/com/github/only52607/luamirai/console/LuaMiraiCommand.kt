package com.github.only52607.luamirai.console

import com.github.only52607.luamirai.configuration.ConfigurableScriptSource
import com.github.only52607.luamirai.console.config.PluginConfig
import com.github.only52607.luamirai.core.integration.BotScriptList
import com.github.only52607.luamirai.core.script.BotScriptSource
import com.github.only52607.luamirai.core.script.ScriptAlreadyStoppedException
import com.github.only52607.luamirai.core.script.ScriptNotYetStartedException
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.ConsoleCommandSender
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.utils.MiraiLogger
import java.io.File
import java.net.URL

private const val LUA = "lua"

@OptIn(
    ConsoleExperimentalApi::class,
    kotlinx.serialization.ExperimentalSerializationApi::class
)
@Suppress("UNUSED")
class LuaMiraiCommand(
    private val logger: MiraiLogger,
    private val configFile: File?
) : CompositeCommand(
    owner = LuaMiraiPlugin,
    primaryName = "lua",
    description = "lua mirai 插件相关指令"
) {
    private val scriptList = BotScriptList()
    private val sourceList = mutableListOf<ConfigurableScriptSource>()

    fun enable() {
        runBlocking { initConfig() }
    }

    fun disable() {
        scriptList.forEach { if (it.isActive) it.stop() }
    }

    @SubCommand("doc")
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
    fun ConsoleCommandSender.add(@Name("文件名或URL") fileName: String) {
        val source = if (!fileName.contains("://")) {
            val file = File(fileName)
            if (!file.exists()) {
                logger.error("文件${file.absolutePath}不存在")
                return
            }
            BotScriptSource.FileSource(file, LUA)
        } else {
            BotScriptSource.URLSource(URL(fileName), LUA)
        }
        sourceList.add(ConfigurableScriptSource(source))
        logger.info("添加脚本源[${sourceList.size - 1}] $fileName 成功")
        updateConfig()
    }

    @SubCommand("source remove")
    @Description("删除指定位置上的脚本源")
    fun ConsoleCommandSender.remove(@Name("索引") index: Int) {
        sourceList.removeAt(index)
        updateConfig()
    }

    @SubCommand("source autostart")
    @Description("设置脚本源自动启动")
    fun ConsoleCommandSender.autostart(@Name("索引") index: Int, @Name("是否开启") autostart: Boolean) {
        sourceList[index].autoStart = autostart
        updateConfig()
    }

    @SubCommand("source alias")
    @Description("设置脚本源别名")
    fun ConsoleCommandSender.alias(@Name("索引") index: Int, @Name("别名") alias: String) {
        sourceList[index].alias = alias
        updateConfig()
    }

    // Script Commands

    @SubCommand("script list")
    @Description("列出运行中的脚本")
    fun ConsoleCommandSender.listScript() {
        scriptList.forEachIndexed { index, botScript ->
            logger.info("[$index] $botScript")
        }
    }

    @SubCommand("script stop")
    @Description("停用一个运行中的脚本（该操作会停止脚本以及脚本内注册的所有事件监听器）")
    fun ConsoleCommandSender.stop(@Name("脚本编号") scriptId: Int) {
        try {
            scriptList[scriptId].stop()
        } catch (_: ScriptAlreadyStoppedException) {
            logger.error("脚本[${scriptList[scriptId]}]已经停用，请勿重复操作")
            return
        } catch (_: ScriptNotYetStartedException) {
            logger.error("脚本[${scriptList[scriptId]}]没有被启动，无需停止")
            return
        }
        logger.info("停用脚本[${scriptList[scriptId]}]成功")
        scriptList.removeAt(scriptId)
    }

    @SubCommand("script start", "source start")
    @Description("使用脚本源启动一个新脚本")
    suspend fun ConsoleCommandSender.start(@Name("脚本源编号") sourceId: Int) {
        scriptList.addFromSource(sourceList[sourceId]).start()
    }

    @SubCommand("script restart")
    @Description("重新读入脚本源以启动脚本")
    suspend fun ConsoleCommandSender.restart(@Name("脚本编号") scriptId: Int) {
        val source = scriptList[scriptId].source
        try {
            scriptList[scriptId].stop()
        } catch (_: ScriptAlreadyStoppedException) {
        } catch (_: ScriptNotYetStartedException) {
        }
        scriptList.removeAt(scriptId)
        scriptList.addFromSource(source).start()
    }

    @SubCommand("script info")
    @Description("查看运行中的脚本信息")
    fun ConsoleCommandSender.info(@Name("脚本编号") scriptId: Int) {
        val source = scriptList[scriptId].header ?: return
        logger.info("名称：${source["name"]}")
        logger.info("版本：${source["version"]}")
        logger.info("作者：${source["author"]}")
        logger.info("描述：${source["description"]}")
    }

    private val json by lazy {
        Json { prettyPrint = true }
    }

    /**
     * 从配置文件读取已加载脚本信息
     */
    private suspend fun initConfig() {
        if (configFile == null || !configFile.exists()) return
        val pluginConfig = json.decodeFromStream(PluginConfig.serializer(), configFile.inputStream())
        sourceList.clear()
        sourceList.addAll(pluginConfig.sources)
        for (source in pluginConfig.sources) {
            if (!source.autoStart) continue
            try {
                val script = scriptList.addFromSource(source)
                script.start()
                logger.info("$source 自动启动成功")
            } catch (e: Exception) {
                logger.error("$source 自动启动失败", e)
            }
        }
    }

    /**
     * 写出脚本信息到配置文件
     */
    private fun updateConfig() {
        configFile ?: return
        val pluginConfig = PluginConfig(
            sources = sourceList
        )
        json.encodeToStream(pluginConfig, configFile.outputStream())
    }
}