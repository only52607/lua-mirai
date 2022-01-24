package com.github.only52607.luamirai.lua.lib

import com.github.only52607.luakt.ValueMapper
import com.github.only52607.luakt.utils.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.luaj.vm2.*
import org.luaj.vm2.lib.TwoArgFunction
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

open class HttpLib(
    private val valueMapper: ValueMapper
) : TwoArgFunction() {
    companion object {
        fun getRedirectUrl(path: String, referer: String?): String {
            val url = URL(path)
            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection
            conn.instanceFollowRedirects = false
            conn.connectTimeout = 5000
            referer?.takeIf { it.isNotBlank() }.let {
                conn.addRequestProperty("Referer", it)
            }
            return conn.getHeaderField("Location").toByteArray(Charset.forName("ISO-8859-1")).let {
                "${url.protocol}://${url.host}${String(it)}"
            }
        }
    }

    private val defaultClient: OkHttpClient by lazy {
        OkHttpClient()
    }

    override fun call(modname: LuaValue?, env: LuaValue): LuaValue {
        val globals: Globals = env.checkglobals()
        val httpTable = LuaTable()
        valueMapper.provideScope {
            httpTable.edit {
                "get" to varArgFunctionOf { args: Varargs ->
                    defaultClient.newCall(args.toRequest("GET")).execute().toVarargs()
                }
                "post" to varArgFunctionOf { args: Varargs ->
                    defaultClient.newCall(args.toRequest("POST")).execute().toVarargs()
                }
                "delete" to varArgFunctionOf { args: Varargs ->
                    defaultClient.newCall(args.toRequest("DELETE")).execute().toVarargs()
                }
                "put" to varArgFunctionOf { args: Varargs ->
                    defaultClient.newCall(args.toRequest("PUT")).execute().toVarargs()
                }
                "patch" to varArgFunctionOf { args: Varargs ->
                    defaultClient.newCall(args.toRequest("PATCH")).execute().toVarargs()
                }
                "getRedirectUrl" to varArgFunctionOf { args: Varargs ->
                    getRedirectUrl(args.arg1().optjstring(""), args.arg(2).optjstring("")).asLuaValue()
                }
            }
        }
        globals.set("Http", httpTable)
        return LuaValue.NIL
    }

    // HttpClient Builder

    private fun LuaTable.toOkHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
        this@toOkHttpClient.get("connectTimeout").longValueOrNull?.let { connectTimeout ->
            this.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
        }
        this@toOkHttpClient.get("readTimeout").longValueOrNull?.let { readTimeout ->
            this.readTimeout(readTimeout, TimeUnit.MILLISECONDS)
        }
        this@toOkHttpClient.get("followRedirects").booleanValueOrNull?.let { followRedirects ->
            this.followRedirects(followRedirects)
        }
        this@toOkHttpClient.get("followSslRedirects").booleanValueOrNull?.let { followSslRedirects ->
            this.followSslRedirects(followSslRedirects)
        }
        this@toOkHttpClient.get("writeTimeout").longValueOrNull?.let { writeTimeout ->
            this.writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
        }
    }.build()

    // Request Builder

    private fun Varargs.toRequest(method: String, extra: Request.Builder.() -> Unit = {}): Request {
        val builder = Request.Builder()
        val requestUrl = arg1().checkjstring()
        val requestBody =  if (narg() >= 2 && arg(2).istable()) arg(2).checktable().toRequestBody() else null
        val params = if (narg() >= 3 && arg(3).istable()) arg(3).checktable() else null
        builder.url(requestUrl)
        builder.method(method, requestBody)
        params?.get("headers")?.takeIf { it.istable() }?.checktable()?.let {
            builder.headers(it.toHeaders())
        }
        builder.extra()
        return builder.build()
    }

    private fun LuaValue.toRequestBody(): RequestBody = when (this::class) {
        LuaTable::class -> this.get("data").optjstring("")
            .toRequestBody(this.get("type").optjstring("text/plain;charset=utf-8").toMediaTypeOrNull())
        else -> this.optjstring("").toRequestBody("text/plain;charset=utf-8".toMediaTypeOrNull())
    }

    private fun LuaTable.toHeaders(): Headers {
        val builder = Headers.Builder()
        keys().forEach { key ->
            builder.add(key.tojstring(), get(key).tojstring())
        }
        return builder.build()
    }

    // Response Converter

    private fun Headers.toLuaTable(): LuaTable = buildLuaTable(valueMapper) {
        this@toLuaTable.forEach { (key, value) ->
            key to value
        }
    }

    private fun Response.toVarargs(): Varargs = LuaValue.varargsOf(
        arrayOf(
            LuaValue.valueOf(this.body?.bytes()),
            toDetailsLuaTable()
        )
    )

    private fun Response.toDetailsLuaTable(): LuaTable = buildLuaTable(valueMapper) {
        "code" to this@toDetailsLuaTable.code
        "headers" to this@toDetailsLuaTable.headers.toLuaTable()
        "isRedirect" to this@toDetailsLuaTable.isRedirect
        "isSuccessful" to this@toDetailsLuaTable.isSuccessful
        "message" to this@toDetailsLuaTable.message
        "receivedResponseAtMillis" to this@toDetailsLuaTable.receivedResponseAtMillis
        "sentRequestAtMillis" to this@toDetailsLuaTable.sentRequestAtMillis
    }
}