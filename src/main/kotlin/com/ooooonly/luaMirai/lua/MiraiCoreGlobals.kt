package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.lua.lib.*
import com.ooooonly.luakt.asKValue
import kotlinx.coroutines.CompletableJob
import org.luaj.vm2.Globals
import org.luaj.vm2.LoadState
import org.luaj.vm2.LuaClosure
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.*
import org.luaj.vm2.lib.jse.*

class MiraiCoreGlobals(val id: Int = scriptId++) : Globals(), EventListenerContainable {
    companion object {
        private var scriptId = 0
        private var instances = mutableMapOf<Int, MiraiCoreGlobals>()
        const val SCRIPT_ID = "_SCRIPT_ID"

        fun checkScriptId(cls: LuaClosure): Int {
            val upvalues = cls.upValues
            if (upvalues.isEmpty()) return -1
            return upvalues[0].value[SCRIPT_ID].asKValue(-1)
        }

        fun checkGlobals(cls: LuaClosure): MiraiCoreGlobals? {
            val id = checkScriptId(cls)
            if (id == -1) return null
            return getInstance(id)
        }

        fun getInstance(id: Int = scriptId++): MiraiCoreGlobals {
            if (!instances.containsKey(id)) instances[id] = MiraiCoreGlobals(id)
            return instances[id]!!
        }

        fun removeInstance(id: Int) {
            instances[id]?.let {
                it.clearListeners()
                instances.remove(id)
            }
        }
    }

    init {
        load(JseBaseLib())
        load(PackageLib())
        load(Bit32Lib())
        load(TableLib())
        load(StringLib())
        load(CoroutineLib())
        load(JseMathLib())
        load(JseIoLib())
        load(JseOsLib())
        load(LuajavaLib())

        load(MiraiCoreLib())
        load(LuaJavaExLib())
        load(ThreadExLib())
        load(HttpLib())
        load(JsonLib())

        LoadState.install(this)
        LuaC.install(this)

        set(SCRIPT_ID, id)
    }

    private val botEventListeners = mutableListOf<CompletableJob>()
    override fun addListener(job: CompletableJob) {
        botEventListeners.add(job)
    }

    override fun clearListeners() = botEventListeners.run {
        forEach { it.complete() }
        clear()
    }
}