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
    var qq: QQ

    constructor(bot: LuaBot, id: Long) : super(bot, id) {
        qq = (bot as MiraiBot).bot.getFriend(id);
    }

    constructor(bot: LuaBot, qq: QQ) : super(bot, qq.id) {
        this.qq = qq
    }

    override fun getOpFunction(opcode: Int): OpFunction {
        return object : OpFunction(opcode) {
            override fun op(varargs: Varargs): Varargs {
                var luaQQ: MiraiQQ = varargs.arg1() as MiraiQQ
                return when (opcode) {
                    GET_NICK -> LuaValue.valueOf(luaQQ.qq.nick)
                    GET_AVATAR_URL -> LuaValue.valueOf(luaQQ.qq.avatarUrl)
                    IS_ACTIVE -> LuaValue.valueOf(luaQQ.qq.isActive)
                    QUERY_REMARK -> runBlocking {
                        LuaValue.valueOf(luaQQ.qq.queryRemark().value)
                    }
                    QUERY_PROFILE -> runBlocking {
                        var profile = luaQQ.qq.queryProfile()
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
                    SEND_MESSAGE -> {
                        var msg = varargs.arg(2);
                        if (msg is LuaString) {
                            runBlocking {
                                println("准备发送消息：" + msg.checkjstring())
                                luaQQ.qq.sendMessage(PlainText(msg.checkjstring()))
                            }
                        } else if (msg is LuaMsg) {
                            //TODO
                        }
                        luaQQ
                    }
                    else -> LuaValue.NIL
                }
            }
        }
    }
}