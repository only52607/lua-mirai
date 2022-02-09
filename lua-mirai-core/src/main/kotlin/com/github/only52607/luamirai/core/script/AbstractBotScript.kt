package com.github.only52607.luamirai.core.script

import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

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

    override fun start(): Job {
        if (_stopped) throw BotScriptException("Stopped script could not be start again")
        if (_active) throw BotScriptException("Script has already been started")
        val job = launch {
            onStart()
        }
        _active = true
        return job
    }

    abstract suspend fun onStart()

    override fun stop(): Job {
        if (_stopped) throw BotScriptException("Script has already stopped")
        if (!_active) throw BotScriptException("Script has not been started")
        val job = launch {
            onStop()
        }
        cancel()
        _active = false
        _stopped = true
        return job
    }

    abstract suspend fun onStop()
}

class BotScriptException(message: String) : RuntimeException(message)