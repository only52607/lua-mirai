package com.github.only52607.luamirai.lua.mirai.message

import com.github.only52607.luakt.ValueMapper
import com.github.only52607.luakt.utils.forEach
import com.github.only52607.luakt.utils.provideScope
import net.mamoe.mirai.message.data.EmptyMessageChain
import net.mamoe.mirai.message.data.ForwardMessage
import org.luaj.vm2.LuaTable

/**
 * ClassName: ForwardMessageBuilder
 * Description:
 * date: 2022/1/10 22:57
 * @author ooooonly
 * @version
 */

fun LuaTable.buildForwardMsg(valueMapper: ValueMapper): ForwardMessage = valueMapper.provideScope {
    ForwardMessage(
        title = get("title").optjstring("群聊的聊天记录"),
        brief = get("brief").optjstring("[聊天记录]"),
        source = get("source").optjstring("聊天记录"),
        preview = mutableListOf<String>().apply {
            get("preview").opttable(LuaTable()).forEach { _, value ->
                add(value.optjstring(""))
            }
        },
        summary = get("summary").optjstring("群聊的聊天记录"),
        nodeList = mutableListOf<ForwardMessage.Node>().apply {
            get("content").opttable(LuaTable()).forEach { _, value ->
                add(value.checktable().buildNode(valueMapper))
            }
        },
    )
}

private fun LuaTable.buildNode(valueMapper: ValueMapper): ForwardMessage.Node = valueMapper.provideScope {
    ForwardMessage.Node(
        getOrNull("senderId")?.tolong() ?: 0,
        getOrNull("time")?.toint() ?: 0,
        getOrNull("senderName")?.tojstring() ?: "",
        getOrNull("message")?.asKValue() ?: EmptyMessageChain
    )
}