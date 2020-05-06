package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.utils.MessageAnalyzer
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.Friend

import net.mamoe.mirai.message.MessageReceipt
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs

class MiraiFriend : LuaFriend {
    var qq: Friend

    constructor(bot: LuaBot, id: Long) : super(bot, id) {
        qq = (bot as MiraiBot).bot.getFriend(id);
    }

    constructor(bot: LuaBot, qq: Friend) : super(bot, qq.id) {
        this.qq = qq
    }

    override fun getOpFunction(opcode: Int): OpFunction {
        return object : OpFunction(opcode) {
            override fun op(varargs: Varargs): Varargs {
                var luaFriend: MiraiFriend = varargs.arg1() as MiraiFriend
                return when (opcode) {
                    GET_NICK -> LuaValue.valueOf(luaFriend.qq.nick)
                    GET_AVATAR_URL -> LuaValue.valueOf(luaFriend.qq.avatarUrl)
                    IS_ACTIVE -> LuaValue.valueOf(luaFriend.qq.isActive)
                    /*
                    QUERY_REMARK -> runBlocking {
                        LuaValue.valueOf(luaFriend.qq.queryRemark().value)
                    }*/
                    /*
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
                    SEND_MESSAGE -> {
                        var msg = varargs.arg(2);
                        var receipt: MessageReceipt<Friend>? = null
                        if (msg is LuaString) {
                            runBlocking {
                                println("准备发送消息LuaString：" + msg.checkjstring())
                                var chain = MessageAnalyzer.toMessageChain(msg.checkjstring(), luaFriend.qq)
                                receipt = luaFriend.qq.sendMessage(chain)
                            }
                        } else if (msg is LuaMsg) {
                            runBlocking {
                                println("准备发送消息LuaMsg：$msg")
                                var chain = (msg as MiraiMsg).getChain(luaFriend.qq)
                                receipt = luaFriend.qq.sendMessage(chain)
                            }
                        }
                        receipt?.let { MiraiSource(it) } ?: LuaValue.NIL
                    }
                    else -> LuaValue.NIL
                }
            }
        }
    }
}