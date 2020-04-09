package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.lua.LuaGroup.*
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.QQ
import net.mamoe.mirai.message.data.PlainText
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs

class MiraiGroup : LuaGroup {
    var group: Group

    constructor(bot: LuaBot, id: Long) : super(bot, id) {
        group = (bot as MiraiBot).bot.getGroup(id);
    }

    constructor(bot: LuaBot, group: Group) : super(bot, group.id) {
        this.group = group
    }

    override fun getSendGroupMsgFunction(): SendGroupMsgFunction {
        return MiraiSendGroupMsgFunction
    }

    override fun getGroupOpFunction(type: Int): GroupOpFunction {
        return MiraiGroupOpFunction(type)
    }
}

object MiraiSendGroupMsgFunction : LuaGroup.SendGroupMsgFunction() {
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

class MiraiGroupOpFunction(type: Int) : LuaGroup.GroupOpFunction(type) {
    override fun op(varargs: Varargs): Varargs {
        var group = varargs.arg1() as MiraiGroup
        return when (type) {
            GET_MEMBER -> MiraiGroupMember(group.get("bot") as MiraiBot, group, varargs.optlong(2, 0))
            GET_AVATAR_URL -> LuaValue.valueOf(group.group.avatarUrl)
            GET_BOT_AS_MEMBER -> MiraiGroupMember(group.get("bot") as MiraiBot, group, group.group.botAsMember)
            GET_BOT_MUTE_REMAIN -> LuaValue.valueOf(group.group.botMuteRemaining)
            GET_BOT_PERMISSION -> LuaValue.valueOf(group.group.botPermission.name)
            GET_NAME -> LuaValue.valueOf(group.group.name)
            GET_OWNER -> MiraiGroupMember(group.get("bot") as MiraiBot, group, group.group.owner)
            CONTAINS -> LuaValue.valueOf(group.group.contains(varargs.optlong(2, 0)))
            GET_MEMBER_OR_NULL -> {
                var value = group.group.getOrNull(varargs.optlong(2, 0))
                if (value != null) MiraiGroupMember(group.get("bot") as MiraiBot, group, value)
                else LuaValue.NIL
            }
            QUIT -> runBlocking {
                LuaValue.valueOf(group.group.quit())
            }
            TO_FULL_STRING -> LuaValue.valueOf(group.group.toFullString())
            else -> LuaValue.NIL
        }
    }
}