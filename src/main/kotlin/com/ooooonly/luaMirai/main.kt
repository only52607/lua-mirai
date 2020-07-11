package com.ooooonly.luaMirai

import com.ooooonly.luaMirai.lua.MiraiGlobals
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.join
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaValue


fun main(args: Array<String>) {
    val globals: Globals = MiraiGlobals()
    if (args.size == 0) {
        println("请输入lua脚本路径！")
        return
    }

    val func: LuaValue = globals.loadfile(args[0])
    if (func == LuaValue.NIL) {
        println("脚本无效！")
        return
    }
    func.call()
    Bot.forEachInstance { bot ->
        runBlocking {
            bot.join()
        }
    }


}



