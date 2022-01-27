package com.github.only52607.luamirai.core.script

import java.io.File
import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset

/**
 * ClassName: BotScriptSource
 * Description:
 * date: 2021/8/24 10:11
 * @author ooooonly
 * @version
 */

sealed class BotScriptSource(
    val scriptLang: String,
    var name: String,
    var size: Long?,
    var charset: Charset?
) {

    class FileSource(
        val file: File,
        scriptLang: String,
        name: String = file.name,
        charset: Charset = Charsets.UTF_8
    ) : BotScriptSource(scriptLang, name, file.length(), charset) {
        override fun toString(): String {
            return "FileSource(name=$name, file=$file, lang=$scriptLang)"
        }
    }

    class StringSource(
        val content: String,
        scriptLang: String,
        name: String = content,
        charset: Charset = Charsets.UTF_8
    ) : BotScriptSource(scriptLang, name, content.length.toLong(), charset) {
        override fun toString(): String {
            return "StringSource(name=$name, content=${content.hashCode()}, lang=$scriptLang)"
        }
    }

    class URLSource(
        val url: URL,
        scriptLang: String,
        name: String = url.toString(),
        charset: Charset = Charsets.UTF_8
    ) : BotScriptSource(scriptLang, name, null, charset) {
        override fun toString(): String {
            return "URLSource(name=$name, url=$url, lang=$scriptLang)"
        }
    }

    class InputStreamSource(
        val inputStream: InputStream,
        scriptLang: String,
        name: String = inputStream.toString(),
        charset: Charset = Charsets.UTF_8
    ) : BotScriptSource(scriptLang, name, null, charset) {
        override fun toString(): String {
            return "InputStreamSource(name=$name, inputStream=$inputStream, lang=$scriptLang)"
        }
    }

    abstract class CustomSource(
        val file: File,
        scriptLang: String,
        name: String = file.name,
        charset: Charset = Charsets.UTF_8
    ) : BotScriptSource(scriptLang, name, file.length(), charset)
}