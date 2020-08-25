package com.ooooonly.luaMirai.lua.lib

import com.ooooonly.luaMirai.lua.bridge.coreimpl.BotCoreImpl
import com.ooooonly.luaMirai.lua.bridge.coreimpl.MsgCoreImpl
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

open class MiraiCoreLib : TwoArgFunction() {
    override fun call(modName: LuaValue?, env: LuaValue): LuaValue? {
        val globals = env.checkglobals()
        BotCoreImpl.setBotFactory(globals) //载入Bot构建函数
        MsgCoreImpl.setMsgConstructor(globals) //载入Msg构建函数
        return LuaValue.NIL
    }
}