package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.lua.lib.LuaJavaExLib
import com.ooooonly.luaMirai.lua.lib.MiraiBotLib
import com.ooooonly.luaMirai.lua.lib.NetLib
import org.luaj.vm2.Globals
import org.luaj.vm2.LoadState
import org.luaj.vm2.LuaValue
import org.luaj.vm2.compiler.LuaC
import org.luaj.vm2.lib.*
import org.luaj.vm2.lib.jse.*

class MiraiGlobals : Globals {
    constructor() {
        this.load(JseBaseLib())
        this.load(PackageLib())
        this.load(Bit32Lib())
        this.load(TableLib())
        this.load(StringLib())
        this.load(CoroutineLib())
        this.load(JseMathLib())
        this.load(JseIoLib())
        this.load(JseOsLib())
        this.load(LuajavaLib())
        this.load(MiraiBotLib())
        this.load(NetLib())
        this.load(LuaJavaExLib())
        LoadState.install(this)
        LuaC.install(this)
    }
/*
    override fun get(key: LuaValue?): LuaValue {
        //println(key.toString())
        return super.get(key)
    }*/
}