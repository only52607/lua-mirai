package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.base.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaValue
import java.io.*
import java.net.URL
import java.nio.charset.Charset

/**
 * ClassName: LuaSource
 * Description: Lua脚本源，提供了读取Lua脚本内容的方法
 * date: 2021/8/15 11:28
 * @author ooooonly
 * @version
 */
sealed class LuaSource: BotScriptSource {
    companion object {
        private val headerStartRegx by lazy {
            Regex("^--\\s*LuaMiraiScript\\s*--$")
        }

        private val headerEndRegx by lazy {
            Regex("^--\\s*/LuaMiraiScript\\s*--$")
        }

        private val headerFieldRegx by lazy {
            Regex("^--\\s*([^:：]+)\\s*[:：]\\s*(.*)\\s*$")
        }

        val luaCharset = Charsets.UTF_8

        private fun matchHeaderStart(line: String): Boolean {
            return line.matches(headerStartRegx)
        }

        private fun matchHeaderEnd(line: String): Boolean {
            return line.matches(headerEndRegx)
        }

        private fun MutableBotScriptHeader.addFieldByLine(line: String) {
            val matchResult = headerFieldRegx.find(line) ?: return
            val groupValues = matchResult.groupValues
            this[groupValues[1]] = groupValues[2]
        }

        protected fun Sequence<String>.parseBotScriptHeader(): MutableBotScriptHeader = mutableBotScriptHeaderOf {
            val iterator = this@parseBotScriptHeader.iterator()
            if (!iterator.hasNext() || !matchHeaderStart(iterator.next())) return@mutableBotScriptHeaderOf
            while (iterator.hasNext()) {
                val line = iterator.next()
                if (matchHeaderEnd(line)) return@mutableBotScriptHeaderOf
                addFieldByLine(line)
            }
            throw InvalidScriptHeaderException("Missing header end label!")
        }
    }

    abstract override val chunkName: String

    abstract override val size: Long

    /**
     * Lua脚本头部
     * 示例:
     * -- LuaMiraiScript --
     * -- name: ScriptExample
     * -- version: 1.0
     * -- description: xxxx
     * -- author: ooooonly
     * -- /LuaMiraiScript --
     */
    abstract val header: BotScriptHeader

    abstract suspend fun load(globals: Globals): LuaValue

    abstract fun copy(): LuaSource

    internal abstract suspend fun init()

    class LuaFileSource internal constructor(override val file: File) : LuaSource(), BotScriptFileSource {
        constructor(filePath: String) : this(File(filePath))

        override val chunkName: String
            get() = file.name

        override val size: Long
            get() = file.length()

        private lateinit var _header: MutableBotScriptHeader
        override val header: BotScriptHeader
            get() = _header

        override suspend fun load(globals: Globals): LuaValue {
            return withContext(Dispatchers.IO){
                globals.loadfile(file.absolutePath)
            }
        }

        override fun copy(): LuaFileSource {
            return LuaFileSource(file)
        }

        override suspend fun init() {
            withContext(Dispatchers.IO){
                _header = file.useLines(charset = luaCharset) { it.parseBotScriptHeader() }
                if (_header["name"] == null) {
                    _header["name"] = file.name
                }
            }
        }
    }

    class LuaContentSource internal constructor(override val content: String, override val chunkName: String) : LuaSource(), BotScriptContentSource {
        override val size: Long
            get() = content.length.toLong()

        private lateinit var _header: MutableBotScriptHeader
        override val header: BotScriptHeader
            get() = _header

        override suspend fun load(globals: Globals): LuaValue {
            return withContext(Dispatchers.IO){
                globals.load(content, chunkName)
            }
        }

        override fun copy(): LuaContentSource {
            return LuaContentSource(content, chunkName)
        }

        override suspend fun init() {
            _header = content.splitToSequence("\n").parseBotScriptHeader()
        }
    }

    class LuaURLSource internal constructor(override val url: URL, override val chunkName: String) : LuaSource(), BotScriptURLSource {

        private lateinit var content:String

        private lateinit var _header: MutableBotScriptHeader
        override val header: BotScriptHeader
            get() = _header

        override val size: Long
            get() = content.length.toLong()

        override suspend fun load(globals: Globals): LuaValue {
            return withContext(Dispatchers.IO){
                globals.load(content, chunkName)
            }
        }

        override fun copy(): LuaURLSource {
            return LuaURLSource(url, chunkName)
        }

        override suspend fun init() {
            withContext(Dispatchers.IO) {
                content = url.readText(luaCharset)
                _header = content.splitToSequence("\n").parseBotScriptHeader()
            }
        }
    }

    class LuaInputStreamSource internal constructor(override val inputStream: InputStream, override val chunkName: String) : LuaSource(), BotScriptInputStreamSource {

        private lateinit var content:String

        private lateinit var _header: MutableBotScriptHeader
        override val header: BotScriptHeader
            get() = _header

        override val size: Long
            get() = content.length.toLong()

        override suspend fun load(globals: Globals): LuaValue {
            return withContext(Dispatchers.IO){
                globals.load(content, chunkName)
            }
        }

        override fun copy(): LuaContentSource {
            return LuaContentSource(content, chunkName)
        }

        override suspend fun init() {
            withContext(Dispatchers.IO) {
                content = inputStream.readBytes().toString(luaCharset)
                _header = content.splitToSequence("\n").parseBotScriptHeader()
            }
        }
    }
}

class InvalidScriptHeaderException(override val message: String): RuntimeException(message)