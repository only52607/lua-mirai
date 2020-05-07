package com.ooooonly.luaMirai.lua.lib


import com.ooooonly.luaMirai.lua.LuaBot
import com.ooooonly.luaMirai.lua.MiraiBot
import com.ooooonly.luaMirai.lua.MiraiMsg
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.VarArgFunction


class MiraiBotLib : BotLib() {
    override fun call(modName: LuaValue?, env: LuaValue): LuaValue? {
        val globals = env.checkglobals()
        globals.set("Msg", object : VarArgFunction() {
            override fun onInvoke(args: Varargs): Varargs {
                val arg1 = args.arg1()
                if (arg1 is LuaTable) return MiraiMsg(arg1)
                else if (arg1 is LuaString) return MiraiMsg(arg1.toString())
                return MiraiMsg()
            }
        })
        return LuaValue.NIL
    }
    /*
    override fun getNewBotFunction(): BotLib.NewBotFunction {
        return object : BotLib.NewBotFunction() {
            override fun newBot(qq: LuaValue?, password: LuaValue?): LuaBot {
                var qqLong: Long = qq?.checklong() ?: 0
                var passwordString: String = password?.checkjstring() ?: ""
                return MiraiBot(qqLong, passwordString)
            }
        }
    }*/
}

