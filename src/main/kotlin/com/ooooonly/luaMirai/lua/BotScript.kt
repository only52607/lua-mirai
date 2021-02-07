package com.ooooonly.luaMirai.lua

interface BotScript {
    fun create() = onCreate()
    fun onCreate() {}

    fun stop() = onStop()
    fun onStop() {}

    fun destroy() = onDestroy()
    fun onDestroy() {}

    fun load() = onLoad()
    fun onLoad() {}

    fun getInfo(): Info? = null

    data class Info(
        val name: String = "",
        val version: String = "",
        val author: String = "",
        val description: String = "",
        val usage: String = "",
        val file: String = ""
    )
}

