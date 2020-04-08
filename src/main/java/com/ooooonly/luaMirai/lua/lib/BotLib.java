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
        globals.set("Bot", getNewBotFunction());
        return LuaValue.NIL;
    }

    public abstract static class NewBotFunction extends TwoArgFunction {
        @Override
        public LuaValue call(LuaValue qq, LuaValue password) {
            return newBot(qq, password);
        }

        public abstract LuaBot newBot(LuaValue qq, LuaValue password);
    }

    public abstract NewBotFunction getNewBotFunction();
}
