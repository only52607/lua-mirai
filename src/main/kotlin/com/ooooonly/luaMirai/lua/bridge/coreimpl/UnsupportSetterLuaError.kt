package com.ooooonly.luaMirai.lua.bridge.coreimpl

import org.luaj.vm2.LuaError

object UnsupportSetterLuaError : LuaError("You could not modify this field!")