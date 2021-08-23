package com.ooooonly.luaMirai.base

import com.ooooonly.luaMirai.lua.LuaMiraiScript
import com.ooooonly.luaMirai.lua.LuaSource
import java.io.File
import java.io.InputStream
import java.io.PrintStream
import java.net.URL

/**
 * ClassName: BotScriptFactory
 * Description: Bot脚本工厂类
 * date: 2021/8/24 0:16
 * @author ooooonly
 * @version
 */
class BotScriptFactory {
    companion object {
        enum class ScriptLang {
            Lua
        }

        fun buildBotScriptFromFile(
            lang: ScriptLang,
            file: File,
            stdout: PrintStream? = System.out,
            stderr: PrintStream? = System.err,
            stdin: InputStream? = null
        ): BotScript = when(lang) {
            ScriptLang.Lua -> {
                LuaMiraiScript(LuaSource.LuaFileSource(file), stdout, stderr, stdin)
            }
        }

        fun buildBotScriptFromContent(
            lang: ScriptLang,
            content: String,
            stdout: PrintStream? = System.out,
            stderr: PrintStream? = System.err,
            stdin: InputStream? = null
        ): BotScript = when(lang) {
            ScriptLang.Lua -> {
                LuaMiraiScript(LuaSource.LuaContentSource(content), stdout, stderr, stdin)
            }
        }

        fun buildBotScriptFromURL(
            lang: ScriptLang,
            url: URL,
            stdout: PrintStream? = System.out,
            stderr: PrintStream? = System.err,
            stdin: InputStream? = null
        ): BotScript = when(lang) {
            ScriptLang.Lua -> {
                LuaMiraiScript(LuaSource.LuaURLSource(url), stdout, stderr, stdin)
            }
        }
    }
}

class UnknownScriptTypeException(override val message: String?):RuntimeException(message)