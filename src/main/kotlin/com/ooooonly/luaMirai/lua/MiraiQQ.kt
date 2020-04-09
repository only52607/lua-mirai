package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.lua.LuaQQ.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.QQ
import net.mamoe.mirai.message.data.PlainText
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs

class MiraiQQ : LuaQQ {
    var friend: QQ

    constructor(bot: LuaBot, id: Long) : super(bot, id) {
        friend = (bot as MiraiBot).bot.getFriend(id);
    }

    constructor(bot: LuaBot, friend: QQ) : super(bot, friend.id) {
        this.friend = friend
    }

    override fun getQQOpFunction(type: Int): QQOpFunction {
        return MiraiQQOpFunction(type)
    }


    override fun getSendFriendMsgFunction(): SendFriendMsgFunction {
        return MiraiSendFriendMsgFunction()
    }
}

class MiraiSendFriendMsgFunction : LuaQQ.SendFriendMsgFunction() {

    override fun sendMsg(friend: LuaQQ?, msg: LuaString?): LuaValue {
        if (msg != null) {
            runBlocking {
                println("准备发送消息：" + msg.checkjstring())
                (friend as MiraiQQ).friend?.sendMessage(PlainText(msg.checkjstring()))
            }
        }
        return LuaValue.NIL
    }

    override fun sendMsg(friend: LuaQQ?, luaMsg: LuaMsg?): LuaValue {
        return LuaValue.NIL
    }
}

class MiraiQQOpFunction(type: Int) : LuaQQ.QQOpFunction(type) {
    override fun op(varargs: Varargs): Varargs {
        var qq: MiraiQQ = varargs.arg1() as MiraiQQ
        return when (type) {
            GET_NICK -> LuaValue.valueOf(qq.friend.nick)
            GET_AVATAR_URL -> LuaValue.valueOf(qq.friend.avatarUrl)
            IS_ACTIVE -> LuaValue.valueOf(qq.friend.isActive)
            QUERY_REMARK -> runBlocking {
                LuaValue.valueOf(qq.friend.queryRemark().value)
            }
            QUERY_PROFILE -> runBlocking {
                var profile = qq.friend.queryProfile()
                var profileTable = LuaTable()
                profileTable.set("chineseName", LuaValue.valueOf(profile.chineseName))
                profileTable.set("company", LuaValue.valueOf(profile.company))
                profileTable.set("email", LuaValue.valueOf(profile.email))
                profileTable.set("englishName", LuaValue.valueOf(profile.englishName))
                profileTable.set("homepage", LuaValue.valueOf(profile.homepage))
                profileTable.set("nickname", LuaValue.valueOf(profile.nickname))
                profileTable.set("personalStatement", LuaValue.valueOf(profile.personalStatement))
                profileTable.set("phone", LuaValue.valueOf(profile.phone))
                profileTable.set("school", LuaValue.valueOf(profile.school))
                profileTable.set("zipCode", LuaValue.valueOf(profile.zipCode))
                profileTable.set("qAge", profile.qAge?.let { LuaValue.valueOf(it) })
                profileTable.set("birthday", LuaValue.valueOf(profile.birthday.toString()))
                profileTable.set("gender", LuaValue.valueOf(profile.gender.name))
                profileTable.set("qq", LuaValue.valueOf(profile.qq.toString()))
                profileTable
            }
            else -> LuaValue.NIL
        }
    }

}