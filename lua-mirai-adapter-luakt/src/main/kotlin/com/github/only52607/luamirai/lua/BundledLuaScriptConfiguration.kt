package com.github.only52607.luamirai.lua

import com.github.only52607.luamirai.core.ScriptConfiguration
import java.io.InputStream
import java.util.*

class BundledLuaScriptConfiguration private constructor(
    private val map: Map<String, String>
): ScriptConfiguration {
    companion object {
        private val REGEX_HEADER_START = Regex("^\\s*--\\s*LuaMiraiScript\\s*--\\s*$")
        private val REGEX_HEADER_END = Regex("^\\s*--\\s*/LuaMiraiScript\\s*--\\s*$")
        private val REGEX_HEADER_FIELD = Regex("^\\s*--\\s*([^:：]+)\\s*[:：]\\s*(.*)\\s*$")

        fun from(main: InputStream): BundledLuaScriptConfiguration {
            val map = mutableMapOf<String, String>()

            fun processLine(line: String) {
                val matchResult = REGEX_HEADER_FIELD.find(line) ?: return
                val groupValues = matchResult.groupValues
                map[groupValues[1]] = groupValues[2]
            }
            val scanner = Scanner(main)
            if (!scanner.hasNextLine() || !scanner.nextLine().matches(REGEX_HEADER_START)) {
                return BundledLuaScriptConfiguration(map)
            }
            while (scanner.hasNextLine()) {
                val line = scanner.nextLine()
                if (line.matches(REGEX_HEADER_END)) {
                    return BundledLuaScriptConfiguration(map)
                }
                processLine(line)
            }
            throw InvalidScriptHeaderException("Missing header end label!")
        }
    }

    override fun getStringOrNull(key: String): String? {
        return map[key]
    }
}

class InvalidScriptHeaderException(override val message: String) : RuntimeException(message)