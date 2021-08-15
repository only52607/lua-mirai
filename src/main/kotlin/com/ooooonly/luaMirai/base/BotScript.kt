package com.ooooonly.luaMirai.base

@Suppress("unused")
interface BotScript {
    fun stop()

    fun destroy()

    fun load()

    fun reload()

    val isLoaded: Boolean

    val info: BotScriptInfo?
}

data class BotScriptInfo (
    var name: String = "",
    var version: String = "",
    var author: String = "",
    var description: String = "",
    var usage: String = "",
    var file: String = ""
)
