package com.ooooonly.luaMirai.lua.lib.mirai

import com.ooooonly.luakt.*
import com.ooooonly.luakt.mapper.ValueMapperChain
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Contact.Companion.uploadImage
import net.mamoe.mirai.contact.UserOrBot
import net.mamoe.mirai.message.code.MiraiCode
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.MiraiExperimentalApi
import net.mamoe.mirai.utils.MiraiInternalApi
import org.luaj.vm2.*
import org.luaj.vm2.lib.TwoArgFunction
import java.io.File
import java.net.URL

@Suppress("unused")
@MiraiExperimentalApi
object MessageLib : TwoArgFunction() {
    @MiraiInternalApi
    override fun call(modName: LuaValue?, env: LuaValue): LuaValue? {
        val globals = env.checkglobals()
        ValueMapperChain.addKValueMapperBefore { kValue, _ ->
            return@addKValueMapperBefore if (kValue is Message) LuaMiraiMessage(kValue) else null
        }
        globals.injectMessageConstructors()
        val constructors = LuaTable().apply { injectMessageConstructors() }
        globals.edit {
            "Message" to constructors
        }
        globals.setFrom(constructors)
        return LuaValue.NIL
    }

    @MiraiInternalApi
    private fun LuaTable.injectMessageConstructors() = edit {
        listOf("MiraiCode", "Code") nto luaFunctionOf { code: String ->
            return@luaFunctionOf MiraiCode.deserializeMiraiCode(code)
        }

        listOf("PlainText", "Text", "Plain") nto luaFunctionOf { content: String ->
            return@luaFunctionOf PlainText(content)
        }

        "At" to luaFunctionOf { target: UserOrBot ->
            return@luaFunctionOf At(target)
        }

        listOf("QuoteReply", "Quote", "Reply") nto luaFunctionOf { msg: Message ->
            return@luaFunctionOf when (msg) {
                is MessageSource -> QuoteReply(msg)
                is MessageChain -> QuoteReply(msg)
                else -> throw LuaError("Quote reply is required MessageSource or MessageSource.")
            }
        }

        listOf("ImageId", "Image") nto luaFunctionOf { id: String ->
            return@luaFunctionOf Image(id)
        }

        "ImageUrl" to luaFunctionOf { url: String, target: Contact ->
            return@luaFunctionOf runBlocking { target.uploadImage(URL(url).openStream()) }
        }

        "ImageFile" to luaFunctionOf { filePath: String, target: Contact ->
            return@luaFunctionOf runBlocking { target.uploadImage(File(filePath)) }
        }

        listOf("FlashImage", "Flash") nto luaFunctionOf { arg: Image ->
            return@luaFunctionOf FlashImage(arg)
        }

        "AtAll" to luaFunctionOf {
            return@luaFunctionOf AtAll
        }

        "Face" to luaFunctionOf { arg: Int ->
            return@luaFunctionOf Face(arg)
        }


//        set("MarketFace", luaFunctionOf { id: Int,name:String ->
//            return@luaFunctionOf net.mamoe.mirai.message.data.MarketFace(id, name)
//        })


        "VipFace" to luaFunctionOf { arg: LuaValue, count: LuaValue ->
            return@luaFunctionOf arg.asKValue<Int>().toVipFace(count.asKValue())
        }

        listOf("PokeMessage", "Poke") nto luaFunctionOf { arg: LuaValue ->
            return@luaFunctionOf arg.asKValue<Int>().toPokeMessage()
        }

        listOf("ForwardMessage", "Forward") nto luaFunctionOf { table: LuaValue ->
            return@luaFunctionOf table.checktable().buildForwardMsg()
        }

        listOf("LightApp", "App") nto luaFunctionOf { code: String ->
            return@luaFunctionOf LightApp(code)
        }

        "Voice" to luaFunctionOf { fileName: String, md5: ByteArray, fileSize: Long, codec: Int, url: String ->
            return@luaFunctionOf Voice(fileName, md5, fileSize, codec, url)
        }

        listOf("SimpleServiceMessage", "Service") nto luaFunctionOf { serviceId: Int, content: String ->
            return@luaFunctionOf SimpleServiceMessage(serviceId, content)
        }

        listOf(
            "MusicShare",
            "Music"
        ) nto luaFunctionOf { kindId: LuaValue, title: String, summary: String, jumpUrl: String, pictureUrl: String, musicUrl: String ->
            return@luaFunctionOf MusicShare(kindId.toMusicKind(), title, summary, jumpUrl, pictureUrl, musicUrl)
        }
    }

    @MiraiInternalApi
    private fun Int.toVipFace(count: Int) = VipFace(VipFace.values[this], count)

    private fun LuaValue.toMusicKind() =
        if (isstring()) MusicKind.valueOf(checkjstring())
        else MusicKind.values()[checkint()]

    private fun Int.toPokeMessage() = PokeMessage.values[this]

    private fun LuaTable.buildForwardMsg(): ForwardMessage {
        val title by this.provideDelegate(defaultValue = "群聊的聊天记录")
        val brief by this.provideDelegate(defaultValue = "[聊天记录]")
        val source by this.provideDelegate(defaultValue = "聊天记录")
        val previewTable by this.provideDelegate(defaultValue = LuaTable())
        val summary by this.provideDelegate(defaultValue = "群聊的聊天记录")
        val nodeListTable by this.provideDelegate(defaultValue = LuaTable())
        val nodeList = nodeListTable!!.toList().map { it.checktable().buildNode() }
        val preview = previewTable!!.toList().map { it.tojstring() }
        return ForwardMessage(preview, title!!, brief!!, source!!, summary!!, nodeList)
    }

    private fun LuaTable.buildNode() = ForwardMessage.Node(
        get("senderId")?.takeIf { it != LuaValue.NIL }?.tolong() ?: 0,
        get("time")?.takeIf { it != LuaValue.NIL }?.toint() ?: 0,
        get("senderName")?.takeIf { it != LuaValue.NIL }?.tojstring() ?: "",
        get("message")?.takeIf { it != LuaValue.NIL }?.asKValue() ?: EmptyMessageChain
    )
}