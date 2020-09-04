package com.ooooonly.luaMirai

import com.ooooonly.luaMirai.lua.MiraiCoreGlobals
import com.ooooonly.luakt.runLuaFile
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.Bot
import net.mamoe.mirai.join


fun main(args: Array<String>) {
    args.ifEmpty { throw Exception("请指定运行参数！") }
    when (args[0]) {
        "exec" -> if (args.size >= 2) execFile(args[1]) else throw Exception("请指定脚本路径！")
        else -> throw Exception("未知的运行参数！")
    }
}

fun execFile(filePath: String) {
    runLuaFile(filePath, MiraiCoreGlobals())
    Bot.forEachInstance { bot ->
        runBlocking {
            bot.join()
        }
    }
}
