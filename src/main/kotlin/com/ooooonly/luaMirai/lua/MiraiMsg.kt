package com.ooooonly.luaMirai.lua

class MiraiMsg : LuaMsg() {
    override fun getOpFunction(opcode: Int): OpFunction? {
        return null
    }

    override fun getPlain(): String {
        return ""
    }
}