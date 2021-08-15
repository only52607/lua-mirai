package com.ooooonly.luaMirai.base

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
    abstract fun load(globals: Globals): LuaValue

    class LuaFileSource(val file: File) : LuaSource() {
        override fun load(globals: Globals): LuaValue {
            return globals.loadfile(file.absolutePath)
        }
    }

    class LuaContentSource(val content: String) : LuaSource() {
        override fun load(globals: Globals): LuaValue {
            return globals.load(content)
        }
    }

    class LuaURLSource(val url: URL, val charset: Charset = Charsets.UTF_8) : LuaSource() {
        override fun load(globals: Globals): LuaValue {
            return globals.load(url.readText(charset))
        }
    }
}