package com.ooooonly.luaMirai.base

import java.io.File
import java.net.URL

/**
 * ClassName: BotScriptSource
 * Description:
 * date: 2021/8/24 10:11
 * @author ooooonly
 * @version
 */

interface BotScriptSource

interface BotScriptFileSource: BotScriptSource {
    val file: File
}

interface BotScriptURLSource: BotScriptSource {
    val url: URL
}

interface BotScriptContentSource: BotScriptSource {
    val content: String
}