package com.ooooonly.luaMirai.lua.lib

import org.luaj.vm2.LuaValue


open class MiraiBotLib : BotLib() {
    override fun call(modName: LuaValue?, env: LuaValue): LuaValue? {
        val globals = env.checkglobals()
        /*
        globals.set("Msg", object : VarArgFunction() {
            override fun onInvoke(args: Varargs): Varargs {
                val arg1 = args.arg1()
                if (arg1 is LuaTable) return MiraiMsg(arg1)
                else if (arg1 is LuaString) return MiraiMsg(arg1.toString())
                return MiraiMsg()
            }
        })

         */
        return LuaValue.NIL
    }
}

