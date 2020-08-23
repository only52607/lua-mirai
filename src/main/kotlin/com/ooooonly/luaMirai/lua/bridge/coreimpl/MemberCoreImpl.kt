package com.ooooonly.luaMirai.lua.bridge.coreimpl

import asLuaValue
import com.ooooonly.luaMirai.lua.bridge.base.*
import kotlinx.coroutines.launch
import net.mamoe.mirai.contact.*
import net.mamoe.mirai.message.uploadImage
import org.luaj.vm2.LuaValue
import java.net.URL

class MemberCoreImpl(val host: Member) : BaseMember() {
    override var bot: BaseBot
        get() = TODO("Not yet implemented")
        set(value) {}
    override var group: BaseGroup
        get() = GroupCoreImpl(host.group)
        set(value) {}
    override var nick: String
        get() = host.nick
        set(value) {}
    override var nameCard: String
        get() = host.nameCard
        set(value) {}
    override var specialTitle: String
        get() = host.specialTitle
        set(value) {}
    override var isAdministrator: Boolean
        get() = host.isAdministrator()
        set(value) {}
    override var isOwner: Boolean
        get() = host.isOwner()
        set(value) {}
    override var isFriend: Boolean
        get() = host.isFriend
        set(value) {}

    override fun getMuteRemain(): Int = host.muteTimeRemaining

    override fun isMuted(): Boolean = host.isMuted

    override fun mute(time: Int) {
        host.bot.launch {
            host.mute(time)
        }
    }

    override fun unMute() {
        host.bot.launch {
            host.unmute()
        }
    }

    override fun kick(msg: String) {
        host.bot.launch {
            host.kick()
        }
    }

    override fun asFriend(): BaseFriend {
        TODO("Not yet implemented")
    }

    override fun getPermission(): LuaValue = host.permission.asLuaValue()

    override fun sendMsg(msg: BaseMsg) {
        if (msg !is MsgCoreImpl) return
        host.bot.launch {
            host.sendMessage(msg.host)
        }
    }

    override fun sendImg(url: String) {
        host.bot.launch {
            host.sendMessage(host.uploadImage(URL(url)))
        }
    }

}