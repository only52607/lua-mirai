package com.github.only52607.luamirai.lua

import com.github.only52607.luamirai.core.factory.BotScriptBuilder
import com.github.only52607.luamirai.core.factory.BotScriptBuilderRegistry
import com.github.only52607.luamirai.core.script.BotScript
import java.io.File
import java.io.InputStream
import java.net.URL

/**
 * ClassName: LuaMiraiScriptBuilder
 * Description:
 * date: 2022/1/13 21:37
 * @author ooooonly
 * @version
 */
@Suppress("unused")
object LuaMiraiScriptBuilder : BotScriptBuilder {

    init {
        BotScriptBuilderRegistry.registerBotScriptBuilder("lua", LuaMiraiScriptBuilder)
    }

    override suspend fun buildBotScript(file: File, chunkName: String): BotScript {
        val source = LuaSource.LuaFileSource(file).also { it.init() }
        return LuaMiraiScript(source)
    }

    override suspend fun buildBotScript(url: URL, chunkName: String): BotScript {
        val source = LuaSource.LuaURLSource(url, chunkName).also { it.init() }
        return LuaMiraiScript(source)
    }

    override suspend fun buildBotScript(content: String, chunkName: String): BotScript {
        val source = LuaSource.LuaContentSource(content, chunkName).also { it.init() }
        return LuaMiraiScript(source)
    }

    override suspend fun buildBotScript(inputStream: InputStream, chunkName: String): BotScript {
        val source = LuaSource.LuaInputStreamSource(inputStream, chunkName).also { it.init() }
        return LuaMiraiScript(source)
    }
}