package com.github.only52607.luamirai.core

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.Closeable
import java.io.InputStream
import java.io.OutputStream

/**
 * Bot脚本接口，代表一个可以执行的脚本
 */
@Suppress("unused")
interface Script : Closeable, CoroutineScope {
    /**
     * 运行此脚本，进行注册各类监听器，启动线程等任务
     */
    fun start(): Job

    /**
     * 停止脚本，清除脚本内注册的监听器，清除脚本内启动的线程
     */
    fun stop(): Job

    /**
     * 脚本是否正在运行，当值为true时，代表脚本已经被运行，且脚本内注册的监听器处于活跃状态。
     */
    val isActive: Boolean

    /**
     * 脚本是否被停止
     */
    val isStopped: Boolean

    /**
     * 脚本语言
     */
    val lang: String

    /**
     * 脚本配置信息
     */
    val config: ScriptConfiguration?
        get() = null

    /**
     * 脚本源
     */
    val source: ScriptSource

    /**
     * 脚本标准输出流
     */
    var stdout: OutputStream?

    /**
     * 脚本错误输出流
     */
    var stderr: OutputStream?

    /**
     * 脚本标准输入流
     */
    var stdin: InputStream?

    /**
     * 关闭脚本
     */
    override fun close() {
        launch { stop() }
    }
}

open class ScriptException(message: String) : RuntimeException(message)

class ScriptAlreadyStoppedException(message: String = "Script has already stopped") : ScriptException(message)

class ScriptNotYetStartedException(message: String = "Script has not been started") : ScriptException(message)

class ScriptAlreadyStartedException(message: String = "Script has already stopped") : ScriptException(message)