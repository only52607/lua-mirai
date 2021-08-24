package com.ooooonly.luaMirai.base

/**
 * Bot脚本接口，代表一个可以执行的脚本实体
 */
@Suppress("unused")
interface BotScript {

    /**
     * 创建脚本，读取脚本内容，读取脚本头部，初始化资源的操作在这里执行
     */
    fun create()

    /**
     * 运行此脚本，进行注册各类监听器，启动线程等任务
     */
    fun load()

    /**
     * 停止脚本，清除脚本内注册的监听器，清除脚本内启动的线程
     */
    fun stop()

    /**
     * 销毁脚本，释放资源
     */
    fun destroy()

    /**
     * 重启脚本，相当于stop后load
     */
    fun reload()

    /**
     * 脚本是否可用，当值为true时，代表脚本已经被运行，且脚本内注册的监听器处于活跃状态。
     */
    val isLoaded: Boolean

    /**
     * 脚本头部的附带信息
     */
    val header: BotScriptHeader

    /**
     * 脚本源
     */
    val source: BotScriptSource?
}

interface BotScriptHeader: Map<String, String>

interface MutableBotScriptHeader: MutableMap<String, String>, BotScriptHeader

internal class MutableBotScriptHeaderImpl: MutableBotScriptHeader, MutableMap<String, String> by mutableMapOf()

fun mutableBotScriptHeaderOf(builder: MutableBotScriptHeader.() -> Unit = {}):MutableBotScriptHeader =
    MutableBotScriptHeaderImpl().apply(builder)