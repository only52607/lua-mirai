package com.github.only52607.luamirai.lua.lib

import com.ooooonly.luakt.*
import com.ooooonly.luakt.mapper.ValueMapper
import com.ooooonly.luakt.utils.*
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.TwoArgFunction
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset

class StringExLib(
    private val valueMapper: ValueMapper
) : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        val stringTable = LuaString.s_metatable[INDEX] as LuaTable
        valueMapper.provideScope {
            stringTable.edit {
                "substring" to varArgFunctionOf { args: Varargs ->
                    if (args.narg() > 2) return@varArgFunctionOf args[0].asKValue<String>()
                        .substring(args[1].asKValue(), args[2].asKValue()).asLuaValue()
                    return@varArgFunctionOf args[0].asKValue<String>().substring(args[1].asKValue<Int>()).asLuaValue()
                }
                "encode" to luaFunctionOf { s: String, fromCharset: String, toCharset: String ->
                    return@luaFunctionOf String(s.toByteArray(Charset.forName(fromCharset)), Charset.forName(toCharset))
                }
                "length" to luaFunctionOf { s: String ->
                    return@luaFunctionOf s.length
                }
                "encodeURL" to luaFunctionOf { s: String, charset: String ->
                    return@luaFunctionOf URLEncoder.encode(s, charset)
                }
                "decodeURL" to luaFunctionOf { s: String, charset: String ->
                    return@luaFunctionOf URLDecoder.decode(s, charset)
                }
            }
        }
        return LuaValue.NIL
    }
}