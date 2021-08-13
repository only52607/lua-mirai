package com.ooooonly.luaMirai.lua.lib.mirai

import kotlinx.coroutines.CoroutineScope
import net.mamoe.mirai.utils.MiraiExperimentalApi
import net.mamoe.mirai.utils.MiraiInternalApi
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

@MiraiExperimentalApi
@MiraiInternalApi
open class MiraiLib(private val coroutineScope: CoroutineScope) : TwoArgFunction() {
    override fun call(modName: LuaValue?, env: LuaValue): LuaValue? {
        val globals = env.checkglobals()
        globals.load(BotLib(coroutineScope))
        globals.load(MessageLib)
        globals.load(EventLib(coroutineScope))
        return LuaValue.NIL
    }
}