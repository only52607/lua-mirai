package com.ooooonly.luaMirai.lua

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.QQ
import net.mamoe.mirai.message.data.PlainText
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaValue

class MiraiGroup : LuaGroup {
    var group: Group

    constructor(bot: LuaBot, id: Long) : super(bot, id) {
        group = (bot as MiraiBot).bot.getGroup(id);
    }

    constructor(bot: LuaBot, group: Group) : super(bot, group.id) {
        this.group = group
    }

    override fun getGetMemberFunction(): GetMemberFunction {
        return MiraiGetMemberFunction()
    }

    override fun getSendGroupMsgFunction(): SendGroupMsgFunction {
        return MiraiSendGroupMsgFunction()
    }
}

class MiraiSendGroupMsgFunction : LuaGroup.SendGroupMsgFunction() {
    override fun sendMsg(luaGroup: LuaGroup, msg: LuaString): LuaValue {
        if (msg != null) {
            runBlocking {
                println("准备发送群消息：" + msg.checkjstring())
                (luaGroup as MiraiGroup).group.sendMessage(PlainText(msg.checkjstring()))
            }
        }
        return LuaValue.NIL
    }

    override fun sendMsg(friend: LuaGroup, luaMsg: LuaMsg): LuaValue {
        return LuaValue.NIL
    }
}

class MiraiGetMemberFunction : LuaGroup.GetMemberFunction() {
    override fun getMember(group: LuaGroup, memberId: LuaValue): LuaValue {
        return MiraiGroupMember(group.get("bot") as MiraiBot, group, memberId.checklong())
    }
}