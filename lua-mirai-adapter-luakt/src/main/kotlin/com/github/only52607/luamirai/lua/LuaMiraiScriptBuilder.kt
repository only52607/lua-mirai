package com.github.only52607.luamirai.lua

import com.github.only52607.luamirai.core.BotScriptBuilder
import com.github.only52607.luamirai.core.script.BotScript
import com.github.only52607.luamirai.core.script.BotScriptHeader
import com.github.only52607.luamirai.core.script.BotScriptSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream

/**
 * ClassName: LuaMiraiScriptBuilder
 * Description:
 * date: 2022/1/13 21:37
 * @author ooooonly
 * @version
 */
@Suppress("unused")
class LuaMiraiScriptBuilder(
    val source: BotScriptSource
) : BotScriptBuilder(source) {
    companion object {
        const val LANG = "lua"
        const val MAX_SCRIPT_HEADER = 8192

        init {
            register(LANG, ::LuaMiraiScriptBuilder)
        }
    }

    override val lang: String
        get() = LANG

    private lateinit var mainInputStream: BufferedInputStream
    private lateinit var header: BotScriptHeader
    private var mainInputStreamInitialized: Boolean = false
    private var headerInitialized: Boolean = false

    private fun prepareMainInputStream() {
        if (mainInputStreamInitialized) return
        mainInputStream = BufferedInputStream(botScriptSource.mainInputStream)
        mainInputStreamInitialized = true
    }

    private suspend fun prepareHeader() {
        if (headerInitialized) return
        prepareMainInputStream()
        mainInputStream.mark(MAX_SCRIPT_HEADER)
        withContext(Dispatchers.IO) {
            try {
                header = LuaHeaderReader.readHeader(mainInputStream)
            } finally {
                mainInputStream.reset()
            }
        }
        headerInitialized = true
    }

    override suspend fun buildInstance(): BotScript {
        prepareMainInputStream()
        prepareHeader()
        mainInputStream.mark(MAX_SCRIPT_HEADER)
        return withContext(Dispatchers.IO) {
            try {
                LuaMiraiScript(source, header, mainInputStream)
            } finally {
                mainInputStream.reset()
            }
        }
    }

    override suspend fun readHeader(): BotScriptHeader {
        prepareHeader()
        return header
    }

    override suspend fun update() {
        mainInputStreamInitialized = false
        headerInitialized = false
    }
}