package com.ooooonly.luaMirai.lua.bridge.coreimpl

import com.ooooonly.luaMirai.lua.bridge.base.BaseBot
import com.ooooonly.luaMirai.lua.bridge.base.BaseFriend
import com.ooooonly.luaMirai.lua.bridge.base.BaseMsg
import io.ktor.http.Url
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.uploadImage
import java.net.URL

class FriendCoreImpl(val host: Friend) : BaseFriend() {
    override var nick: String
        get() = host.nick
        set(value) {}
    override var avatarUrl: String
        get() = host.avatarUrl
        set(value) {}
    override var bot: BaseBot
        get() = BotCoreImpl(host.bot)
        set(value) {}

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

    override fun isActive(): Boolean = host.isActive
}