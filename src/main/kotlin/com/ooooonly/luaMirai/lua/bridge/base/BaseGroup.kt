package com.ooooonly.luaMirai.lua.bridge.base

import org.luaj.vm2.LuaValue

abstract class BaseGroup {
    abstract var id: Long
    abstract var avatarUrl: String
    abstract var name: String
    abstract var owner: BaseMember
    abstract var bot: BaseBot
    abstract var settings: LuaValue
    abstract var botAsMember: BaseMember
    abstract var botPermission: LuaValue
    abstract var botMuteRemaining: Int
    abstract var isBotMuted: Boolean

    abstract fun sendMessage(msg: LuaValue): BaseMessage
    abstract fun sendImage(url: String): BaseMessage
    abstract fun getMember(id: Long): BaseMember
    abstract fun containsMember(id: Long): Boolean
    abstract fun quit()
}