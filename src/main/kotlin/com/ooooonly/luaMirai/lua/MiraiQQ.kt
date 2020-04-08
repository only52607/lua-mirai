package com.ooooonly.luaMirai.lua

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.QQ
import net.mamoe.mirai.message.data.PlainText
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaValue

class MiraiQQ : LuaQQ {
    var friend: QQ
    var nick: String

    constructor(bot: LuaBot, id: Long) : super(bot, id) {
        friend = (bot as MiraiBot).bot.getFriend(id);
        nick = friend.nick
        initTable()
    }

    constructor(bot: LuaBot, friend: QQ) : super(bot, friend.id) {
        this.friend = friend
        nick = friend.nick
        initTable()
    }

    fun initTable() {
        this.set("nick", nick)
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