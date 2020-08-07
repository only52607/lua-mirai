package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.utils.checkArg
import com.ooooonly.luaMirai.utils.checkMessageChain
import com.ooooonly.luaMirai.utils.generateOpFunction
import com.ooooonly.luaMirai.utils.toLuaValue
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.Friend

import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.data.PlainText
import org.luaj.vm2.*

class MiraiFriend(var bot: MiraiBot, var friend: Friend) : LuaFriend(bot, friend.id) {
    constructor(bot: MiraiBot, id: Long) : this(bot, bot.bot.getFriend(id))

    init {
        rawset("nick", this.friend.nick.toLuaValue())
        rawset("avatarUrl", this.friend.avatarUrl.toLuaValue())
        rawset("isActive", this.friend.isActive.toLuaValue())
    }

    override fun getOpFunction(opcode: Int): OpFunction = generateOpFunction(opcode) { op, varargs ->
        varargs.checkArg<MiraiFriend>(1).let {
            when (opcode) {
                GET_NICK -> it.friend.nick.toLuaValue()
                GET_AVATAR_URL -> it.friend.avatarUrl.toLuaValue()
                IS_ACTIVE -> it.friend.isActive.toLuaValue()
                SEND_MESSAGE -> MiraiSource(it.friend.sendMessage(varargs.arg(2).checkMessageChain()))
                SEND_IMG -> MiraiMsg.uploadImage(varargs.arg(2), it)?.let { img ->
                    MiraiSource(it.friend.sendMessage(img))
                } ?: LuaValue.NIL
                else -> LuaValue.NIL
            }
        }
    }
}