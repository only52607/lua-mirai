package com.github.only52607.luamirai.core.script

/**
 * ClassName: AbstractBotScript
 * Description:
 * date: 2021/7/23 21:17
 * @author ooooonly
 * @version
 */
abstract class AbstractBotScript : BotScript {

    private var _active = false

    private var _stopped = false

    override val isActive: Boolean
        get() = _active

    override val isStopped: Boolean
        get() = _stopped

    override suspend fun start() {
        if (_stopped) throw BotScriptException("Stopped script could not be start again")
        if (_active) throw BotScriptException("Script has already been started")
        onStart()
        _active = true
    }

    abstract suspend fun onStart()

    override suspend fun stop() {
        if (_stopped) throw BotScriptException("Script has already stopped")
        if (!_active) throw BotScriptException("Script has not been started")
        onStop()
        _active = false
        _stopped = true
    }

    abstract suspend fun onStop()
}

class BotScriptException(message: String) : RuntimeException(message)