package com.github.only52607.luamirai.lua

import com.github.only52607.luamirai.core.script.BotScriptHeader
import com.github.only52607.luamirai.core.script.BotScriptSource
import com.github.only52607.luamirai.core.script.MutableBotScriptHeader
import com.github.only52607.luamirai.core.script.mutableBotScriptHeaderOf

/**
 * ClassName: LuaHeaderReader
 * Description:
 * date: 2022/1/27 22:49
 * @author ooooonly
 * @version
 */
object LuaHeaderReader {
    private val REGEX_HEADER_START = Regex("^\\s*--\\s*LuaMiraiScript\\s*--\\s*$")
    private val REGEX_HEADER_END = Regex("^\\s*--\\s*/LuaMiraiScript\\s*--\\s*$")
    private val REGEX_HEADER_FIELD = Regex("^\\s*--\\s*([^:：]+)\\s*[:：]\\s*(.*)\\s*$")

    private fun MutableBotScriptHeader.addFieldByLine(line: String) {
        val matchResult = REGEX_HEADER_FIELD.find(line) ?: return
        val groupValues = matchResult.groupValues
        this[groupValues[1]] = groupValues[2]
    }

    private fun parseBotScriptHeader(lines: Sequence<String>): MutableBotScriptHeader = mutableBotScriptHeaderOf {
        val iterator = lines.iterator()
        if (!iterator.hasNext() || !iterator.next().matches(REGEX_HEADER_START)) return@mutableBotScriptHeaderOf
        while (iterator.hasNext()) {
            val line = iterator.next()
            if (line.matches(REGEX_HEADER_END)) return@mutableBotScriptHeaderOf
            addFieldByLine(line)
        }
        throw InvalidScriptHeaderException("Missing header end label!")
    }

    fun readHeader(source: BotScriptSource, readSourceString: (String) -> Unit): BotScriptHeader = when (source) {
        is BotScriptSource.FileSource -> source.file
            .readText(source.charset ?: Charsets.UTF_8)
            .also(readSourceString)
            .splitToSequence("\n")
            .let(::parseBotScriptHeader)
        is BotScriptSource.StringSource -> source.content
            .splitToSequence("\n")
            .let(::parseBotScriptHeader)
        is BotScriptSource.URLSource -> source.url
            .readText(source.charset ?: Charsets.UTF_8)
            .also(readSourceString)
            .also { source.size = it.length.toLong() }
            .splitToSequence("\n")
            .let(::parseBotScriptHeader)
        is BotScriptSource.InputStreamSource -> source.inputStream
            .readBytes()
            .toString(source.charset ?: Charsets.UTF_8)
            .also(readSourceString)
            .also { source.size = it.length.toLong() }
            .splitToSequence("\n")
            .let(::parseBotScriptHeader)
        else -> throw Exception("Unsupported BotScriptSource $source")
    }
}

class InvalidScriptHeaderException(override val message: String) : RuntimeException(message)