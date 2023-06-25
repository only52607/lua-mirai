package com.github.only52607.luamirai.core.util

/**
 * ClassName: CommonPath
 * Description:
 * date: 2022/6/29 16:48
 * @author ooooonly
 * @version
 */

internal class CommonPath(
    private val pathString: String
) {
    companion object {
        const val SEPARATOR = '/'
    }

    private fun String.trimLastSeparator(): String {
        return if (this.last() == SEPARATOR) this.substring(0, this.length - 1) else this
    }

    private fun String.trimLastEntry(): String {
        if (isBlank()) throw Exception("Could not found parent for empty path")
        val lastSeparator = lastIndexOf(SEPARATOR)
        if (lastSeparator < 0) return ""
        return substring(0, lastSeparator)
    }

    private fun String.buildCanonicalPath(): String {
        if (startsWith(SEPARATOR)) return substring(1).buildCanonicalPath()
        var curPathString = ""
        split(SEPARATOR).filter(String::isNotBlank).map(String::trim).forEach {
            curPathString = when (it) {
                "." -> curPathString
                ".." -> curPathString.trimLastEntry()
                else -> "$curPathString/$it"
            }
        }
        return curPathString
    }

    val parent: CommonPath by lazy {
        CommonPath(pathString.trimLastSeparator().trimLastEntry())
    }

    val canonicalPath: String by lazy {
        pathString.buildCanonicalPath()
    }

    fun join(joinPathString: String): CommonPath {
        if (joinPathString.startsWith(SEPARATOR)) return CommonPath(joinPathString.substring(1))
        return CommonPath("$pathString/$joinPathString")
    }
}