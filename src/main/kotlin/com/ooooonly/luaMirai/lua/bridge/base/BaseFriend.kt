package com.ooooonly.luaMirai.lua.bridge.base

import org.luaj.vm2.LuaValue

abstract class BaseFriend {
    abstract var id: Long
    abstract var nick: String
    abstract var avatarUrl: String
    abstract var bot: BaseBot
    abstract var isActive: Boolean
    abstract var nameCardOrNick: String
    abstract fun sendMessage(msg: LuaValue): BaseMessage
    abstract fun sendImage(url: String): BaseMessage
}