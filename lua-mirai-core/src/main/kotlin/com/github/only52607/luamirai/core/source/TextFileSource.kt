package com.github.only52607.luamirai.core.source

import com.github.only52607.luamirai.core.ScriptResourceFinder
import com.github.only52607.luamirai.core.ScriptSource
import com.github.only52607.luamirai.core.util.getExtension
import java.io.File
import java.io.InputStream
import java.nio.charset.Charset

class TextFileSource(
    val file: File,
    override val lang: String = file.name.getExtension(),
    override val name: String = "@${file.name}",
    override val charset: Charset = Charsets.UTF_8
) : ScriptSource {
    override val size: Long
        get() = file.length()

    override val resourceFinder: ScriptResourceFinder = object : ScriptResourceFinder {
        val directory = file.parentFile
        override fun findResource(filename: String): InputStream? {
            val file = File(directory, filename)
            if (!file.exists()) return null
            return file.inputStream()
        }
    }

    override val main: InputStream
        get() = file.inputStream()

    override fun toString(): String {
        return "TextFileSource(name=$name, file=$file, lang=$lang)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TextFileSource

        return file == other.file
    }

    override fun hashCode(): Int {
        return file.hashCode()
    }
}
