package com.ooooonly.luaMirai.lua.bridge.coreimpl

import com.ooooonly.luaMirai.lua.bridge.base.BaseMsg
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.mamoe.mirai.message.data.*
import okhttp3.OkHttpClient
import okhttp3.Request
import org.luaj.vm2.LuaError
import java.io.FileOutputStream

class MsgCoreImpl(val host: Message) : BaseMsg() {
    init {
    }

    override var type: String
        get() = host::class.simpleName ?: ""
        set(value) {}

    override fun recall() {
        if (host !is MessageChain) throw LuaError("You could not recall single message!")
        host.bot.launch {
            host.recall()
        }
    }

    override fun downloadImage(path: String) {
        runBlocking {
            val imageUrl = (host as Image).queryUrl()
            val client = OkHttpClient()
            val request = Request.Builder().url(imageUrl).build()
            val data = client.newCall(request).execute().body?.byteStream()?.readAllBytes()
            FileOutputStream(path).apply { write(data) }.close()
        }
    }

    override fun getImageUrl(): String {
        TODO("Not yet implemented")
    }

    override fun toTable(): Array<MsgCoreImpl> {
        if (host !is MessageChain) throw LuaError("You could not transform a single message to table!")
        return host.map { MsgCoreImpl(it) }.toTypedArray()
    }


}