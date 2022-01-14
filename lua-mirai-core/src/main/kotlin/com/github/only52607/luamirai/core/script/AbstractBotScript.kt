package com.github.only52607.luamirai.core.script

/**
 * ClassName: AbstractBotScript
 * Description:
 * date: 2021/7/23 21:17
 * @author ooooonly
 * @version
 */
abstract class AbstractBotScript : BotScript {

    private var _running = false

    override val running: Boolean
        get() = _running

    override suspend fun start() {
        if (_running) throw BotScriptLoadedException("Script is running")
        onStart()
        _running = true
    }

    protected open suspend fun onStart() {}

    override suspend fun stop() {
        if (!_running) throw BotScriptLoadedException("Script is not running")
        onStop()
        _running = false
    }

    protected open suspend fun onStop() {}

    override suspend fun restart() {
        onRestart()
    }

    protected open suspend fun onRestart() {
        stop()
        start()
    }
}

class BotScriptLoadedException(message: String): RuntimeException(message)