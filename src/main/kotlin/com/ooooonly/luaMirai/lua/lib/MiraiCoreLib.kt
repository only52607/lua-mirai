package com.ooooonly.luaMirai.lua.lib

import com.ooooonly.luaMirai.lua.bridge.coreimpl.*
import com.ooooonly.luakt.LuaValueConverter
import com.ooooonly.luakt.asLuaValue
import com.ooooonly.luakt.register
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.message.data.MessageChain
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

open class MiraiCoreLib : TwoArgFunction() {
    override fun call(modName: LuaValue?, env: LuaValue): LuaValue? {
        val globals = env.checkglobals()
        BotCoreImpl.setBotFactory(globals) //载入Bot构建函数
        MessageCoreImpl.setMsgConstructor(globals) //载入Msg构建函数

        //全局对象拦截器
        object : LuaValueConverter {
            override fun caseToLuaValue(obj: Any): LuaValue? {
                if (obj !is Bot) return null
                return BotCoreImpl(obj).asLuaValue()
            }
        }.register()

        object : LuaValueConverter {
            override fun caseToLuaValue(obj: Any): LuaValue? {
                if (obj !is Bot) return null
                return BotCoreImpl(obj).asLuaValue()
            }
        }.register()

        object : LuaValueConverter {
            override fun caseToLuaValue(obj: Any): LuaValue? {
                if (obj !is Friend) return null
                return FriendCoreImpl(obj).asLuaValue()
            }
        }.register()

        object : LuaValueConverter {
            override fun caseToLuaValue(obj: Any): LuaValue? {
                if (obj !is Group) return null
                return GroupCoreImpl(obj).asLuaValue()
            }
        }.register()

        object : LuaValueConverter {
            override fun caseToLuaValue(obj: Any): LuaValue? {
                if (obj !is MessageChain) return null
                return MessageCoreImpl(obj).asLuaValue()
            }
        }.register()

        return LuaValue.NIL
    }
}