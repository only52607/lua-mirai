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

    private var _completed = false

    private var _stopped = false

    override val running: Boolean
        get() = _running

    override val completed: Boolean
        get() = _completed

    override val stopped: Boolean
        get() = _stopped

    override suspend fun start() {
        if (_completed) throw BotScriptException("Script is completed")
        if (_stopped) throw BotScriptException("Script is stopped")
        if (_running) throw BotScriptException("Script is running")
        onStart()
        _running = true
    }

    abstract suspend fun onStart()

    override suspend fun stop() {
        if (_completed) throw BotScriptException("Script is completed")
        if (_stopped) throw BotScriptException("Script is stopped")
        if (!_running) throw BotScriptException("Script is not running")
        onStop()
        _running = false
        _stopped = true
    }

    abstract suspend fun onStop()

    protected fun onScriptCompleted() {
        _completed = true
    }
}

class BotScriptException(message: String) : RuntimeException(message)