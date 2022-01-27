package com.github.only52607.luamirai.core.integration

import com.github.only52607.luamirai.core.integration.impl.BotScriptSourceListImpl
import com.github.only52607.luamirai.core.script.BotScriptSource

/**
 * ClassName: BotScriptSourceManager
 * Description:
 * date: 2022/1/27 18:39
 * @author ooooonly
 * @version
 */
interface BotScriptSourceList : MutableList<BotScriptSource>

fun BotScriptSourceList(): BotScriptSourceList = BotScriptSourceListImpl()