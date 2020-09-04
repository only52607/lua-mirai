package com.ooooonly.luaMirai.frontend.web.entities

import java.io.File

data class FileInfo(val fileName:String = "",val fileSize:Long = 0) {
    companion object{
        fun fromFile(file: File) = FileInfo(file.name,file.length())
        fun fromFiles(files:Array<File>):List<FileInfo> = mutableListOf<FileInfo>().apply {
            files.forEach { add(fromFile(it)) }
        }
    }
}