package com.ooooonly.luaMirai.base

/**
 * ClassName: AbstractBotScript
 * Description:
 * date: 2021/7/23 21:17
 * @author ooooonly
 * @version
 */
abstract class AbstractBotScript : BotScript {

    private var isLoaderInternal = false

    override val isLoaded: Boolean
        get() = isLoaderInternal

    override suspend fun load() {
        if (isLoaded) throw BotScriptLoadedException("Script has been loaded.")
        onLoad()
        isLoaderInternal = true
    }

    protected open suspend fun onLoad() {}

    override suspend fun stop() {
        onStop()
        isLoaderInternal = false
    }

    protected open suspend fun onStop() {}

    override suspend fun reload() {
        onReload()
    }

    protected open suspend fun onReload() {
        stop()
        load()
    }
}

class BotScriptLoadedException(message: String): RuntimeException(message)