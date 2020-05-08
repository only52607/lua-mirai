package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.utils.MessageAnalyzer
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.data.PlainText
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs

class MiraiGroup(var bot: MiraiBot, var group: Group) : LuaGroup(bot, group.id) {
    constructor(bot: MiraiBot, id: Long) : this(bot, bot.bot.getGroup(id))

    init {
        this.rawset("name", LuaValue.valueOf(this.group.name))
        this.rawset("avatarUrl", LuaValue.valueOf(this.group.avatarUrl))
    }

    override fun getOpFunction(opcode: Int): OpFunction = object : OpFunction(opcode) {
        override fun op(varargs: Varargs): Varargs = varargs.arg1().let {
            if (!(it is MiraiGroup)) throw LuaError("The reference object must be MiraiGroup")
            when (opcode) {
                GET_MEMBER -> MiraiGroupMember(it.get("bot") as MiraiBot, it, varargs.optlong(2, 0))
                GET_AVATAR_URL -> LuaValue.valueOf(it.group.avatarUrl)
                GET_BOT_AS_MEMBER -> MiraiGroupMember(it.get("bot") as MiraiBot, it, it.group.botAsMember)
                GET_BOT_MUTE_REMAIN -> LuaValue.valueOf(it.group.botMuteRemaining)
                GET_BOT_PERMISSION -> LuaValue.valueOf(it.group.botPermission.name)
                GET_NAME -> LuaValue.valueOf(it.group.name)
                GET_OWNER -> MiraiGroupMember(it.get("bot") as MiraiBot, it, it.group.owner)
                CONTAINS -> LuaValue.valueOf(it.group.contains(varargs.optlong(2, 0)))
                GET_MEMBER_OR_NULL -> it.group.getOrNull(varargs.optlong(2, 0))?.let { member ->
                    MiraiGroupMember(it.get("bot") as MiraiBot, it, member)
                } ?: LuaValue.NIL
                QUIT -> runBlocking { LuaValue.valueOf(it.group.quit()) }
                /*TO_FULL_STRING -> LuaValue.valueOf(luaGroup.group.to())*/
                SEND_MSG -> varargs.arg(2).let { msg ->
                    runBlocking {
                        when (msg) {
                            is LuaString -> it.group.sendMessage(PlainText(msg.checkjstring()))
                            is MiraiMsg -> it.group.sendMessage(msg.chain)
                            else -> null
                        }?.let {
                            MiraiSource(it)
                        } ?: throw LuaError("Unsupported message type,please use String or MiraiMsg!")
                    }
                }
                else -> LuaValue.NIL
            }
        }
    }
}