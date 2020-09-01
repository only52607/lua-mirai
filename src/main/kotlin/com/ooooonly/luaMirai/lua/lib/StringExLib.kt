package com.ooooonly.luaMirai.lua.lib

import com.ooooonly.luakt.asKValue
import com.ooooonly.luakt.edit
import com.ooooonly.luakt.get
import com.ooooonly.luakt.luaFunctionOf
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
            "substring" to luaFunctionOf { args: Varargs ->
                if (args.narg() > 2) return@luaFunctionOf args[0].asKValue<String>()
                    .substring(args[1].asKValue(), args[2].asKValue())
                return@luaFunctionOf args[0].asKValue<String>().substring(args[1].asKValue<Int>())
            }
            "encode" to luaFunctionOf { s: String, fromCharset: String, toCharset: String ->
                return@luaFunctionOf String(s.toByteArray(Charset.forName(fromCharset)), Charset.forName(toCharset))
            }
            "length" to luaFunctionOf { s: String ->
                return@luaFunctionOf s.length
            }
            "encodeURL" to luaFunctionOf { s: String, charset: String ->
                return@luaFunctionOf URLEncoder.encode(s, Charset.forName(charset))
            }
            "decodeURL" to luaFunctionOf { s: String, charset: String ->
                return@luaFunctionOf URLDecoder.decode(s, Charset.forName(charset))
            }
        }
        return LuaValue.NIL
    }
}