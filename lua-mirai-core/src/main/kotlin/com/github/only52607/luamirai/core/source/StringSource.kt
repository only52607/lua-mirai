package com.github.only52607.luamirai.core.source

import com.github.only52607.luamirai.core.ScriptResourceFinder
import com.github.only52607.luamirai.core.ScriptSource
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.charset.Charset

class StringSource(
    val content: String,
    override val lang: String,
    override val name: String = content,
    override val charset: Charset = Charsets.UTF_8,
    override val resourceFinder: ScriptResourceFinder? = null
) : ScriptSource {
    override val size: Long = content.length.toLong()
    override val main: InputStream
        get() = ByteArrayInputStream(content.toByteArray(charset = charset))

    override fun toString(): String {
        return "StringSource(name=$name, content=${content.hashCode()}, lang=$lang)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StringSource

        return content == other.content
    }

    override fun hashCode(): Int {
        return content.hashCode()
    }
}