package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.base.*
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaValue
import java.io.File
import java.io.FileInputStream
import java.io.FileReader
import java.io.InputStreamReader
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
        val headerStartRegx by lazy {
            Regex("^--\\s*LuaMiraiScript\\s*--$")
        }

        val headerEndRegx by lazy {
            Regex("^--\\s*/LuaMiraiScript\\s*--$")
        }

        val headerFieldRegx by lazy {
            Regex("^--\\s*([^:：]+)\\s*[:：]\\s*(.*)\\s*$")
        }

        val luaCharset = Charsets.UTF_8
    }

    abstract val chunkName: String

    /**
     * Lua脚本头部格式示例:
     * -- LuaMiraiScript --
     * -- name: ScriptExample
     * -- version: 1.0
     * -- description: xxxx
     * -- author: ooooonly
     * -- /LuaMiraiScript --
     */
    abstract fun getHeader(): BotScriptHeader

    abstract fun load(globals: Globals): LuaValue

    abstract fun copy(): LuaSource

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

    class LuaFileSource(override val file: File) : LuaSource(), BotScriptFileSource {
        constructor(filePath: String) : this(File(filePath))

        override val chunkName: String
            get() = file.name

        override fun load(globals: Globals): LuaValue {
            return globals.loadfile(file.absolutePath)
        }

        private val _header: BotScriptHeader by lazy {
            val reader = InputStreamReader(FileInputStream(file), luaCharset)
            val botScriptHeader:MutableBotScriptHeader = reader.useLines { lines: Sequence<String> -> lines.parseBotScriptHeader() }
            if (botScriptHeader["name"] == null) {
                botScriptHeader["name"] = file.name
            }
            return@lazy botScriptHeader
        }

        override fun getHeader(): BotScriptHeader {
            return _header
        }

        override fun copy(): LuaFileSource {
            return LuaFileSource(file)
        }
    }

    class LuaContentSource(override val content: String) : LuaSource(), BotScriptContentSource {
        override val chunkName: String
            get() = "{ content }"

        override fun load(globals: Globals): LuaValue {
            return globals.load(content)
        }

        private val _header: BotScriptHeader by lazy {
            content.splitToSequence("\n").parseBotScriptHeader()
        }

        override fun getHeader(): BotScriptHeader {
            return _header
        }

        override fun copy(): LuaContentSource {
            return LuaContentSource(content)
        }
    }

    class LuaURLSource(override val url: URL, val charset: Charset = luaCharset) : LuaSource(), BotScriptURLSource {
        override val chunkName: String
            get() = url.path

        private val content by lazy {
            url.readText(charset)
        }

        private val _header: BotScriptHeader by lazy {
            content.splitToSequence("\n").parseBotScriptHeader()
        }

        override fun load(globals: Globals): LuaValue {
            return globals.load(content)
        }

        override fun getHeader(): BotScriptHeader {
            return _header
        }

        override fun copy(): LuaURLSource {
            return LuaURLSource(url)
        }
    }
}

class InvalidScriptHeaderException(override val message: String): RuntimeException(message)

