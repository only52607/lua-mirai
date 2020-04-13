package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.lua.LuaGroup.*
import com.ooooonly.luaMirai.utils.MessageAnalyzer
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.QQ
import net.mamoe.mirai.message.MessageReceipt
import net.mamoe.mirai.message.data.PlainText
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs

class MiraiGroup : LuaGroup {
    var group: Group

    constructor(bot: LuaBot, id: Long) : super(bot, id) {
        group = (bot as MiraiBot).bot.getGroup(id);
    }

    constructor(bot: LuaBot, group: Group) : super(bot, group.id) {
        this.group = group
    }

    override fun getOpFunction(opcode: Int): OpFunction {
        return object : OpFunction(opcode) {
            override fun op(varargs: Varargs): Varargs {
                var luaGroup = varargs.arg1() as MiraiGroup
                return when (opcode) {
                    GET_MEMBER -> MiraiGroupMember(luaGroup.get("bot") as MiraiBot, luaGroup, varargs.optlong(2, 0))
                    GET_AVATAR_URL -> LuaValue.valueOf(luaGroup.group.avatarUrl)
                    GET_BOT_AS_MEMBER -> MiraiGroupMember(
                        luaGroup.get("bot") as MiraiBot,
                        luaGroup,
                        luaGroup.group.botAsMember
                    )
                    GET_BOT_MUTE_REMAIN -> LuaValue.valueOf(luaGroup.group.botMuteRemaining)
                    GET_BOT_PERMISSION -> LuaValue.valueOf(luaGroup.group.botPermission.name)
                    GET_NAME -> LuaValue.valueOf(luaGroup.group.name)
                    GET_OWNER -> MiraiGroupMember(luaGroup.get("bot") as MiraiBot, luaGroup, luaGroup.group.owner)
                    CONTAINS -> LuaValue.valueOf(luaGroup.group.contains(varargs.optlong(2, 0)))
                    GET_MEMBER_OR_NULL -> {
                        var value = luaGroup.group.getOrNull(varargs.optlong(2, 0))
                        if (value != null) MiraiGroupMember(luaGroup.get("bot") as MiraiBot, luaGroup, value)
                        else LuaValue.NIL
                    }
                    QUIT -> runBlocking {
                        LuaValue.valueOf(luaGroup.group.quit())
                    }
                    TO_FULL_STRING -> LuaValue.valueOf(luaGroup.group.toFullString())
                    SEND_MSG -> {
                        var msg = varargs.arg(2);
                        var receipt: MessageReceipt<QQ>? = null
                        if (msg is LuaString) {
                            runBlocking {
                                println("准备发送群消息：" + msg.checkjstring())
                                receipt = (luaGroup as MiraiGroup).group.sendMessage(
                                    MessageAnalyzer.toMessageChain(
                                        msg.checkjstring(),
                                        luaGroup.group
                                    )
                                )
                            }
                        } else if (msg is LuaMsg) {
                            runBlocking {
                                receipt = luaGroup.group.sendMessage((msg as MiraiMsg).getChain(luaGroup.group))
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