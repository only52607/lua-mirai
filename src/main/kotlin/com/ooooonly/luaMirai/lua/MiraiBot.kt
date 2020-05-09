package com.ooooonly.luaMirai.lua

import io.ktor.util.Hash
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.*
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.message.FriendMessageEvent
import net.mamoe.mirai.message.GroupMessageEvent
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaFunction
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs

class MiraiBot : LuaBot {
    var bot: Bot
    var scriptId: Int

    companion object {
        var listeners: HashMap<Int, HashMap<Int, CompletableJob>> = HashMap()
    }

    constructor(account: Long, password: String, scriptId: Int) : super(account, password) {
        this.scriptId = scriptId;
        try {
            this.bot = Bot.getInstance(account)
        } catch (e: Exception) {
            this.bot = Bot(account, password)
        }
    }

    constructor(bot: Bot, scriptId: Int) : super(bot.id, "") {
        this.scriptId = scriptId;
        this.bot = bot
    }

    override fun getSubscribeFunction(opcode: Int): SubscribeFunction = object : SubscribeFunction(opcode) {
        override fun onSubscribe(self: LuaValue, listener: LuaFunction): LuaValue = self.also {
            if (!(self is MiraiBot)) throw LuaError("The reference object must be MiraiBot")
            listeners[self.scriptId]?.let { it[opcode]?.complete() }
            if (listeners[self.scriptId] == null) listeners[self.scriptId] = HashMap()
            when (opcode) {
                EVENT_MSG_FRIEND -> self.bot.subscribeAlways<FriendMessageEvent> {
                    listener.call(self, MiraiMsg(it.message, it.bot), MiraiFriend(self, it.sender)).let { args ->
                        if (args.narg() != 0 && args.arg1() != LuaValue.NIL) it.intercept()
                    }
                }
                EVENT_MSG_GROUP -> self.bot.subscribeAlways<GroupMessageEvent> {
                    var g = MiraiGroup(self, it.group)
                    listener.invoke(
                        LuaValue.varargsOf(
                            arrayOf<LuaValue>(
                                self,
                                MiraiMsg(it.message, (self as MiraiBot).bot),
                                g,
                                MiraiGroupMember(self, g, it.sender)
                            )
                        )
                    ).let { args ->
                        if (args.narg() != 0 && args.arg1() != LuaValue.NIL) it.intercept()
                    }
                }
                else -> null
            }?.let { listeners[self.scriptId]?.set(opcode, it) }
        }
    }

    override fun getOpFunction(opcode: Int): OpFunction = object : OpFunction(opcode) {
        override fun op(varargs: Varargs): Varargs = varargs.arg1().let {
            if (!(it is MiraiBot)) throw LuaError("The reference object must be MiraiBot")
            when (opcode) {
                LOGIN -> runBlocking { it.also { it.bot.alsoLogin() } }
                JOIN -> runBlocking { it.also { it.bot.join() } }
                CLOSE_AND_JOIN -> runBlocking { it.also { it.bot.closeAndJoin() } }
                GET_FRIEND -> MiraiFriend(it, varargs.optlong(2, 0))
                GET_GROUP -> MiraiGroup(it, varargs.optlong(2, 0))
                GET_SELF_QQ -> MiraiFriend(it, it.bot.selfQQ)
                GET_ID -> LuaValue.valueOf(it.bot.id.toString())
                CONTAINS_FRIEND -> LuaValue.valueOf(it.bot.containsFriend(varargs.optlong(2, 0)))
                CONTAINS_GROUP -> LuaValue.valueOf(it.bot.containsGroup(varargs.optlong(2, 0)))
                IS_ACTIVE -> LuaValue.valueOf(it.bot.isActive)
                else -> LuaValue.NIL
                /*ADD_FRIEND -> runBlocking {
                    LuaValue.valueOf(
                        bot.bot.addFriend(
                            varargs.optlong(2, 0),
                            varargs.optjstring(3, ""),
                            varargs.optjstring(4, "")
                        ).toString()
                    )
                }*/
            }
        }
    }

    fun unSubsribeAll() = listeners[scriptId]?.let { it.forEach { it.value.complete() } }
}