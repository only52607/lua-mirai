package com.ooooonly.luaMirai

/**
 * ClassName: AbstractBotScript
 * Description:
 * date: 2021/7/23 21:17
 * @author ooooonly
 * @version
 */
abstract class AbstractBotScript: BotScript {

    private var isLoaderInternal = false

    override val isLoaded: Boolean
        get() = isLoaderInternal

    fun create() = onCreate()

    protected open fun onCreate() {}

    override fun stop() {
        onStop()
        isLoaderInternal = false
    }

    protected open fun onStop() {}

    override fun destroy() {
        stop()
        onDestroy()
    }

    protected open fun onDestroy() {}

    override fun load() {
        onLoad()
        isLoaderInternal = true
    }

    protected open fun onLoad() {}

    override fun reload() {
        onReload()
    }

    protected open fun onReload() {
        destroy()
        load()
    }
}