package com.ooooonly.luaMirai.utils

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.contact.Member
import net.mamoe.mirai.message.data.*
import org.luaj.vm2.LuaValue

object MiraiCodeParser {
    fun toMessageChain(raw: java.lang.StringBuilder, contact: Contact?): MessageChain =
        toMessageChain(raw.toString(), contact)

    fun toMessageChain(raw: String, contact: Contact?): MessageChain {
        return buildMessageChain {
            if (raw.contains("[mirai:")) {
                var interpreting = false
                val sb = StringBuilder()
                var index = 0
                raw.forEach { c: Char ->
                    if (c == '[') {
                        if (interpreting) {
                            return PlainText(raw).asMessageChain()
                        } else {
                            interpreting = true
                            if (sb.isNotEmpty()) {
                                val lastMsg = sb.toString()
                                sb.delete(0, sb.length)
                                +lastMsg.toMessageInternal(contact)
                            }
                            sb.append(c)
                        }
                    } else if (c == ']') {
                        if (!interpreting) {
                            return PlainText(raw).asMessageChain()
                        } else {
                            interpreting = false
                            sb.append(c)
                            if (sb.isNotEmpty()) {
                                val lastMsg = sb.toString()
                                sb.delete(0, sb.length)
                                +lastMsg.toMessageInternal(contact)
                            }
                        }
                    } else {
                        sb.append(c)
                    }
                    index++
                }
                if (sb.isNotEmpty()) {
                    +sb.toString().toMessageInternal(contact)
                }
            } else +PlainText(raw)

        }
    }

    private fun String.toMessageInternal(contact: Contact?): Message {
        if (startsWith("[mirai:") && endsWith("]")) {
            val parts = substring(7, length - 1).split(":", limit = 2)
            if (parts.size != 2) return PlainText(this)
            val method = parts[0]
            val arg = parts[1]
            return when (method) {
                "at" -> {
                    if (contact is Group) At((contact as Group).get(arg.toLong()))
                    else if (contact is Member) At(contact as Member)
                    else PlainText(this)
                }
                "atall" -> AtAll
                "face" -> Face(arg.toInt())
                "image" -> Image(arg)
                "xml" -> ServiceMessage(1, arg)
                "app" -> LightApp(arg)
                "json" -> ServiceMessage(1, arg)
                //"forward" -> ForwardMessage(arg)
                else -> PlainText(this)
            }
        }
        return PlainText(this)
    }
}