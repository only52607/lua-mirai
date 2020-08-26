package com.ooooonly.luaMirai.lua.bridge.coreimpl

import com.ooooonly.luaMirai.lua.bridge.base.BaseBot
import com.ooooonly.luaMirai.lua.bridge.base.BaseFriend
import com.ooooonly.luaMirai.lua.bridge.base.BaseMessage
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.nameCardOrNick
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.asMessageChain
import net.mamoe.mirai.message.sendImage
import net.mamoe.mirai.message.uploadImage
import org.luaj.vm2.LuaValue
import java.net.URL

class FriendCoreImpl(val host: Friend) : BaseFriend() {
    override var nick: String
        get() = host.nick
        set(value) {}
    override var avatarUrl: String
        get() = host.avatarUrl
        set(value) {}
    override var bot: BaseBot
        get() = BotCoreImpl.getInstance(host.bot)
        set(value) {}
    override var isActive: Boolean
        get() = host.isActive
        set(value) {}
    override var nameCardOrNick: String
        get() = host.nameCardOrNick
        set(value) {}

    override fun sendMessage(msg: LuaValue): BaseMessage {
        val msgToSend = if (msg is MessageCoreImpl) msg.host else PlainText(msg.toString())
        return runBlocking {
            MessageCoreImpl(host.sendMessage(msgToSend).source.asMessageChain())
        }
    }

    override fun sendImage(url: String): MessageCoreImpl =
        runBlocking {
            MessageCoreImpl(host.sendImage(URL(url)).source.asMessageChain())
            //MessageCoreImpl(host.sendMessage(host.uploadImage(URL(url))).source.asMessageChain())
        }

}