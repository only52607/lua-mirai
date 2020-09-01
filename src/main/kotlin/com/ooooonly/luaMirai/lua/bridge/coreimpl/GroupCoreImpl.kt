package com.ooooonly.luaMirai.lua.bridge.coreimpl


import com.ooooonly.luaMirai.lua.bridge.base.BaseBot
import com.ooooonly.luaMirai.lua.bridge.base.BaseGroup
import com.ooooonly.luaMirai.lua.bridge.base.BaseMember
import com.ooooonly.luakt.asLuaValue
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.isBotMuted
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.asMessageChain
import net.mamoe.mirai.message.uploadImage
import org.luaj.vm2.LuaValue
import java.net.URL

class GroupCoreImpl(val host: Group) : BaseGroup() {
    override var id: Long
        get() = host.id
        set(value) {
            throw UnsupportSetterLuaError
        }
    override var avatarUrl: String
        get() = host.avatarUrl
        set(value) {
            throw UnsupportSetterLuaError
        }
    override var name: String
        get() = host.name
        set(value) {
            host.name = value
        }
    override var owner: BaseMember
        get() = MemberCoreImpl(host.owner)
        set(value) {
            owner = value
        }
    override var bot: BaseBot
        get() = BotCoreImpl.getInstance(host.bot)
        set(value) {
            throw UnsupportSetterLuaError
        }
    override var settings: LuaValue
        get() = host.settings.asLuaValue()
        set(value) {
            throw UnsupportSetterLuaError
        }
    override var botAsMember: BaseMember
        get() = MemberCoreImpl(host.botAsMember)
        set(value) {
            throw UnsupportSetterLuaError
        }
    override var botPermission: LuaValue
        get() = host.botPermission.asLuaValue()
        set(value) {
            throw UnsupportSetterLuaError
        }
    override var botMuteRemaining: Int
        get() = host.botMuteRemaining
        set(value) {
            throw UnsupportSetterLuaError
        }
    override var isBotMuted: Boolean
        get() = host.isBotMuted
        set(value) {
            throw UnsupportSetterLuaError
        }

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

    override fun getMember(id: Long): MemberCoreImpl = MemberCoreImpl(host.get(id))

    override fun containsMember(id: Long): Boolean = host.contains(id)

    override fun quit() {
        host.bot.launch {
            host.quit()
        }
    }
}