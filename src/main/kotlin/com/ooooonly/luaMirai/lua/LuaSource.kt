package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.base.BotScriptHeader
import com.ooooonly.luaMirai.base.MutableBotScriptHeader
import com.ooooonly.luaMirai.base.mutableBotScriptHeaderOf
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaValue
import java.io.File
import java.io.FileReader
import java.net.URL
import java.nio.charset.Charset

/**
 * ClassName: LuaSource
 * Description: Lua脚本源，提供了读取Lua脚本内容的方法
 * date: 2021/8/15 11:28
 * @author ooooonly
 * @version
 */
sealed class LuaSource {
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

    class LuaFileSource(val file: File) : LuaSource() {
        constructor(filePath: String) : this(File(filePath))

        override val chunkName: String
            get() = file.name

        override fun load(globals: Globals): LuaValue {
            return globals.loadfile(file.absolutePath)
        }

        private val _header: BotScriptHeader by lazy {
            FileReader(file).useLines { it.parseBotScriptHeader() }.also {
                if (it["name"] == null) {
                    it["name"] = file.name
                }
            }
        }

        override fun getHeader(): BotScriptHeader {
            return _header
        }

        override fun copy(): LuaFileSource {
            return LuaFileSource(file)
        }
    }

    class LuaContentSource(val content: String) : LuaSource() {
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

    class LuaURLSource(val url: URL, val charset: Charset = Charsets.UTF_8) : LuaSource() {
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

