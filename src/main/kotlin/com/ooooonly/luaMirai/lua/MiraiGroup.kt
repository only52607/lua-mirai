package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.utils.checkArg
import com.ooooonly.luaMirai.utils.checkMessageChain
import com.ooooonly.luaMirai.utils.generateOpFunction
import com.ooooonly.luaMirai.utils.toLuaValue
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.data.MessageChain
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs

class MiraiGroup(var bot: MiraiBot, var group: Group) : LuaGroup(bot, group.id) {
    constructor(bot: MiraiBot, id: Long) : this(bot, bot.bot.getGroup(id))

    init {
        rawset("name", this.group.name.toLuaValue())
        rawset("avatarUrl", this.group.avatarUrl.toLuaValue())
    }

    override fun getOpFunction(opcode: Int): OpFunction = generateOpFunction(opcode) { op, varargs ->
        varargs.checkArg<MiraiGroup>(1).let {
            when (opcode) {
                GET_MEMBER -> MiraiGroupMember(it.get("bot") as MiraiBot, it, varargs.optlong(2, 0))
                GET_AVATAR_URL -> it.group.avatarUrl.toLuaValue()
                GET_BOT_AS_MEMBER -> MiraiGroupMember(it.get("bot") as MiraiBot, it, it.group.botAsMember)
                GET_BOT_MUTE_REMAIN -> it.group.botMuteRemaining.toLuaValue()
                GET_BOT_PERMISSION -> it.group.botPermission.name.toLuaValue()
                GET_NAME -> it.group.name.toLuaValue()
                GET_OWNER -> MiraiGroupMember(it.get("bot") as MiraiBot, it, it.group.owner)
                CONTAINS -> it.group.contains(varargs.optlong(2, 0)).toLuaValue()
                GET_MEMBER_OR_NULL -> it.group.getOrNull(varargs.optlong(2, 0))?.let { member ->
                    MiraiGroupMember(it.get("bot") as MiraiBot, it, member)
                } ?: LuaValue.NIL
                QUIT -> it.group.quit().toLuaValue()
                SEND_MSG -> MiraiSource(it.group.sendMessage(varargs.arg(2).checkMessageChain()))
                SEND_IMG -> MiraiMsg.uploadImage(varargs.arg(2), it)?.let { img ->
                    MiraiSource(it.group.sendMessage(img))
                } ?: LuaValue.NIL
                else -> LuaValue.NIL
            }
        }
    }
}