package com.ooooonly.luaMirai.lua

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
        this.rawset("nick", LuaValue.valueOf(this.member.nick))
        this.rawset("nameCard", LuaValue.valueOf(this.member.nameCard))
        this.rawset("specialTitle", LuaValue.valueOf(this.member.specialTitle))
    }

    override fun getOpFunction(opcode: Int): OpFunction = object : OpFunction(opcode) {
        override fun op(varargs: Varargs): LuaValue = varargs.arg1().let {
            if (!(it is MiraiGroupMember)) throw LuaError("The reference object must be MiraiGroupMember")
            when (opcode) {
                GET_NICK -> LuaValue.valueOf(it.member.nick)
                GET_NAME_CARD -> LuaValue.valueOf(it.member.nameCard)
                GET_MUTE_REMAIN -> LuaValue.valueOf(it.member.muteTimeRemaining)
                GET_SPECIAL_TITLE -> LuaValue.valueOf(it.member.specialTitle)
                IS_MUTE -> LuaValue.valueOf(it.member.isMuted)
                IS_ADMINISTRATOR -> LuaValue.valueOf(it.member.isAdministrator())
                IS_OWNER -> LuaValue.valueOf(it.member.isOwner())
                MUTE -> runBlocking { it.also { it.member.mute(varargs.optint(2, 0)) } }
                UN_MUTE -> runBlocking { it.also { it.member.unmute() } }
                KICK -> runBlocking { it.also { it.member.kick(varargs.optjstring(2, "")) } }
                IS_FRIEND -> LuaValue.valueOf(it.member.isFriend)
                AS_FRIEND -> it.member.asFriendOrNull()?.let { friend ->
                    MiraiFriend(it.bot, friend)
                } ?: LuaValue.NIL
                SEND_MEG -> varargs.arg(2).let { msg ->
                    runBlocking {
                        when (msg) {
                            is LuaString -> it.member.sendMessage(PlainText(msg.checkjstring()))
                            is MiraiMsg -> it.member.sendMessage(msg.chain)
                            else -> null
                        }?.let {
                            MiraiSource(it)
                        } ?: throw LuaError("Unsupported message type,please use String or MiraiMsg!")
                    }
                }
                else -> LuaValue.NIL
            }
        }
    }
}

