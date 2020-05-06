package com.ooooonly.luaMirai.lua

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.contact.isAdministrator
import net.mamoe.mirai.contact.isMuted
import net.mamoe.mirai.contact.isOwner
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs

class MiraiGroupMember : LuaGroupMember {
    var member: Member

    constructor(bot: LuaBot, luaGroup: LuaGroup, id: Long) : super(bot, luaGroup, id) {
        this.member = (luaGroup as MiraiGroup).group.members.get(id)
    }

    constructor(bot: LuaBot, luaGroup: LuaGroup, member: Member) : super(bot, luaGroup, member.id) {
        this.member = member
    }

    override fun getOpFunction(opcode: Int): OpFunction {
        return object : OpFunction(opcode) {
            override fun op(varargs: Varargs): LuaValue {
                var miraiMember = varargs.arg1() as MiraiGroupMember
                return when (opcode) {
                    GET_NICK -> LuaValue.valueOf(miraiMember.member.nick)
                    GET_NAME_CARD -> LuaValue.valueOf(miraiMember.member.nameCard)
                    GET_MUTE_REMAIN -> LuaValue.valueOf(miraiMember.member.muteTimeRemaining)
                    GET_SPECIAL_TITLE -> LuaValue.valueOf(miraiMember.member.specialTitle)
                    IS_MUTE -> LuaValue.valueOf(miraiMember.member.isMuted)
                    IS_ADMINISTRATOR -> LuaValue.valueOf(miraiMember.member.isAdministrator())
                    IS_OWNER -> LuaValue.valueOf(miraiMember.member.isOwner())
                    MUTE -> runBlocking {
                        miraiMember.member.mute(varargs.optint(2, 0))
                        miraiMember
                    }
                    UN_MUTE -> runBlocking {
                        miraiMember.member.unmute()
                        miraiMember
                    }
                    KICK -> runBlocking {
                        miraiMember.member.kick(varargs.optjstring(2, ""))
                        miraiMember
                    }
                    else -> LuaValue.NIL
                }
            }
        }
    }
}

