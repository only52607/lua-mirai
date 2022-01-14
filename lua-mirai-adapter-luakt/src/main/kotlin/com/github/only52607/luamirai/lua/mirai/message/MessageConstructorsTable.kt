package com.github.only52607.luamirai.lua.mirai.message

import com.ooooonly.luakt.mapper.ValueMapper
import com.ooooonly.luakt.utils.provideScope
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.Contact.Companion.uploadImage
import net.mamoe.mirai.contact.Group
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
) : LuaTable() {
    init {
        valueMapper.provideScope {
            edit {
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

                "VipFace" to luaFunctionOf { arg: LuaValue, count: LuaValue ->
                    return@luaFunctionOf arg.asKValue<Int>().toVipFace(count.asKValue())
                }

                listOf("PokeMessage", "Poke") nto luaFunctionOf { arg: LuaValue ->
                    return@luaFunctionOf arg.asKValue<Int>().toPokeMessage()
                }

                listOf("ForwardMessage", "Forward") nto luaFunctionOf { table: LuaValue ->
                    return@luaFunctionOf table.checktable().buildForwardMsg(valueMapper)
                }

                listOf("LightApp", "App") nto luaFunctionOf { code: String ->
                    return@luaFunctionOf LightApp(code)
                }

                listOf("Voice", "Audio") nto luaFunctionOf { group: Group, fileName: String, formatName: String ->
                    return@luaFunctionOf runBlocking { group.uploadAudio(File(fileName).toExternalResource(formatName)) }
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
        }
    }

    private fun Int.toVipFace(count: Int) = VipFace(VipFace.values[this], count)

    private fun LuaValue.toMusicKind() =
        if (isstring()) MusicKind.valueOf(checkjstring())
        else MusicKind.values()[checkint()]

    private fun Int.toPokeMessage() = PokeMessage.values[this]
}