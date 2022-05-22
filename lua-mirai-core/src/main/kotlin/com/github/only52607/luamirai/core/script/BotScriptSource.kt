package com.github.only52607.luamirai.core.script

import java.io.ByteArrayInputStream
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

abstract class BotScriptSource(
    val lang: String,
    var name: String,
    open var size: Long?,
    open val charset: Charset?,
    val resourceFinder: BotScriptResourceFinder? = null
) {
    abstract val mainInputStream: InputStream

    class FileSource(
        val file: File,
        scriptLang: String,
        name: String = "@${file.name}",
        charset: Charset = Charsets.UTF_8
    ) : BotScriptSource(scriptLang, name, file.length(), charset) {
        override val mainInputStream: InputStream
            get() = file.inputStream()

        override fun toString(): String {
            return "FileSource(name=$name, file=$file, lang=$lang)"
        }
    }

    class StringSource(
        val content: String,
        lang: String,
        name: String = content,
        override val charset: Charset = Charsets.UTF_8
    ) : BotScriptSource(lang, name, content.length.toLong(), charset) {
        override val mainInputStream: InputStream
            get() = ByteArrayInputStream(content.toByteArray(charset = charset))

        override fun toString(): String {
            return "StringSource(name=$name, content=${content.hashCode()}, lang=$lang)"
        }
    }

    class URLSource(
        val url: URL,
        lang: String,
        name: String = url.toString(),
        override val charset: Charset = Charsets.UTF_8
    ) : BotScriptSource(lang, name, null, charset) {
        override val mainInputStream: InputStream
            get() = url.openStream()

        override fun toString(): String {
            return "URLSource(name=$name, url=$url, lang=$lang)"
        }
    }
}