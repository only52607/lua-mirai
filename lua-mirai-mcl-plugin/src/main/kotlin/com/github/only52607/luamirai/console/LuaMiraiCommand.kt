package com.github.only52607.luamirai.console

import com.github.only52607.luamirai.configuration.ConfigurableScriptSource
import com.github.only52607.luamirai.console.config.PluginConfig
import com.github.only52607.luamirai.core.BotScriptBuilder
import com.github.only52607.luamirai.core.script.*
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
    data class RunningScript(
        var instance: BotScript,
        val builder: BotScriptBuilder
    )

    private val runningScripts = mutableListOf<RunningScript>()
    private val builders = mutableListOf<BotScriptBuilder>()

    private val BotScriptHeader.name: String?
        get() = get("name")

    private val BotScriptHeader.version: String?
        get() = get("version")

    private val BotScriptHeader.author: String?
        get() = get("author")

    private val BotScriptHeader.description: String?
        get() = get("description")

    private val BotScriptHeader.simpleInfo: String
        get() = "名称：$name\n版本：$version\n作者：$author\n描述：$description"

    fun enable() {
        runBlocking { initConfig() }
    }

    fun disable() {
        runningScripts.forEach { if (it.instance.isActive) it.instance.stop() }
    }

    private fun parseSourceLang(sourcePath: String): String {
        if (sourcePath.endsWith(".lua") || sourcePath.endsWith(".lmpk")) {
            return "lua"
        }
        if (sourcePath.endsWith(".js")) {
            return "js"
        }
        throw Exception("Unknown source lang")
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
        builders.forEachIndexed { index, builder ->
            logger.info("[$index] ${builder.botScriptSource}")
        }
    }

    @SubCommand("source info")
    @Description("查看脚本源信息")
    suspend fun ConsoleCommandSender.sourceInfo(@Name("脚本源编号") sourceId: Int) {
        val header = builders[sourceId].readHeader()
        logger.info("\n" + header.simpleInfo)
    }

    @SubCommand("source update")
    @Description("更新脚本源")
    fun ConsoleCommandSender.updateSource(@Name("脚本源编号") sourceId: Int) {

    }

    @SubCommand("source add")
    @Description("新增脚本源")
    suspend fun ConsoleCommandSender.add(@Name("文件名或URL") fileName: String) {
        val source = if (!fileName.contains("://")) {
            val file = File(fileName)
            if (!file.exists()) {
                logger.error("文件${file.absolutePath}不存在")
                return
            }
            BotScriptSource.FileSource(file, parseSourceLang(file.absolutePath))
        } else {
            BotScriptSource.URLSource(URL(fileName), parseSourceLang(fileName))
        }
        val builder = BotScriptBuilder.fromSource(ConfigurableScriptSource(source))
        builders.add(builder)
        logger.info("添加脚本源[${builders.size - 1}] $fileName 成功")
        logger.info("脚本信息：\n" + builder.readHeader().simpleInfo)
        updateConfig()
    }

    @SubCommand("source remove")
    @Description("删除指定位置上的脚本源")
    fun ConsoleCommandSender.remove(@Name("索引") index: Int) {
        builders.removeAt(index)
        updateConfig()
    }

    @SubCommand("source autostart")
    @Description("设置脚本源自动启动")
    fun ConsoleCommandSender.autostart(@Name("索引") index: Int, @Name("是否开启") autostart: Boolean) {
        (builders[index].botScriptSource as ConfigurableScriptSource).autoStart = autostart
        updateConfig()
    }

    @SubCommand("source alias")
    @Description("设置脚本源别名")
    fun ConsoleCommandSender.alias(@Name("索引") index: Int, @Name("别名") alias: String) {
        (builders[index].botScriptSource as ConfigurableScriptSource).alias = alias
        updateConfig()
    }

    // Script Commands

    @SubCommand("script list")
    @Description("列出运行中的脚本")
    fun ConsoleCommandSender.listScript() {
        runningScripts.forEachIndexed { index, runningScript ->
            logger.info("[$index] ${runningScript.instance}")
        }
    }

    @SubCommand("script stop")
    @Description("停用一个运行中的脚本（该操作会停止脚本以及脚本内注册的所有事件监听器）")
    fun ConsoleCommandSender.stop(@Name("脚本编号") scriptId: Int) {
        try {
            runningScripts[scriptId].instance.stop()
        } catch (_: ScriptAlreadyStoppedException) {
            logger.error("脚本[${runningScripts[scriptId]}]已经停用，请勿重复操作")
            return
        } catch (_: ScriptNotYetStartedException) {
            logger.error("脚本[${runningScripts[scriptId]}]没有被启动，无需停止")
            return
        }
        logger.info("停用脚本[${runningScripts[scriptId]}]成功")
        runningScripts.removeAt(scriptId)
    }

    @SubCommand("script start", "source start")
    @Description("使用脚本源启动一个新脚本")
    suspend fun ConsoleCommandSender.start(@Name("脚本源编号") sourceId: Int) {
        val script = builders[sourceId].buildInstance()
        runningScripts.add(
            RunningScript(script, builders[sourceId])
        )
        script.start()
    }

    @SubCommand("script restart")
    @Description("重新读入脚本源以启动脚本")
    suspend fun ConsoleCommandSender.restart(@Name("脚本编号") scriptId: Int) {
        val runningScript = runningScripts[scriptId]
        try {
            runningScript.instance.stop()
        } catch (_: ScriptAlreadyStoppedException) {
        } catch (_: ScriptNotYetStartedException) {
        }
        runningScript.instance = runningScript.builder.buildInstance()
        runningScript.instance.start()
    }

    @SubCommand("script info")
    @Description("查看运行中的脚本信息")
    fun ConsoleCommandSender.info(@Name("脚本编号") scriptId: Int) {
        val header = runningScripts[scriptId].instance.header ?: return
        logger.info("\n" + header.simpleInfo)
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
        builders.clear()
        builders.addAll(pluginConfig.sources.map { BotScriptBuilder.fromSource(it) })
        for (source in pluginConfig.sources) {
            if (!source.autoStart) continue
            try {
                val builder = BotScriptBuilder.fromSource(source)
                val runningScript = RunningScript(builder.buildInstance(), builder)
                runningScripts.add(runningScript)
                runningScript.instance.start()
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
            sources = builders.map { it.botScriptSource as ConfigurableScriptSource }
        )
        json.encodeToStream(pluginConfig, configFile.outputStream())
    }
}