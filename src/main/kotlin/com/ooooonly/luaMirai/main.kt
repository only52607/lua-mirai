package com.ooooonly.luaMirai

import com.ooooonly.luaMirai.lua.LuaMiraiScript
import net.mamoe.mirai.utils.MiraiExperimentalApi
import java.io.File

@MiraiExperimentalApi
fun main(args: Array<String>) {
    args.ifEmpty { throw Exception("请指定运行参数！") }
    when (args[0]) {
        "exec" -> if (args.size >= 2) {
            LuaMiraiScript(sourceFile = File(args[1])).load()
        } else throw Exception("请指定脚本路径！")
        else -> throw Exception("未知的运行参数！")
    }
}

