package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.base.AbstractBotScriptManager
import net.mamoe.mirai.utils.MiraiInternalApi
import java.io.FileNotFoundException

@Suppress("unused")
@MiraiInternalApi
class LuaMiraiBotScriptManager : AbstractBotScriptManager<LuaMiraiScript, BotScriptInfo, LuaSource>() {
    override fun add(source: LuaSource): Int {
        if (source is LuaSource.LuaFileSource && !source.file.exists()) throw FileNotFoundException("文件 ${source.file.absolutePath} 不存在！")
        val script = LuaMiraiScript(source)
        val index = appendScript(script)
        script.create()
        return index
    }
}