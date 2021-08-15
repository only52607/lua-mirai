package com.ooooonly.luaMirai.lua.lib

import com.ooooonly.luakt.*
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.TwoArgFunction
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset

class StringExLib : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        (LuaString.s_metatable[INDEX] as LuaTable).edit {
//            "codePointAt" to luaFunctionOf{ s:String,i:Int ->
//                return@luaFunctionOf s.codePointAt(i)
//            }
//            "codePointBefore" to luaFunctionOf{ s:String,i:Int ->
//                return@luaFunctionOf s.codePointBefore(i)
//            }
//            "codePointAt" to luaFunctionOf{ s:String,begin:Int,end:Int ->
//                return@luaFunctionOf s.codePointCount(begin,end)
//            }
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
        return LuaValue.NIL
    }
}