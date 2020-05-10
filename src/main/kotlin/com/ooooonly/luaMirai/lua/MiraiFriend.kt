package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.utils.MessageAnalyzer
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.Friend

import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.data.PlainText
import org.luaj.vm2.*

class MiraiFriend(var bot: MiraiBot, var friend: Friend) : LuaFriend(bot, friend.id) {
    constructor(bot: MiraiBot, id: Long) : this(bot, bot.bot.getFriend(id))

    init {
        this.rawset("nick", LuaValue.valueOf(this.friend.nick))
        this.rawset("avatarUrl", LuaValue.valueOf(this.friend.avatarUrl))
        this.rawset("isActive", LuaValue.valueOf(this.friend.isActive))
    }

    override fun getOpFunction(opcode: Int): OpFunction = object : OpFunction(opcode) {
        override fun op(varargs: Varargs): Varargs = varargs.arg1().let {
            if (it !is MiraiFriend) throw LuaError("The reference object must be MiraiFriend")
            when (opcode) {
                GET_NICK -> LuaValue.valueOf(it.friend.nick)
                GET_AVATAR_URL -> LuaValue.valueOf(it.friend.avatarUrl)
                IS_ACTIVE -> LuaValue.valueOf(it.friend.isActive)
                SEND_MESSAGE -> varargs.arg(2).let { msg ->
                    runBlocking {
                        when (msg) {
                            is LuaString -> it.friend.sendMessage(PlainText(msg.checkjstring()))
                            is MiraiMsg -> it.friend.sendMessage(msg.chain)
                            else -> null
                        }?.let {
                            MiraiSource(it)
                        } ?: throw LuaError("Unsupported message type,please use String or MiraiMsg!")
                    }
                }
                else -> LuaValue.NIL
                /*
                QUERY_REMARK -> runBlocking {
                    LuaValue.valueOf(luaFriend.qq.queryRemark().value)
                }
                QUERY_PROFILE -> runBlocking {
                    var profile = luaFriend.qq.
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
                }*/
            }
        }
    }
}