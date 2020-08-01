package com.ooooonly.luaMirai.lua.lib

import com.ooooonly.luaMirai.utils.setFunction
import com.ooooonly.luaMirai.utils.toLuaValue
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.luaj.vm2.*
import org.luaj.vm2.lib.TwoArgFunction
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

open class HttpLib() : TwoArgFunction() {
    companion object {
        fun getRedirectUrl(path: String, referer: String?): String? {
            val url = URL(path)
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.setInstanceFollowRedirects(false)
            conn.setConnectTimeout(5000)
            referer?.takeIf { !it.isBlank() }.let {
                conn.addRequestProperty("Referer", it)
            }
            return conn.getHeaderField("Location").toByteArray(Charset.forName("ISO-8859-1")).let {
                "${url.protocol}://${url.host}${String(it)}"
            }
        }
    }

    val defaultClient: OkHttpClient by lazy {
        OkHttpClient()
    }

    override fun call(modname: LuaValue?, env: LuaValue): LuaValue {
        val globals: Globals = env.checkglobals()
        val httpTable: LuaTable = LuaTable()
        httpTable.apply {
            setFunction("get") { args ->
                val requestUrl = args.checkjstring(1)
                val client: OkHttpClient = if (args.narg() >= 2)
                    args.checktable(2).toOkHttpClient()
                else defaultClient
                val request: Request = if (args.narg() >= 3)
                    Request.Builder().get().applyHeaderFromLuaTable(args.checktable(3)).url(requestUrl).build()
                else Request.Builder().get().url(requestUrl).build()

                client.newCall(request).execute().toVarargs()
            }
            setFunction("post") { args ->
                val requestUrl = args.arg1().checkjstring()
                val requestBody = args.arg(2).toRequestBody()
                val client: OkHttpClient = if (args.narg() >= 3)
                    args.checktable(3).toOkHttpClient()
                else defaultClient
                val request: Request = if (args.narg() >= 4)
                    Request.Builder().post(requestBody).applyHeaderFromLuaTable(args.checktable(4)).url(requestUrl)
                        .build()
                else Request.Builder().post(requestBody).url(requestUrl).build()
                client.newCall(request).execute().toVarargs()
            }
            setFunction("getRedirectUrl") {
                getRedirectUrl(it.arg1().optjstring(""), it.arg(2).optjstring(""))?.toLuaValue() ?: "".toLuaValue()
            }
        }
        globals.set("Http", httpTable)
        return LuaValue.NIL
    }


    private fun Response.toLuaTable(): LuaTable = LuaTable().also { table ->
        table.set("body", this.body?.string())
        table.set("code", this.code)
        table.set("message", this.message)
        table.set("isSuccessful", LuaValue.valueOf(this.isSuccessful))
    }

    private fun Response.toVarargs(): Varargs = LuaValue.varargsOf(
        arrayOf(
            LuaValue.valueOf(this.body?.string()),
            LuaValue.valueOf(this.isSuccessful),
            LuaValue.valueOf(this.code),
            LuaValue.valueOf(this.message)
        )
    )

    private fun LuaTable.toOkHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
        this@toOkHttpClient.get("connectTimeout").takeIf { it is LuaInteger }?.let { connectTimeout ->
            this.connectTimeout(connectTimeout.optlong(0), TimeUnit.MILLISECONDS)
        }
        this@toOkHttpClient.get("readTimeout").takeIf { it is LuaInteger }?.let { readTimeout ->
            this.readTimeout(readTimeout.optlong(0), TimeUnit.MILLISECONDS)
        }
        this@toOkHttpClient.get("followRedirects").takeIf { it is LuaBoolean }?.let { followRedirects ->
            this.followRedirects(followRedirects.optboolean(true))
        }
        this@toOkHttpClient.get("followSslRedirects").takeIf { it is LuaBoolean }?.let { followSslRedirects ->
            this.followSslRedirects(followSslRedirects.optboolean(true))
        }
        this@toOkHttpClient.get("writeTimeout").takeIf { it is LuaInteger }?.let { writeTimeout ->
            this.writeTimeout(writeTimeout.optlong(0), TimeUnit.MILLISECONDS)
        }
    }.build()

    private fun LuaTable.toRequest(): Request = Request.Builder().apply {
        this@toRequest.keys().forEach { key ->
            this.header(key.tojstring(), this@toRequest.get(key).tojstring())
        }
    }.build()

    private fun Request.Builder.applyHeaderFromLuaTable(table: LuaTable): Request.Builder {
        table.keys().forEach { key ->
            this.header(key.tojstring(), table.get(key).tojstring())
        }
        return this
    }

    private fun LuaValue.toRequestBody(): RequestBody = when (this::class) {
        LuaTable::class -> RequestBody.create(
            this.get("type").optjstring("text/plain;chaset=utf-8").toMediaTypeOrNull(), this.get("data").optjstring("")
        )
        else -> this.optjstring("").toRequestBody("text/plain;chaset=utf-8".toMediaTypeOrNull())
    }
}