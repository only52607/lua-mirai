package com.ooooonly.luaMirai.lua

import org.luaj.vm2.Globals
import org.luaj.vm2.LuaValue
import java.io.File
import java.net.URL
import java.nio.charset.Charset

/**
 * ClassName: LuaSource
 * Description:
 * date: 2021/8/15 11:28
 * @author ooooonly
 * @version
 */
sealed class LuaSource {
    abstract val chunkName: String

    abstract fun load(globals: Globals): LuaValue

    class LuaFileSource(val file: File) : LuaSource() {
        constructor(filePath: String) : this(File(filePath))

        override val chunkName: String
            get() = file.name

        override fun load(globals: Globals): LuaValue {
            return globals.loadfile(file.absolutePath)
        }
    }

    class LuaContentSource(val content: String) : LuaSource() {
        override val chunkName: String
            get() = "{content}"

        override fun load(globals: Globals): LuaValue {
            return globals.load(content)
        }
    }

    class LuaURLSource(val url: URL, val charset: Charset = Charsets.UTF_8) : LuaSource() {
        override val chunkName: String
            get() = url.path

        override fun load(globals: Globals): LuaValue {
            return globals.load(url.readText(charset))
        }
    }
}