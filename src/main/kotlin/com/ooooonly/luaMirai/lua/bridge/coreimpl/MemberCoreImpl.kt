package com.ooooonly.luaMirai.lua.bridge.coreimpl

import com.ooooonly.luaMirai.lua.bridge.base.*
import com.ooooonly.luakt.asLuaValue
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.*
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.asMessageChain
import net.mamoe.mirai.message.uploadImage
import org.luaj.vm2.LuaValue
import java.net.URL

class MemberCoreImpl(val host: Member) : BaseMember() {
    override var bot: BaseBot
        get() = BotCoreImpl.getInstance(host.bot)
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
    override var muteTimeRemaining: Int
        get() = host.muteTimeRemaining
        set(value) {}
    override var isMuted: Boolean
        get() = host.isMuted
        set(value) {}
    override var permission: LuaValue
        get() = host.permission.asLuaValue()
        set(value) {}

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

    override fun asFriend(): BaseFriend = FriendCoreImpl(host.asFriend())

    override fun sendMessage(msg: LuaValue): MessageCoreImpl {
        val msgToSend = if (msg is MessageCoreImpl) msg.host else PlainText(msg.toString())
        return runBlocking {
            MessageCoreImpl(host.sendMessage(msgToSend).source.asMessageChain())
        }
    }

    override fun sendImage(url: String): MessageCoreImpl =
        runBlocking {
            MessageCoreImpl(host.sendMessage(host.uploadImage(URL(url))).source.asMessageChain())
        }

}