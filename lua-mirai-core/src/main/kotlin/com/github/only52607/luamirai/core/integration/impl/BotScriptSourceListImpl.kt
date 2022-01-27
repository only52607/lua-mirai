package com.github.only52607.luamirai.core.integration.impl

import com.github.only52607.luamirai.core.integration.BotScriptSourceList
import com.github.only52607.luamirai.core.script.BotScriptSource

/**
 * ClassName: BotScriptSourceListImpl
 * Description:
 * date: 2022/1/27 19:13
 * @author ooooonly
 * @version
 */
class BotScriptSourceListImpl : BotScriptSourceList, MutableList<BotScriptSource> by mutableListOf()