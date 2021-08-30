package com.ooooonly.luaMirai.base

import com.ooooonly.luaMirai.lua.LuaMiraiScript
import com.ooooonly.luaMirai.lua.LuaSource
import com.ooooonly.luaMirai.lua.LuaSourceFactory
import java.io.File
import java.io.InputStream
import java.io.PrintStream
import java.net.URL
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

/**
 * ClassName: BotScriptFactory
 * Description: Bot脚本工厂类
 * date: 2021/8/24 0:16
 * @author ooooonly
 * @version
 */

enum class ScriptLang(val langName: String) {
    Lua("lua")
}

fun ScriptLang.forName(name: String):ScriptLang = when(name) {
    ScriptLang.Lua.langName -> ScriptLang.Lua
    else -> throw ScriptLangNotFoundException("Script lang $name not found.")
}

class ScriptLangNotFoundException(message: String?): RuntimeException(message)

object BotScriptFactory {
    suspend fun buildBotScript(
        lang: ScriptLang,
        file: File,
        stdout: (LuaMiraiScript) -> PrintStream? = { System.out },
        stderr: (LuaMiraiScript) -> PrintStream? =  { System.err },
        stdin: (LuaMiraiScript) -> InputStream? =  { null },
        extraCoroutineContext: (LuaMiraiScript) -> CoroutineContext =  { EmptyCoroutineContext }
    ): BotScript = when(lang) {
        ScriptLang.Lua -> {
            LuaMiraiScript(LuaSourceFactory.buildSource(file), stdout, stderr, stdin, extraCoroutineContext)
        }
    }

    suspend fun buildBotScript(
        lang: ScriptLang,
        url: URL,
        stdout: (LuaMiraiScript) -> PrintStream? = { System.out },
        stderr: (LuaMiraiScript) -> PrintStream? =  { System.err },
        stdin: (LuaMiraiScript) -> InputStream? =  { null },
        extraCoroutineContext: (LuaMiraiScript) -> CoroutineContext =  { EmptyCoroutineContext }
    ): BotScript = when(lang) {
        ScriptLang.Lua -> {
            LuaMiraiScript(LuaSourceFactory.buildSource(url) as LuaSource, stdout, stderr, stdin, extraCoroutineContext)
        }
    }

    suspend fun buildBotScript(
        lang: ScriptLang,
        content: String,
        stdout: (LuaMiraiScript) -> PrintStream? = { System.out },
        stderr: (LuaMiraiScript) -> PrintStream? =  { System.err },
        stdin: (LuaMiraiScript) -> InputStream? =  { null },
        extraCoroutineContext: (LuaMiraiScript) -> CoroutineContext =  { EmptyCoroutineContext }
    ): BotScript = when(lang) {
        ScriptLang.Lua -> {
            LuaMiraiScript(LuaSourceFactory.buildSource(content) as LuaSource, stdout, stderr, stdin, extraCoroutineContext)
        }
    }

    suspend fun buildBotScript(
        lang: ScriptLang,
        inputStream: InputStream,
        stdout: (LuaMiraiScript) -> PrintStream? = { System.out },
        stderr: (LuaMiraiScript) -> PrintStream? =  { System.err },
        stdin: (LuaMiraiScript) -> InputStream? =  { null },
        extraCoroutineContext: (LuaMiraiScript) -> CoroutineContext =  { EmptyCoroutineContext }
    ): BotScript = when(lang) {
        ScriptLang.Lua -> {
            LuaMiraiScript(LuaSourceFactory.buildSource(inputStream) as LuaSource, stdout, stderr, stdin, extraCoroutineContext)
        }
    }
}