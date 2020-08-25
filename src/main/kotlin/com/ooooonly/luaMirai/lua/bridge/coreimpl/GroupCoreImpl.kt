package com.ooooonly.luaMirai.lua.bridge.coreimpl


import com.ooooonly.luaMirai.lua.bridge.base.BaseBot
import com.ooooonly.luaMirai.lua.bridge.base.BaseGroup
import com.ooooonly.luaMirai.lua.bridge.base.BaseMember
import com.ooooonly.luaMirai.lua.bridge.base.BaseMsg
import com.ooooonly.luakt.asLuaValue
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.data.EmptyMessageChain
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.asMessageChain
import net.mamoe.mirai.message.uploadImage
import org.luaj.vm2.LuaValue
import java.net.URL

class GroupCoreImpl(val host: Group) : BaseGroup() {
    override var avatarUrl: String
        get() = host.avatarUrl
        set(value) {}
    override var name: String
        get() = host.name
        set(value) {}
    override var owner: BaseMember
        get() = MemberCoreImpl(host.owner)
        set(value) {}
    override var bot: BaseBot
        get() = BotCoreImpl.getInstance(host.bot)
        set(value) {}

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

    override fun getMember(id: Long): MemberCoreImpl = MemberCoreImpl(host.get(id))

    override fun getBotMuteRemain(): Int = host.botMuteRemaining

    override fun getBotAsMember(): MemberCoreImpl = MemberCoreImpl(host.botAsMember)

    override fun getBotPermission(): LuaValue = host.botPermission.asLuaValue()

    override fun containsMember(id: Long): Boolean = host.contains(id)

    override fun quit() {
        host.bot.launch {
            host.quit()
        }
    }
}