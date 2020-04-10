package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.lua.LuaBot.*
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.*
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.message.FriendMessage
import net.mamoe.mirai.message.GroupMessage
import org.luaj.vm2.LuaFunction
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs

class MiraiBot : LuaBot {
    var bot: Bot

    constructor(account: Long, password: String) : super(account, password) {
        this.bot = Bot(account, password)
    }

    constructor(bot: Bot) : super(bot.id, "") {
        this.bot = bot
    }

    override fun getSubscribeGroupMsgFunction(): SubscribeMsgFunction {
        return MiraiSubscribeGroupMsgFunction
    }

    override fun getSubscribeFriendMsgFunction(): SubscribeMsgFunction {
        return MiraiSubscribeFriendMsgFunction
    }

    override fun getBotOpFunction(type: Int): BotOpFunction {
        return MiraiBotOpFunction(type)
    }
}

class MiraiBotOpFunction(type: Int) : LuaBot.BotOpFunction(type) {
    override fun op(varargs: Varargs): Varargs {
        var bot = varargs.arg1() as MiraiBot
        return when (type) {
            LOGIN -> runBlocking {
                bot.bot.alsoLogin()
                bot
            }
            JOIN -> runBlocking {
                bot.bot.join()
                bot
            }
            CLOSE_AND_JOIN -> runBlocking {
                bot.bot.closeAndJoin()
                bot
            }
            GET_FRIEND -> MiraiQQ(bot, varargs.optlong(2, 0))
            GET_GROUP -> MiraiGroup(bot, varargs.optlong(2, 0))
            GET_SELF_QQ -> MiraiQQ(bot, bot.bot.selfQQ)
            GET_ID -> LuaValue.valueOf(bot.bot.id.toString())
            ADD_FRIEND -> runBlocking {
                LuaValue.valueOf(
                    bot.bot.addFriend(
                        varargs.optlong(2, 0),
                        varargs.optjstring(3, ""),
                        varargs.optjstring(4, "")
                    ).toString()
                )
            }
            CONTAINS_FRIEND -> LuaValue.valueOf(bot.bot.containsFriend(varargs.optlong(2, 0)))
            CONTAINS_GROUP -> LuaValue.valueOf(bot.bot.containsGroup(varargs.optlong(2, 0)))
            IS_ACTIVE -> LuaValue.valueOf(bot.bot.isActive)
            else -> LuaValue.NIL
        }
    }

}

object MiraiSubscribeFriendMsgFunction : LuaBot.SubscribeMsgFunction() {
    override fun subscribeFriendMsg(luaBot: LuaBot?, listener: LuaFunction?): LuaValue {
        (luaBot as MiraiBot).bot.subscribeAlways<FriendMessage> {
            if (listener != null) {
                listener.call(luaBot, LuaValue.valueOf(it.message.toString()), MiraiQQ(luaBot, it.sender))
            }
        }
        return LuaValue.NIL
    }
}

object MiraiSubscribeGroupMsgFunction : LuaBot.SubscribeMsgFunction() {
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