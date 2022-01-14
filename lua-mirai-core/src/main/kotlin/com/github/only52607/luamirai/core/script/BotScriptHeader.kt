package com.github.only52607.luamirai.core.script

/**
 * ClassName: BotScriptHeader
 * Description:
 * date: 2022/1/13 14:28
 * @author ooooonly
 * @version
 */
interface BotScriptHeader : Map<String, String>

interface MutableBotScriptHeader : MutableMap<String, String>, BotScriptHeader

internal class MutableBotScriptHeaderImpl : MutableBotScriptHeader, MutableMap<String, String> by mutableMapOf()

fun mutableBotScriptHeaderOf(builder: MutableBotScriptHeader.() -> Unit = {}): MutableBotScriptHeader =
    MutableBotScriptHeaderImpl().apply(builder)