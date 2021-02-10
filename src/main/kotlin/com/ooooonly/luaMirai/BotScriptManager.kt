package com.ooooonly.luaMirai

interface BotScriptManager {
    fun list(): List<BotScript>
    fun add(fileName: String)
    fun load(fileName: String)
    fun execute(scriptId: Int)
    fun reload(scriptId: Int)
    fun stop(scriptId: Int)
    fun delete(scriptId: Int)
    fun stopAll()
}