package com.github.only52607.luamirai.console

import com.github.only52607.luamirai.core.*
import com.github.only52607.luamirai.core.source.LMPKSource
import com.github.only52607.luamirai.core.source.TextFileSource
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.json.encodeToStream
import net.mamoe.mirai.console.command.CompositeCommand
import net.mamoe.mirai.console.command.ConsoleCommandSender
import net.mamoe.mirai.console.util.ConsoleExperimentalApi
import net.mamoe.mirai.utils.MiraiLogger
import java.io.File
import java.util.*
import kotlin.reflect.KClass

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
    companion object {
        private val json = Json { prettyPrint = true }

        // ServiceLoader在console环境下搜索不到依赖，需要手动添加provider
        @Suppress("unchecked_cast")
        private val providers = listOf(
            Class.forName("com.github.only52607.luamirai.lua.LuaScriptProvider").kotlin as KClass<ScriptProvider>
        )
    }

    class Item private constructor(
        var config: PluginConfig.ScriptSource
    ) {
        companion object {
            private fun PluginConfig.ScriptSource.toSource(): ScriptSource {
                return when (type) {
                    PluginConfig.SOURCE_TYPE_DIRECTORY -> LMPKSource.fromDirectory(File(value))
                    PluginConfig.SOURCE_TYPE_LMPK -> LMPKSource.fromZipFile(File(value))
                    PluginConfig.SOURCE_TYPE_FILE -> TextFileSource(File(value))
                    else -> throw Exception("Unknown source type $type")
                }
            }

            suspend fun load(config: PluginConfig.ScriptSource): Item {
                val item = Item(config)
                item.script = config.toSource().load(providers)
                if (config.autoStart) item.tryStart()
                return item
            }
        }

        private lateinit var script: Script

        private val Script.name
            get() = config?.getStringOrNull("name") ?: "未知脚本"

        private val Script.statusText
            get() = when {
                isActive -> "已启用"
                isStopped -> "已停用"
                else -> "未启用"
            }

        suspend fun update() {
            val activated = script.isActive
            forceStop()
            script = config.toSource().load(providers)
            if (activated || config.autoStart) tryStart()
        }

        fun tryStart() {
            script.start()
        }

        fun forceStop() {
            try {
                script.stop()
            } catch (_: ScriptAlreadyStoppedException) {
            } catch (_: ScriptNotYetStartedException) {
            }
        }

        fun getInfo(): String {
            val name = script.config?.getStringOrNull("name") ?: ""
            val version = script.config?.getStringOrNull("version") ?: ""
            val author = script.config?.getStringOrNull("author") ?: ""
            val description = script.config?.getStringOrNull("description") ?: ""
            return "状态：${script.statusText}\n名称：${name}\n版本：$version\n作者：$author\n描述：$description"
        }

        override fun toString(): String {
            return "${script.name} (${script.statusText})"
        }
    }

    private val items = LinkedHashMap<String, Item>()

    private lateinit var pluginConfig: PluginConfig

    private fun writeConfig() {
        pluginConfig = pluginConfig.copy(
            sources = items.values.map { it.config }
        )
        json.encodeToStream(pluginConfig, configFile.outputStream())
    }

    private fun generateItemID(): String {
        var id: String
        do {
            id = UUID.randomUUID().toString().replace("-", "").substring(0, 8)
        } while (items.containsKey(id))
        return id
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
        logger.info("已载入${items.size}个脚本")
        items.forEach { (key, script) -> logger.info("[$key] $script") }
    }

    @SubCommand("add")
    @Description("添加一个脚本")
    suspend fun ConsoleCommandSender.add(@Name("脚本路径") path: String) {
        val f = File(path)
        if (!f.exists()) return logger.error("文件${f.absolutePath}不存在")
        val id = generateItemID()
        val type = when {
            f.isDirectory -> PluginConfig.SOURCE_TYPE_DIRECTORY
            f.name.endsWith(".zip") || f.name.endsWith(".lmpk") -> PluginConfig.SOURCE_TYPE_LMPK
            else -> PluginConfig.SOURCE_TYPE_FILE
        }
        val item = Item.load(PluginConfig.ScriptSource(type = type, value = path))
        items[id] = item
        logger.info("已添加[$id] $item")
        writeConfig()
    }

    @SubCommand("info")
    @Description("显示脚本详细信息")
    fun ConsoleCommandSender.info(@Name("脚本编号") key: String) {
        val item = items[key] ?: return logger.error("脚本 $key 不存在")
        logger.info("脚本[$key]\n" + item.getInfo())
    }

    @SubCommand("stop")
    @Description("停止脚本")
    fun ConsoleCommandSender.stop(@Name("脚本编号") key: String) {
        val item = items[key] ?: return logger.error("脚本 $key 不存在")
        item.forceStop()
    }

    @SubCommand("remove")
    @Description("移除脚本")
    fun ConsoleCommandSender.remove(@Name("脚本编号") key: String) {
        val item = items[key] ?: return logger.error("脚本 $key 不存在")
        item.forceStop()
        items.remove(key)
        writeConfig()
    }

    @SubCommand("start")
    @Description("启动脚本")
    fun ConsoleCommandSender.start(@Name("脚本编号") key: String) {
        val item = items[key] ?: return logger.error("脚本 $key 不存在")
        item.tryStart()
    }

    @SubCommand("autostart")
    @Description("设置脚本为自启动")
    fun ConsoleCommandSender.setAutoStart(@Name("脚本编号") key: String, @Name("是否自启动") value: Boolean = true) {
        val item = items[key] ?: return logger.error("脚本 $key 不存在")
        item.config = item.config.copy(autoStart = value)
        writeConfig()
    }

    @SubCommand("update")
    @Description("更新脚本，如果脚本正在运行将会重启脚本")
    suspend fun ConsoleCommandSender.update(@Name("脚本编号") key: String) {
        val item = items[key] ?: return logger.error("脚本 $key 不存在")
        item.update()
    }

    fun enable() = runBlocking {
        pluginConfig = if (configFile.exists())
            json.decodeFromStream(PluginConfig.serializer(), configFile.inputStream())
        else
            PluginConfig(mutableListOf())
        pluginConfig.sources.forEach { c ->
            try {
                items[generateItemID()] = Item.load(c)
            } catch (e: Throwable) {
                logger.error("加载脚本${c.value}时发生错误", e)
            }
        }
        if (items.isNotEmpty()) logger.info("${items.size}个脚本已载入")
    }

    fun disable() {
        items.forEach { (_, v) -> v.forceStop() }
    }
}