package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.utils.checkArg
import com.ooooonly.luaMirai.utils.checkMessageChain
import com.ooooonly.luaMirai.utils.generateOpFunction
import com.ooooonly.luaMirai.utils.toLuaValue
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.*
import net.mamoe.mirai.message.data.PlainText
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs

class MiraiGroupMember(var bot: MiraiBot, var group: MiraiGroup, var member: Member) :
    LuaGroupMember(bot, group, member.id) {
    constructor(bot: MiraiBot, group: MiraiGroup, id: Long) : this(bot, group, group.group.members.get(id))

    init {
        rawset("nick", this.member.nick.toLuaValue())
        rawset("nameCard", this.member.nameCard.toLuaValue())
        rawset("specialTitle", this.member.specialTitle.toLuaValue())
    }

    override fun getOpFunction(opcode: Int): OpFunction = generateOpFunction(opcode) { op, varargs ->
        varargs.checkArg<MiraiGroupMember>(1).let {
            when (opcode) {
                GET_NICK -> it.member.nick.toLuaValue()
                GET_NAME_CARD -> it.member.nameCard.toLuaValue()
                GET_MUTE_REMAIN -> it.member.muteTimeRemaining.toLuaValue()
                GET_SPECIAL_TITLE -> it.member.specialTitle.toLuaValue()
                IS_MUTE -> it.member.isMuted.toLuaValue()
                IS_ADMINISTRATOR -> it.member.isAdministrator().toLuaValue()
                IS_OWNER -> it.member.isOwner().toLuaValue()
                MUTE -> it.also { it.member.mute(varargs.optint(2, 0)) }
                UN_MUTE -> it.also { it.member.unmute() }
                KICK -> it.also { it.member.kick(varargs.optjstring(2, "")) }
                IS_FRIEND -> it.member.isFriend.toLuaValue()
                AS_FRIEND -> it.member.asFriendOrNull()?.let { friend -> MiraiFriend(it.bot, friend) } ?: LuaValue.NIL
                SEND_MEG -> MiraiSource(it.member.sendMessage(varargs.arg(2).checkMessageChain()))
                SEND_IMG -> MiraiMsg.uploadImage(varargs.arg(2), it)?.let { img ->
                    MiraiSource(it.member.sendMessage(img))
                } ?: LuaValue.NIL
                else -> LuaValue.NIL
            }
        }
    }
}

