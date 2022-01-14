package com.github.only52607.luamirai.core.manager

import com.github.only52607.luamirai.core.script.BotScript
import java.io.File
import java.io.InputStream
import java.net.URL

interface IBotScriptManager {
    /**
     * 列出所有脚本
     */
    fun list(): List<BotScript>

    /**
     * 获取指定位置脚本
     */
    fun get(index: Int): BotScript

    /**
     * 是否正在运行
     */
    fun isRunning(index: Int): Boolean = get(index).running

    /**
     * 读入脚本
     * 返回脚本索引
     */
    suspend fun add(scriptLang: String, file: File, chunkName: String): Int

    suspend fun add(scriptLang: String, file: File): Int

    suspend fun add(scriptLang: String, url: URL, chunkName: String): Int

    suspend fun add(scriptLang: String, url: URL): Int

    suspend fun add(scriptLang: String, content: String, chunkName: String): Int

    suspend fun add(scriptLang: String, content: String): Int = add(content, content)

    suspend fun add(scriptLang: String, inputStream: InputStream, chunkName: String): Int

    suspend fun add(scriptLang: String, inputStream: InputStream): Int

    /**
     * 加载指定位置脚本
     */
    suspend fun start(scriptId: Int)

    /**
     * 重载指定位置脚本
     */
    suspend fun restart(scriptId: Int)

    /**
     * 停止指定位置脚本
     */
    suspend fun stop(scriptId: Int)

    /**
     * 删除指定位置脚本
     */
    suspend fun remove(scriptId: Int)

    /**
     * 停止所有脚本
     */
    suspend fun stopAll()

    /**
     * 重载所有脚本
     */
    suspend fun restartAll()

    /**
     * 删除所有脚本
     */
    suspend fun removeAll()
}