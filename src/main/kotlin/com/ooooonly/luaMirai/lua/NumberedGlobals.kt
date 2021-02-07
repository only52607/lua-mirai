package com.ooooonly.luaMirai.lua

import kotlinx.atomicfu.atomic
import org.luaj.vm2.Globals

//var LuaClosure.globalsId: Int
//    get() =
//        if (upValues.isEmpty()) throw LuaError("Upvalue of closure is empty!")
//        else upValues[0].value[ExtendGlobals.GLOBALS_ID].asKValue()
//    set(value) {
//        upValues[0].value[ExtendGlobals.GLOBALS_ID] = value
//    }
//val LuaClosure.globals: ExtendGlobals?
//    get() {
//        return ExtendGlobals.instancesMap[this.globalsId]
//    }

open class NumberedGlobals(val id: Long = idAtomic.getAndIncrement()) : Globals() {
    companion object {
        private val idAtomic = atomic(0L)
        internal const val GLOBALS_ID = "_GLOBALS_ID"
    }
}