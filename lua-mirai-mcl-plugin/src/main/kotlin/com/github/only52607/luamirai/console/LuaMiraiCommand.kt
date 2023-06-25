package com.github.only52607.luamirai.console

import com.github.only52607.luamirai.core.*
import com.github.only52607.luamirai.core.source.LMPKSource
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.ConsoleCommandSender
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.utils.MiraiLogger
import java.io.File
import java.util.UUID

@OptIn(
    ConsoleExperimentalApi::class,
    kotlinx.serialization.ExperimentalSerializationApi::class
)
@Suppress("UNUSED", "UnusedReceiverParameter")
class LuaMiraiCommand(
    private val logger: MiraiLogger,
    private val configFile: File
) : CompositeCommand(
    owner = LuaMiraiPlugin,
    primaryName = "lua",
    description = "lua mirai 插件相关指令"
) {
    private val json = Json { prettyPrint = true }
    private val scripts = mutableMapOf<String, Script>()
    private val sourceConfigs = mutableMapOf<Script, PluginConfig.ScriptSource>()
    lateinit var pluginConfig: PluginConfig
    private fun Script.tryStop() {
        try { 
            stop()
        } catch (_: ScriptAlreadyStoppedException) {
        } catch (_: ScriptNotYetStartedException) {
        }
    }
    
    private val Script.statusText
        get() = when {
            isActive -> "已启用"
            isStopped -> "已停用"
            else -> "未启用"
        }

    private fun checkScript(key: String): Script {
        return scripts[key] ?: throw IllegalArgumentException("脚本 $key 不存在")
    }

    private fun PluginConfig.ScriptSource.toScriptSource(): ScriptSource {
        return when(type) {
            "file" -> LMPKSource.fromZipFile(File(value))
            else -> throw Exception("Unknown source type $type")
        }
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

    @SubCommand("list")
    @Description("列出所有脚本")
    fun ConsoleCommandSender.list() {
        scripts.forEach { (key, script) ->
            logger.info("[$key] ${script.config?.getStringOrNull("name") ?: "未知脚本"} (${script.statusText})")
        }
    }

    @SubCommand("info")
    @Description("显示脚本详细信息")
    fun ConsoleCommandSender.info(@Name("脚本编号") key: String) {
        val script = checkScript(key)
        val name = script.config?.getStringOrNull("name") ?: ""
        val version = script.config?.getStringOrNull("version") ?: ""
        val author = script.config?.getStringOrNull("author") ?: ""
        val description = script.config?.getStringOrNull("description") ?: ""
        logger.info(
            "状态：${script.statusText}\n名称：$name\n版本：$version\n作者：$author\n描述：$description"
        )
    }
    
    @SubCommand("stop")
    @Description("停止脚本")
    fun ConsoleCommandSender.stop(@Name("脚本编号") key: String) {
        checkScript(key).tryStop()
    }

    @SubCommand("remove")
    @Description("移除脚本")
    fun ConsoleCommandSender.remove(@Name("脚本编号") key: String) {
        checkScript(key).tryStop()
        scripts.remove(key)
    }

    @SubCommand("start")
    @Description("启动脚本")
    fun ConsoleCommandSender.start(@Name("脚本编号") key: String) {
        checkScript(key).start()
    }

    @SubCommand("update")
    @Description("更新脚本，如果脚本正在运行将会重启脚本")
    suspend fun ConsoleCommandSender.update(@Name("脚本编号") key: String) {
        val script = checkScript(key)
        val sourceConfig = sourceConfigs[script]!!
        val running = script.isActive
        script.tryStop()
        val newScript = sourceConfig.toScriptSource().load()
        scripts.remove(key)
        sourceConfigs.remove(script)
        scripts[key] = newScript
        sourceConfigs[newScript] = sourceConfig
        if (running) {
            newScript.start()
        }
    }

    private suspend fun loadScripts() {
        if (!configFile.exists()) return
        pluginConfig = json.decodeFromStream(PluginConfig.serializer(), configFile.inputStream())
        pluginConfig.sources.forEach { c ->
            val script = c.toScriptSource().load()
            val id = UUID.randomUUID().toString()
            scripts[id] = script
            sourceConfigs[script] = c
            if (c.autoStart) {
                script.start()
            }
        }
    }

    private fun updateConfig() {
        json.encodeToStream(pluginConfig, configFile.outputStream())
    }

    fun enable() {
        runBlocking {
            loadScripts()
        }
    }

    fun disable() {
        scripts.values.forEach { if (it.isActive) it.stop() }
    }
}