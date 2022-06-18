package com.github.only52607.luamirai.core.util

/**
 * ClassName: FileUtils
 * Description:
 * date: 2022/6/18 23:09
 * @author ooooonly
 * @version
 */
fun String.getExtension(): String {
    val i = lastIndexOf('.')
    if (i < 0) throw Exception("Could not parse extension for $this")
    return substring(i + 1)
}