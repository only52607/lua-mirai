package com.ooooonly.luaMirai.base

interface BotScriptManager<Script : BotScript<Info>, Info, Source> {
    /**
     * 列出所有脚本
     */
    fun list(): List<Script>

    /**
     * 获取指定位置脚本
     */
    fun getScript(index: Int): Script

    /**
     * 读入脚本
     * 返回脚本索引
     */
    fun add(source: Source): Int

    /**
     * 读入并加载脚本
     * 返回脚本索引
     */
    fun load(source: Source): Int

    /**
     * 加载指定位置脚本
     */
    fun execute(scriptId: Int)

    /**
     * 重载指定位置脚本
     */
    fun reload(scriptId: Int)

    /**
     * 停止指定位置脚本
     */
    fun stop(scriptId: Int)

    /**
     * 删除指定位置脚本
     */
    fun delete(scriptId: Int)

    /**
     * 停止所有脚本
     */
    fun stopAll()

    /**
     * 重载所有脚本
     */
    fun reloadAll()

    /**
     * 删除所有脚本
     */
    fun deleteAll()
}