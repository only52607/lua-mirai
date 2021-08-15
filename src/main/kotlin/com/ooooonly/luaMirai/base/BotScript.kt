package com.ooooonly.luaMirai.base

@Suppress("unused")
interface BotScript<T> {
    fun stop()

    fun destroy()

    fun load()

    fun reload()

    val isLoaded: Boolean

    val info: T?
}
