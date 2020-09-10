package com.ooooonly.luaMirai.frontend.web.entities

import java.io.File

data class FileInfo(val key: Int = 0, val name: String = "", val size: Long = 0) {
    companion object {
        fun fromFile(file: File, key: Int = 0) = FileInfo(key, file.name, file.length())
        fun fromFiles(files: Array<File>): List<FileInfo> {
            var i = 0
            return mutableListOf<FileInfo>().apply {
                files.forEach { add(fromFile(it, i++)) }
            }
        }
    }
}