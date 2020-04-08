package com.ooooonly.luaMirai.lua

import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.alsoLogin
import net.mamoe.mirai.closeAndJoin
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.message.FriendMessage
import net.mamoe.mirai.message.GroupMessage
import org.luaj.vm2.LuaFunction
import org.luaj.vm2.LuaValue

class MiraiBot : LuaBot {
    var bot: Bot

    constructor(account: Long, password: String) : super(account, password) {
        this.bot = Bot(account, password)
    }

    constructor(bot: Bot) : super(bot.id, "") {
        this.bot = bot
    }

    override fun getLoginFunction(): LuaBot.LoginFunction {
        return MiraiLoginFunction()
    }

    override fun getCloseFunction(): CloseFunction {
        return MiraiCloseFunction()
    }

    override fun getSubscribeGroupMsgFunction(): SubscribeMsgFunction {
        return MiraiSubscribeGroupMsgFunction()
    }

    override fun getJoinFunction(): JoinFunction {
        return MiraiJoinFunction()
    }

    override fun getGetGroupFunction(): GetGroupFunction {
        return MiraiGetGroupFunction()
    }

    override fun getGetFriendFunction(): LuaBot.GetFriendFunction {
        return MiraiGetFriendFunction()
    }

    override fun getSubscribeFriendMsgFunction(): SubscribeMsgFunction {
        return MiraiSubscribeFriendMsgFunction()
    }
}

class MiraiLoginFunction : LuaBot.LoginFunction() {
    override fun login(luaBot: LuaBot?): LuaValue {
        if (luaBot is MiraiBot) {
            runBlocking {
                luaBot.bot.alsoLogin()
            }
        }
        return LuaValue.NIL
    }
}

class MiraiGetFriendFunction : LuaBot.GetFriendFunction() {
    override fun getFriend(luaBot: LuaBot, id: Long): LuaQQ {
        return MiraiQQ(luaBot, id)
    }
}

class MiraiGetGroupFunction : LuaBot.GetGroupFunction() {
    override fun getGroup(luaBot: LuaBot, id: Long): LuaGroup {
        return MiraiGroup(luaBot, id)
    }
}

class MiraiJoinFunction : LuaBot.JoinFunction() {
    override fun join(luaBot: LuaBot?): LuaValue {
        if (luaBot is MiraiBot) {
            runBlocking {
                luaBot.bot.join()
            }
        }
        return LuaValue.NIL
    }
}

class MiraiCloseFunction : LuaBot.CloseFunction() {
    override fun close(luaBot: LuaBot?): LuaValue {
        if (luaBot is MiraiBot) {
            runBlocking {
                luaBot.bot.closeAndJoin()
            }
        }
        return LuaValue.NIL
    }
}

class MiraiSubscribeFriendMsgFunction : LuaBot.SubscribeMsgFunction() {
    override fun subscribeFriendMsg(luaBot: LuaBot?, listener: LuaFunction?): LuaValue {
        (luaBot as MiraiBot).bot.subscribeAlways<FriendMessage> {
            if (listener != null) {
                listener.call(luaBot, LuaValue.valueOf(it.message.toString()), MiraiQQ(luaBot, it.sender))
            }
        }
        return LuaValue.NIL
    }
}

class MiraiSubscribeGroupMsgFunction : LuaBot.SubscribeMsgFunction() {
    override fun subscribeFriendMsg(luaBot: LuaBot?, listener: LuaFunction?): LuaValue {
        (luaBot as MiraiBot).bot.subscribeAlways<GroupMessage> {
            if (listener != null) {
                var luaGroup = MiraiGroup(luaBot, it.group)
                var luaMember = MiraiGroupMember(luaBot, luaGroup, it.sender)
                var args = arrayOf<LuaValue>(luaBot, LuaValue.valueOf(it.message.toString()), luaGroup, luaMember)
                listener.invoke(LuaValue.varargsOf(args))
            }
        }
        return LuaValue.NIL
    }
}