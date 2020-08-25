package com.ooooonly.luaMirai.lua.bridge.base

import org.luaj.vm2.LuaValue

abstract class BaseMember {
    abstract var bot: BaseBot
    abstract var group: BaseGroup
    abstract var nick: String
    abstract var nameCard: String
    abstract var specialTitle: String
    abstract var isAdministrator: Boolean
    abstract var isOwner: Boolean
    abstract var isFriend: Boolean

    abstract fun getMuteRemain(): Int
    abstract fun isMuted(): Boolean
    abstract fun mute(time: Int)
    abstract fun unMute()
    abstract fun kick(msg: String)
    abstract fun asFriend(): BaseFriend
    abstract fun getPermission(): LuaValue
    abstract fun sendMsg(msg: LuaValue): BaseMsg
    abstract fun sendImg(url: String): BaseMsg
}