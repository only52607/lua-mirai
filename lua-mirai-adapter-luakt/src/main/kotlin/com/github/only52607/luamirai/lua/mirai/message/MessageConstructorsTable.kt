package com.github.only52607.luamirai.lua.mirai.message

import com.github.only52607.luakt.ValueMapper
import com.github.only52607.luakt.dsl.*
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.AudioSupported
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Contact.Companion.uploadImage
import net.mamoe.mirai.contact.UserOrBot
import net.mamoe.mirai.message.code.MiraiCode
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.ExternalResource.Companion.toExternalResource
import net.mamoe.mirai.utils.MiraiExperimentalApi
import net.mamoe.mirai.utils.MiraiInternalApi
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import java.io.File
import java.net.URL

/**
 * ClassName: MessageConstructorsTable
 * Description:
 * date: 2022/1/10 22:52
 * @author ooooonly
 * @version
 */
@OptIn(MiraiExperimentalApi::class, MiraiInternalApi::class)
class MessageConstructorsTable(
    valueMapper: ValueMapper
) : LuaTable(), ValueMapper by valueMapper {

    private val Message.luaValue: LuaValue
        get() = mapToLuaValue(this@Message)

    init {
        this["MiraiCode"] = oneArgLuaFunctionOf { MiraiCode.deserializeMiraiCode(it.stringValue).luaValue }
        this["Code"] = this["MiraiCode"]
        this["PlainText"] = oneArgLuaFunctionOf { PlainText(it.stringValue).luaValue }
        this["Text"] = this["PlainText"]
        this["Plain"] = this["PlainText"]
        this["At"] = oneArgLuaFunctionOf { At(it.userdataValue as UserOrBot).luaValue }
        this["QuoteReply"] = oneArgLuaFunctionOf {
            when (val msg = it.userdataValue) {
                is MessageSource -> QuoteReply(msg)
                is MessageChain -> QuoteReply(msg)
                else -> throw LuaError("Quote reply is required MessageSource or MessageSource.")
            }.luaValue
        }
        this["Quote"] = this["QuoteReply"]
        this["Reply"] = this["QuoteReply"]
        this["ImageId"] = oneArgLuaFunctionOf { Image(it.stringValue).luaValue }
        this["Image"] = this["ImageId"]
        this["ImageUrl"] = twoArgLuaFunctionOf { url: LuaValue, target: LuaValue ->
            runBlocking { (target.userdataValue as Contact).uploadImage(URL(url.stringValue).openStream()) }.luaValue
        }
        this["ImageFile"] = twoArgLuaFunctionOf { filePath: LuaValue, target: LuaValue ->
            runBlocking { (target.userdataValue as Contact).uploadImage(File(filePath.stringValue)) }.luaValue
        }
        this["FlashImage"] = oneArgLuaFunctionOf { FlashImage(it.userdataValue as Image).luaValue }
        this["Flash"] = this["FlashImage"]
        this["AtAll"] = zeroArgLuaFunctionOf { AtAll.luaValue }
        this["Face"] = oneArgLuaFunctionOf { Face(it.intValue).luaValue }
        this["VipFace"] = twoArgLuaFunctionOf { arg: LuaValue, count: LuaValue ->
            return@twoArgLuaFunctionOf VipFace(VipFace.values[arg.intValue], count.intValue).luaValue
        }
        this["PokeMessage"] = oneArgLuaFunctionOf { PokeMessage.values[it.intValue].luaValue }
        this["Poke"] = this["PokeMessage"]
        this["ForwardMessage"] = oneArgLuaFunctionOf { table: LuaValue ->
            ForwardMessage(
                title = table.get("title").optjstring("群聊的聊天记录"),
                brief = table.get("brief").optjstring("[聊天记录]"),
                source = table.get("source").optjstring("聊天记录"),
                preview = mutableListOf<String>().apply {
                    table.get("preview").opttable(LuaTable()).forEach { _, value ->
                        add(value.optjstring(""))
                    }
                },
                summary = table.get("summary").optjstring("群聊的聊天记录"),
                nodeList = mutableListOf<ForwardMessage.Node>().apply {
                    table.get("content").forEach { _, node ->
                        add(
                            ForwardMessage.Node(
                                node["senderId"].optlong(0),
                                node["time"].optint(0),
                                node["senderName"].optjstring(""),
                                node["message"].userdataValueOrNull as? Message ?: EmptyMessageChain
                            )
                        )
                    }
                },
            ).luaValue
        }
        this["Forward"] = this["ForwardMessage"]
        this["LightApp"] = oneArgLuaFunctionOf { LightApp(it.stringValue).luaValue }
        this["App"] = this["LightApp"]
        this["Voice"] = threeArgLuaFunctionOf { contact: LuaValue, fileName: LuaValue, formatName: LuaValue ->
            runBlocking { (contact.userdataValue as AudioSupported).uploadAudio(File(fileName.stringValue).toExternalResource(formatName.stringValue)) }.luaValue
        }
        this["Audio"] = this["Voice"]
        this["SimpleServiceMessage"] = twoArgLuaFunctionOf { serviceId: LuaValue, content: LuaValue ->
            SimpleServiceMessage(serviceId.intValue, content.stringValue).luaValue
        }
        this["Service"] = this["SimpleServiceMessage"]
        this["MusicShare"] =
            varArgFunctionOf {
                val arg1 = it.arg1()
                val kind = if (arg1.isstring()) MusicKind.valueOf(arg1.checkjstring()) else MusicKind.values()[arg1.checkint()]
                MusicShare(
                    kind,
                    it.arg(2).stringValue,
                    it.arg(3).stringValue,
                    it.arg(4).stringValue,
                    it.arg(5).stringValue,
                    it.arg(6).stringValue,
                ).luaValue
            }
        this["Music"] = this["MusicShare"]
    }
}