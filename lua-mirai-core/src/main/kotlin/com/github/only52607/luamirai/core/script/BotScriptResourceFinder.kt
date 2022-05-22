package com.github.only52607.luamirai.core.script

import java.io.InputStream

/**
 * ClassName: BotScriptResourceFinder
 * Description:
 * date: 2022/5/22 11:45
 * @author ooooonly
 * @version
 */
interface BotScriptResourceFinder {
    fun findResource(filename: String): InputStream?
}