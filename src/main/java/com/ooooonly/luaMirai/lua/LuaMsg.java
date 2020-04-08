package com.ooooonly.luaMirai.lua;

import org.luaj.vm2.LuaValue;

public abstract class LuaMsg extends LuaValue {
    @Override
    public int type() {
        return 0;
    }

    @Override
    public String typename() {
        return "LuaMsg";
    }
}
