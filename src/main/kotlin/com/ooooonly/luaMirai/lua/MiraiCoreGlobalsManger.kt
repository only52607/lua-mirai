package com.ooooonly.luaMirai.lua

import com.ooooonly.luakt.asKValue
import org.luaj.vm2.LuaClosure

object MiraiCoreGlobalsManger {
    private var globalsId = 0
    private var instances = mutableMapOf<Int, MiraiCoreGlobals>()
    private const val GLOBALS_ID = "_GLOBALS_ID"

    fun checkGlobalsId(cls: LuaClosure): Int {
        val upvalues = cls.upValues
        if (upvalues.isEmpty()) return -1
        return upvalues[0].value[GLOBALS_ID].asKValue(-1)
    }

    fun checkGlobals(cls: LuaClosure): MiraiCoreGlobals? {
        val id = checkGlobalsId(cls)
        if (id == -1) return null
        return getInstance(id)
    }

    fun getInstance(id: Int = globalsId++): MiraiCoreGlobals =
        instances[id] ?: MiraiCoreGlobals().apply {
            instances[id] = this
            set(GLOBALS_ID, id)
        }

    fun removeInstance(id: Int) {
        instances[id]?.let {
            it.clearListeners()
            instances.remove(id)
        }
    }
}