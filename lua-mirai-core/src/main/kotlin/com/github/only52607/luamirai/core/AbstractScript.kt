package com.github.only52607.luamirai.core

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
abstract class AbstractScript : Script {

    private var _active = false

    private var _stopped = false

    override val isActive: Boolean
        get() = _active

    override val isStopped: Boolean
        get() = _stopped

    override fun start(): Job {
        if (_stopped) throw ScriptAlreadyStoppedException("Stopped script could not be start again")
        if (_active) throw ScriptAlreadyStartedException()
        val job = launch {
            onStart()
        }
        _active = true
        return job
    }

    abstract suspend fun onStart()

    override fun stop(): Job {
        if (_stopped) throw ScriptAlreadyStoppedException()
        if (!_active) throw ScriptNotYetStartedException()
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