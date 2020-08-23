package com.ooooonly.luaMirai.lua.bridge.coreimpl

import asLuaValue
import com.ooooonly.luaMirai.lua.bridge.base.BaseBot
import com.ooooonly.luaMirai.lua.bridge.base.BaseGroup
import com.ooooonly.luaMirai.lua.bridge.base.BaseMember
import com.ooooonly.luaMirai.lua.bridge.base.BaseMsg
import kotlinx.coroutines.launch
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.uploadImage
import org.luaj.vm2.LuaValue
import java.net.URL

class GroupCoreImpl(val host: Group) : BaseGroup() {
    override var avatarUrl: String
        get() = TODO("Not yet implemented")
        set(value) {}
    override var name: String
        get() = TODO("Not yet implemented")
        set(value) {}
    override var owner: BaseMember
        get() = TODO("Not yet implemented")
        set(value) {}
    override var bot: BaseBot
        get() = TODO("Not yet implemented")
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