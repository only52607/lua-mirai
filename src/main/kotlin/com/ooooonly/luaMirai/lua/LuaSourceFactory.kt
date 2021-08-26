package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.base.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.mamoe.mirai.message.data.MessageSource
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaValue
import java.io.*
import java.net.URL
import java.nio.charset.Charset

/**
 * ClassName: LuaSourceFactory
 * Description:
 * date: 2021/8/15 11:28
 * @author ooooonly
 * @version
 */
object LuaSourceFactory: AbstractBotScriptSourceFactory() {

    override suspend fun buildSource(file: File): LuaSource.LuaFileSource {
        return LuaSource.LuaFileSource(file).also { it.init() }
    }

    override suspend fun buildSource(url: URL, chunkName: String): LuaSource.LuaURLSource {
        return LuaSource.LuaURLSource(url, chunkName).also { it.init() }
    }

    override suspend fun buildSource(content: String, chunkName: String): LuaSource.LuaContentSource {
        return LuaSource.LuaContentSource(content, chunkName).also { it.init() }
    }

    override suspend fun buildSource(inputStream: InputStream, chunkName: String): LuaSource.LuaInputStreamSource {
        return LuaSource.LuaInputStreamSource(inputStream, chunkName).also { it.init() }
    }
}