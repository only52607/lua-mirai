package com.ooooonly.luaMirai.lua.bridge.base

abstract class BaseFriend {
    abstract var nick: String
    abstract var avatarUrl: String
    abstract var bot: BaseBot
    abstract fun sendMsg(msg: BaseMsg)
    abstract fun sendImg(url: String)
    abstract fun isActive(): Boolean
}