package com.ooooonly.luaMirai.lua

import com.ooooonly.luakt.asKValue
import kotlinx.atomicfu.atomic
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaClosure
import org.luaj.vm2.LuaError

var LuaClosure.globalsId: Int
    get() =
        if (upValues.isEmpty()) throw LuaError("Upvalue of closure is empty!")
        else upValues[0].value[ExtendGlobals.GLOBALS_ID].asKValue()
    set(value) {
        upValues[0].value[ExtendGlobals.GLOBALS_ID] = value
    }
val LuaClosure.globals: ExtendGlobals?
    get() {
        return ExtendGlobals.instancesMap[this.globalsId]
    }

open class ExtendGlobals : Globals() {
    companion object {
        private val idGlobal = atomic(0)
        internal val instancesMap = mutableMapOf<Int, ExtendGlobals>()
        internal const val GLOBALS_ID = "_GLOBALS_ID"
    }

    protected val id = idGlobal.getAndIncrement()

    init {
        instancesMap[id] = this
        set(GLOBALS_ID, id)
    }

    protected val info = mutableMapOf<String, Any>()
    open fun destroy() {
        instancesMap.remove(id)
    }
}