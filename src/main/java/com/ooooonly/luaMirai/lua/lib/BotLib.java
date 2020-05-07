package com.ooooonly.luaMirai.lua.lib;

import com.ooooonly.luaMirai.lua.LuaBot;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

public abstract class BotLib extends TwoArgFunction {
    protected Globals globals;
    @Override
    public LuaValue call(LuaValue modName, LuaValue env) {
        Globals globals = env.checkglobals();
        return LuaValue.NIL;
    }
}
