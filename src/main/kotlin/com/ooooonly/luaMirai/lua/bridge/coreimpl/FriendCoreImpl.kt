package com.ooooonly.luaMirai.lua.bridge.coreimpl

import com.ooooonly.luaMirai.lua.bridge.base.BaseBot
import com.ooooonly.luaMirai.lua.bridge.base.BaseFriend
import com.ooooonly.luaMirai.lua.bridge.base.BaseMsg
import io.ktor.http.Url
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.message.data.EmptyMessageChain
import net.mamoe.mirai.message.data.Image
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.asMessageChain
import net.mamoe.mirai.message.uploadImage
import org.luaj.vm2.LuaUserdata
import org.luaj.vm2.LuaValue
import java.net.URL

class FriendCoreImpl(val host: Friend) : BaseFriend() {
    override var nick: String = host.nick
    override var avatarUrl: String = host.avatarUrl
    override var bot: BaseBot = BotCoreImpl.getInstance(host.bot)
    override var isActive: Boolean = host.isActive

    override fun sendMsg(msg: LuaValue): BaseMsg {
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