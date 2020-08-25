package com.ooooonly.luaMirai.lua.bridge.coreimpl

import com.ooooonly.luaMirai.lua.bridge.base.*
import com.ooooonly.luakt.asLuaValue
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.*
import net.mamoe.mirai.message.data.EmptyMessageChain
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.asMessageChain
import net.mamoe.mirai.message.uploadImage
import org.luaj.vm2.LuaValue
import java.net.URL

class MemberCoreImpl(val host: Member) : BaseMember() {
    override var bot: BaseBot = BotCoreImpl.getInstance(host.bot)
    override var group: BaseGroup = GroupCoreImpl(host.group)
    override var nick: String = host.nick
    override var nameCard: String = host.nameCard
    override var specialTitle: String = host.specialTitle
    override var isAdministrator: Boolean = host.isAdministrator()
    override var isOwner: Boolean = host.isOwner()
    override var isFriend: Boolean = host.isFriend

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

    override fun asFriend(): BaseFriend = FriendCoreImpl(host.asFriend())

    override fun getPermission(): LuaValue = host.permission.asLuaValue()

    override fun sendMsg(msg: LuaValue): MsgCoreImpl {
        val msgToSend = if (msg is MsgCoreImpl) msg.host else PlainText(msg.toString())
        return runBlocking {
            MsgCoreImpl(host.sendMessage(msgToSend).source.asMessageChain())
        }
    }

    override fun sendImg(url: String): MsgCoreImpl =
        runBlocking {
            MsgCoreImpl(host.sendMessage(host.uploadImage(URL(url))).source.asMessageChain())
        }

}