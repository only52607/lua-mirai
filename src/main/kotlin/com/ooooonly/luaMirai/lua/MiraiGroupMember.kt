package com.ooooonly.luaMirai.lua

import net.mamoe.mirai.contact.Member
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
        return MiraiMemberOpFunction(type);
    }
}

class MiraiMemberOpFunction(type: Int) : LuaGroupMember.MemberOpFunction(type) {
    override fun op(varargs: Varargs?): Varargs {
        return LuaValue.NIL
    }
}