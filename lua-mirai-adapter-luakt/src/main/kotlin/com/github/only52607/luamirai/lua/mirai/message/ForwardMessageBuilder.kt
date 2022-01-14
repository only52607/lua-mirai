package com.github.only52607.luamirai.lua.mirai.message

import com.ooooonly.luakt.mapper.ValueMapper
import com.ooooonly.luakt.utils.provideScope
import com.ooooonly.luakt.utils.toList
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
    val title by entry(defaultValue = "群聊的聊天记录")
    val brief by entry(defaultValue = "[聊天记录]")
    val source by entry(defaultValue = "聊天记录")
    val previewTable by entry(defaultValue = LuaTable())
    val summary by entry(defaultValue = "群聊的聊天记录")
    val nodeListTable by entry(defaultValue = LuaTable())
    val nodeList = nodeListTable!!.toList().map { it.checktable().buildNode(valueMapper) }
    val preview = previewTable!!.toList().map { it.tojstring() }
    ForwardMessage(preview, title!!, brief!!, source!!, summary!!, nodeList)
}

private fun LuaTable.buildNode(valueMapper: ValueMapper) = valueMapper.provideScope {
    ForwardMessage.Node(
        getOrNull("senderId")?.tolong() ?: 0,
        getOrNull("time")?.toint() ?: 0,
        getOrNull("senderName")?.tojstring() ?: "",
        getOrNull("message")?.asKValue() ?: EmptyMessageChain
    )
}