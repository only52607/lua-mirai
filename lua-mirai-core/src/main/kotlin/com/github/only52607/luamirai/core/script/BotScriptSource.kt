package com.github.only52607.luamirai.core.script

import java.io.File
import java.io.InputStream
import java.net.URL

/**
 * ClassName: BotScriptSource
 * Description:
 * date: 2021/8/24 10:11
 * @author ooooonly
 * @version
 */

interface BotScriptSource {
    val size: Long
    val chunkName: String
}

interface BotScriptFileSource: BotScriptSource {
    val file: File
}

interface BotScriptURLSource: BotScriptSource {
    val url: URL
}

interface BotScriptContentSource: BotScriptSource {
    val content: String
}

interface BotScriptInputStreamSource: BotScriptSource {
    val inputStream: InputStream
}

