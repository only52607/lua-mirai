package com.ooooonly.luaMirai.lua.lib


import com.ooooonly.luaMirai.lua.LuaBot
import com.ooooonly.luaMirai.lua.MiraiBot
import org.luaj.vm2.LuaValue


class MiraiBotLib : BotLib() {
    override fun getNewBotFunction(): MiraiNewBotFunction {
        return MiraiNewBotFunction()
    }
}

class MiraiNewBotFunction : BotLib.NewBotFunction() {
    override fun newBot(qq: LuaValue?, password: LuaValue?): LuaBot {
        var qqLong: Long = qq?.checklong() ?: 0
        var passwordString: String = password?.checkjstring() ?: ""
        return MiraiBot(qqLong, passwordString)
    }
}