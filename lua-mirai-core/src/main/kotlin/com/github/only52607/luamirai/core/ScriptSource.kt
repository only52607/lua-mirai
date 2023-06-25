package com.github.only52607.luamirai.core

import java.io.InputStream
import java.nio.charset.Charset

/**
 * ClassName: BotScriptSource
 * Description:
 * date: 2021/8/24 10:11
 * @author ooooonly
 * @version
 */

interface ScriptSource {
    /**
     * 脚本的编写语言标识
     */
    val lang: String

    /**
     * 脚本的名字
     */
    val name: String

    /**
     * 脚本大小
     */
    val size: Long

    /**
     * 脚本编码
     */
    val charset: Charset

    /**
     * 脚本内容输入流，每次获取都会创建一个新的流
     */
    val main: InputStream

    /**
     * 脚本资源搜索器
     */
    val resourceFinder: ScriptResourceFinder?
}