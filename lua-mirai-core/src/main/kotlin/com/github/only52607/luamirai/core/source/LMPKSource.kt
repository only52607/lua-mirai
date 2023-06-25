package com.github.only52607.luamirai.core.source

import com.github.only52607.luamirai.core.ScriptConfiguration
import com.github.only52607.luamirai.core.ScriptResourceFinder
import com.github.only52607.luamirai.core.ScriptSource
import com.github.only52607.luamirai.core.configuation.JsonScriptConfiguration
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream
import java.nio.charset.Charset
import java.util.zip.ZipFile

class LMPKSource private constructor(
    val root: File,
    private val config: ScriptConfiguration,
    override val resourceFinder: ScriptResourceFinder,
) : ScriptSource {

    override val size: Long = 0

    override val lang: String = config.getString("lang")

    override val name = config.getStringOrNull("name") ?: "@${root.name}"

    override val charset = config.getStringOrNull("charset")?.let(Charset::forName) ?: Charsets.UTF_8
    override val main: InputStream
        get() = resourceFinder.findResource(config.getString("lang"))
            ?: throw FileNotFoundException("File ${config.getString("lang")} not found in ${root.absolutePath}")

    class DirectoryResourceFinder(private val directory: File) : ScriptResourceFinder {
        override fun findResource(filename: String): InputStream? {
            val file = File(directory, filename)
            if (!file.exists()) return null
            return file.inputStream()
        }
    }

    class ZipResourceFinder(file: File) : ScriptResourceFinder {
        private val zipFile = ZipFile(file)
        override fun findResource(filename: String): InputStream? {
            val zipEntryName = filename.replace("\\", "/")
            val zipEntry = zipFile.getEntry(zipEntryName) ?: return null
            return zipFile.getInputStream(zipEntry)
        }
    }

    companion object {
        fun fromDirectory(directory: File): LMPKSource {
            val resourceFinder = DirectoryResourceFinder(directory)
            val config = JsonScriptConfiguration.parse(
                resourceFinder.findResource("manifest.json")
                    ?: throw FileNotFoundException("manifest.json not found in ${directory.absolutePath}")
            )
            return LMPKSource(
                root = directory,
                config = config,
                resourceFinder = resourceFinder,
            )
        }

        fun fromZipFile(file: File): LMPKSource {
            val resourceFinder = ZipResourceFinder(file)
            val config = JsonScriptConfiguration.parse(
                resourceFinder.findResource("manifest.json")
                    ?: throw FileNotFoundException("manifest.json not found in ${file.absolutePath}")
            )
            return LMPKSource(
                root = file,
                config = config,
                resourceFinder = resourceFinder,
            )
        }
    }
    
    override fun toString(): String {
        return "LMPKSource(name=$name, root=$root, lang=$lang)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LMPKSource

        return root == other.root
    }

    override fun hashCode(): Int {
        return root.hashCode()
    }
}
