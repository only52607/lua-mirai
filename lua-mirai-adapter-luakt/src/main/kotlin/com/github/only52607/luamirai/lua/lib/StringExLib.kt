package com.github.only52607.luamirai.lua.lib

import com.github.only52607.luakt.extension.*
import org.luaj.vm2.LuaString
import org.luaj.vm2.LuaValue
import org.luaj.vm2.Varargs
import org.luaj.vm2.lib.TwoArgFunction
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset

class StringExLib : TwoArgFunction() {
    override fun call(modname: LuaValue?, env: LuaValue?): LuaValue {
        LuaString.s_metatable[INDEX].apply {
            this["substring"] = VarArgFunction { args: Varargs ->
                if (args.narg() > 2) return@VarArgFunction args.arg(1).stringValue
                    .substring(args.arg(2).intValue, args.arg(3).intValue).luaValue
                return@VarArgFunction args.arg(1).stringValue.substring(args.arg(2).intValue).luaValue
            }
            this["encode"] = ThreeArgFunction { s: LuaValue, fromCharset: LuaValue, toCharset: LuaValue ->
                return@ThreeArgFunction String(
                    s.stringValue.toByteArray(Charset.forName(fromCharset.stringValue)),
                    Charset.forName(toCharset.stringValue)
                ).luaValue
            }
            this["length"] = OneArgFunction { s: LuaValue ->
                return@OneArgFunction s.stringValue.length.luaValue
            }
            this["encodeURL"] = TwoArgFunction { s: LuaValue, charset: LuaValue ->
                return@TwoArgFunction URLEncoder.encode(s.stringValue, charset.stringValue).luaValue
            }
            this["decodeURL"] = TwoArgFunction { s: LuaValue, charset: LuaValue ->
                return@TwoArgFunction URLDecoder.decode(s.stringValue, charset.stringValue).luaValue
            }
        }
        return LuaValue.NIL
    }
}