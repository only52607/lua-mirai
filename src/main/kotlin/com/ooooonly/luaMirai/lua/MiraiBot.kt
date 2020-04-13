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

    override fun getSubscribeFunction(opcode: Int): SubscribeFunction? {
        return object : SubscribeFunction(opcode) {
            override fun onSubscribe(self: LuaValue, listener: LuaFunction): LuaValue {
                return when (opcode) {
                    EVENT_MSG_FRIEND -> {
                        (self as MiraiBot).bot.subscribeAlways<FriendMessage> {
                            //println(self)
                            //println(MiraiMsg(it.message,self.bot))
                            //println(MiraiQQ(self, it.sender))
                            listener.call(self, MiraiMsg(it.message, self.bot), MiraiQQ(self, it.sender))
                        }
                        self
                    }
                    EVENT_MSG_GROUP -> {
                        (self as MiraiBot).bot.subscribeAlways<GroupMessage> {
                            var luaGroup = MiraiGroup(self, it.group)
                            var luaMember = MiraiGroupMember(self, luaGroup, it.sender)
                            var args =
                                arrayOf<LuaValue>(
                                    self,
                                    MiraiMsg(it.message, (self as MiraiBot).bot),
                                    luaGroup,
                                    luaMember
                                )
                            listener.invoke(LuaValue.varargsOf(args))
                        }
                        self
                    }
                    else -> LuaValue.NIL
                }
            }
        }
    }

    override fun getOpFunction(opcode: Int): OpFunction {
        return object : OpFunction(opcode) {
            override fun op(varargs: Varargs): Varargs {
                var bot = varargs.arg1() as MiraiBot
                return when (opcode) {
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
    }
}