package com.github.only52607.luamirai.core.script

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.net.URL
import java.nio.charset.Charset
import java.util.zip.ZipFile

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
) {
    abstract val mainInputStream: InputStream

    open val resourceFinder: BotScriptResourceFinder? = null

    class FileSource(
        val file: File,
        scriptLang: String,
        name: String = "@${file.name}",
        charset: Charset = Charsets.UTF_8
    ) : BotScriptSource(scriptLang, name, file.length(), charset) {

        private val scriptSource: BotScriptSource = when {
            file.isDirectory -> DirectorySource(file, scriptLang, name, charset)
            isPackage() -> ZipSource(file, scriptLang, name, charset)
            else -> TextFileSource(file, scriptLang, name, charset)
        }

        private fun isPackage(): Boolean {
            return file.name.endsWith(".zip") || file.name.endsWith(".lmpk")
        }

        override val mainInputStream: InputStream
            get() = scriptSource.mainInputStream

        override val resourceFinder: BotScriptResourceFinder?
            get() = scriptSource.resourceFinder

        override fun toString(): String {
            return "FileSource(name=$name, file=$file, lang=$lang)"
        }
    }

    class TextFileSource(
        val file: File,
        scriptLang: String,
        name: String = "@${file.name}",
        charset: Charset = Charsets.UTF_8
    ) : BotScriptSource(scriptLang, name, file.length(), charset) {
        override val resourceFinder: BotScriptResourceFinder = object : BotScriptResourceFinder {
            val directory = file.parentFile
            override fun findResource(filename: String): InputStream? {
                val file = File(directory, filename)
                if (!file.exists()) return null
                return file.inputStream()
            }
        }

        override val mainInputStream: InputStream
            get() = file.inputStream()

        override fun toString(): String {
            return "TextFileSource(name=$name, file=$file, lang=$lang)"
        }
    }

    class DirectorySource(
        val directory: File,
        scriptLang: String,
        name: String = "@${directory.name}",
        charset: Charset = Charsets.UTF_8
    ) : BotScriptSource(scriptLang, name, directory.length(), charset) {

        override val resourceFinder: BotScriptResourceFinder = object : BotScriptResourceFinder {
            override fun findResource(filename: String): InputStream? {
                val file = File(directory, filename)
                if (!file.exists()) return null
                return file.inputStream()
            }
        }

        private val json = Json

        override val mainInputStream: InputStream
            get() = resourceFinder.findResource(mainFileName)
                ?: throw FileNotFoundException("File $mainFileName not found in ${directory.absolutePath}")

        private val manifest: JsonObject = resourceFinder.findResource("manifest.json")?.let {
            json.parseToJsonElement(String(it.readBytes())).jsonObject
        } ?: throw FileNotFoundException("manifest.json not found in ${directory.absolutePath}")

        private val mainFileName: String = manifest["main"]?.jsonPrimitive?.content
            ?: throw Exception("You must specify the main field as the script entry")

        init {
            super.name = mainFileName
        }

        override fun toString(): String {
            return "DirectorySource(name=$name, directory=$directory, lang=$lang)"
        }
    }

    class ZipSource(
        val file: File,
        scriptLang: String,
        name: String = "@${file.name}",
        charset: Charset = Charsets.UTF_8
    ) : BotScriptSource(scriptLang, name, file.length(), charset) {

        override val resourceFinder: BotScriptResourceFinder = object : BotScriptResourceFinder {
            override fun findResource(filename: String): InputStream? {
                val zipEntryName = filename.replace("\\", "/")
                val zipEntry = zipFile.getEntry(zipEntryName) ?: return null
                return zipFile.getInputStream(zipEntry)
            }
        }

        private val zipFile = ZipFile(file)

        private val json = Json

        private val manifest: JsonObject = resourceFinder.findResource("manifest.json")?.let {
            json.parseToJsonElement(String(it.readBytes())).jsonObject
        } ?: throw FileNotFoundException("manifest.json not found in ${file.absolutePath}")

        private val mainFileName: String = manifest["main"]?.jsonPrimitive?.content
            ?: throw Exception("You must specify the main field as the script entry")

        init {
            super.name = mainFileName
        }

        override val mainInputStream: InputStream
            get() = resourceFinder.findResource(mainFileName)
                ?: throw FileNotFoundException("File $mainFileName not found in ${file.absolutePath}")

        override fun toString(): String {
            return "ZipSource(name=$name, file=$file, lang=$lang)"
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

    open class Wrapper(
        val source: BotScriptSource
    ) : BotScriptSource(
        lang = source.lang,
        name = source.name,
        size = source.size,
        charset = source.charset,
    ) {
        override val mainInputStream: InputStream
            get() = source.mainInputStream

        override val resourceFinder: BotScriptResourceFinder?
            get() = source.resourceFinder

        override fun toString(): String {
            return source.toString()
        }
    }
}