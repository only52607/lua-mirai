package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.lua.bridge.coreimpl.BotCoreImpl
import com.ooooonly.luaMirai.lua.lib.*
import com.ooooonly.luakt.invoke
import kotlinx.coroutines.CompletableJob
import net.mamoe.mirai.Bot
import org.luaj.vm2.Globals
import org.luaj.vm2.LoadState
import org.luaj.vm2.LuaValue
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.*
import org.luaj.vm2.lib.jse.*

class MiraiCoreGlobals : ExtendGlobals(), EventListenerContainable, BotReceivable {
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

        load(StringExLib())
        load(MiraiCoreLib())
        load(LuaJavaExLib())
        load(ThreadExLib())
        load(HttpLib())
        load(JsonLib())
        load(JDBCLib())
        load(JsoupLib())

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

    override fun receiveBot(bot: Bot) {
        get("onLoad").takeIf { it != LuaValue.NIL }?.invoke(BotCoreImpl(bot))
    }

    override fun destroy() {
        clearListeners()
        super.destroy()
    }
}