package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.lua.lib.*
import kotlinx.coroutines.CompletableJob
import org.luaj.vm2.Globals
import org.luaj.vm2.LoadState
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.*
import org.luaj.vm2.lib.jse.*

class MiraiCoreGlobals : Globals(), EventListenerContainable {
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