package com.ooooonly.luaMirai.lua.bridge.base

import org.luaj.vm2.LuaValue

abstract class BaseGroup {
    abstract var avatarUrl: String
    abstract var name: String
    abstract var owner: BaseMember
    abstract var bot: BaseBot

    abstract fun sendMsg(msg: BaseMsg)
    abstract fun sendImg(url: String)
    abstract fun getMember(id: Long): BaseMember
    abstract fun getBotMuteRemain(): Int
    abstract fun getBotAsMember(): BaseMember
    abstract fun getBotPermission(): LuaValue
    abstract fun containsMember(id: Long): Boolean
    abstract fun quit()
}