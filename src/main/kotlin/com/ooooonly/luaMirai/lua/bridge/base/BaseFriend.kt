package com.ooooonly.luaMirai.lua.bridge.base

import org.luaj.vm2.LuaUserdata
import org.luaj.vm2.LuaValue

abstract class BaseFriend {
    abstract var nick: String
    abstract var avatarUrl: String
    abstract var bot: BaseBot
    abstract var isActive: Boolean
    abstract fun sendMsg(msg: LuaValue): BaseMsg
    abstract fun sendImg(url: String): BaseMsg
}