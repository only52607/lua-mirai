package com.ooooonly.luaMirai.lua.lib

import com.ooooonly.luaMirai.lua.bridge.coreimpl.*
import com.ooooonly.luakt.LuaValueConverter
import com.ooooonly.luakt.asLuaValue
import com.ooooonly.luakt.register
import net.mamoe.mirai.Bot
import net.mamoe.mirai.contact.Friend
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.message.data.MessageChain
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.TwoArgFunction

open class MiraiCoreLib : TwoArgFunction() {
    override fun call(modName: LuaValue?, env: LuaValue): LuaValue? {
        val globals = env.checkglobals()
        BotCoreImpl.setBotFactory(globals) //载入Bot构建函数
        MessageCoreImpl.setMsgConstructor(globals) //载入Msg构建函数
        //注册全局对象拦截器
        object : LuaValueConverter {
            override fun caseToLuaValue(obj: Any): LuaValue? = when (obj) {
                is Bot -> BotCoreImpl(obj).asLuaValue()
                is Friend -> FriendCoreImpl(obj).asLuaValue()
                is Group -> GroupCoreImpl(obj).asLuaValue()
                is Member -> MemberCoreImpl(obj).asLuaValue()
                is MessageChain -> MessageCoreImpl(obj).asLuaValue()
                else -> null
            }
        }.register()
        return LuaValue.NIL
    }
}