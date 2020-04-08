package com.ooooonly.luaMirai

import com.ooooonly.luaMirai.lua.MiraiGlobals
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaValue
import java.io.BufferedReader
import java.io.InputStreamReader


fun main() {
    var globals: Globals = MiraiGlobals()
    var func: LuaValue = globals.load(
        BufferedReader(
            InputStreamReader(
                Thread.currentThread().getContextClassLoader().getResourceAsStream("lua/test.lua")
            )
        ),
        ""
    )
    func.call()
}
