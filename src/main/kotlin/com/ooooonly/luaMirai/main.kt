package com.ooooonly.luaMirai

import com.ooooonly.luaMirai.lua.MiraiGlobals
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaValue


fun main(args: Array<String>) {
    var globals: Globals = MiraiGlobals()
    var func: LuaValue = globals.loadfile(args[0])
    func.call()
}
