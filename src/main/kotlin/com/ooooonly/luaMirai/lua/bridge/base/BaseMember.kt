package com.ooooonly.luaMirai.lua.bridge.base

import org.luaj.vm2.LuaValue

abstract class BaseMember {
    abstract var id: Long
    abstract var bot: BaseBot
    abstract var group: BaseGroup
    abstract var nick: String
    abstract var nameCard: String
    abstract var specialTitle: String
    abstract var isAdministrator: Boolean
    abstract var isOwner: Boolean
    abstract var isOperator: Boolean
    abstract var isFriend: Boolean
    abstract var muteTimeRemaining: Int
    abstract var isMuted: Boolean
    abstract var permission: LuaValue

    abstract fun mute(time: Int)
    abstract fun unMute()
    abstract fun kick(msg: String)
    abstract fun asFriend(): BaseFriend
    abstract fun sendMessage(msg: LuaValue): BaseMessage
    abstract fun sendImage(url: String): BaseMessage
}