package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.lua.LuaGroupMember.*
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

    override fun getMemberOpFunction(type: Int): MemberOpFunction {
        return MiraiMemberOpFunction(type)
    }
}

class MiraiMemberOpFunction(type: Int) : LuaGroupMember.MemberOpFunction(type) {
    override fun op(varargs: Varargs): LuaValue {
        var miraiMember = varargs.arg1() as MiraiGroupMember
        return when (type) {
            GET_NICK -> LuaValue.valueOf(miraiMember.member.nick)
            GET_NAME_CARD -> LuaValue.valueOf(miraiMember.member.nameCard)
            GET_MUTE_REMAIN -> LuaValue.valueOf(miraiMember.member.muteTimeRemaining)
            GET_SPECIAL_TITLE -> LuaValue.valueOf(miraiMember.member.specialTitle)
            IS_MUTE -> LuaValue.valueOf(miraiMember.member.isMuted())
            IS_ADMINISTRATOR -> LuaValue.valueOf(miraiMember.member.isAdministrator())
            IS_OWNER -> LuaValue.valueOf(miraiMember.member.isOwner())
            MUTE -> runBlocking {
                miraiMember.member.mute(varargs.optint(2, 0))
                LuaValue.NIL
            }
            UN_MUTE -> LuaValue.valueOf(miraiMember.member.nick)
            KICK -> LuaValue.valueOf(miraiMember.member.nick)
            else -> LuaValue.NIL
        }
    }
}