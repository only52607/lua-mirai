package com.ooooonly.luaMirai.lua;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

public abstract class LuaContact extends LuaObject {
    protected long id;
    protected static final int TYPE_LUA_BOT = 1000;
    protected static final int TYPE_LUA_FRIEND = 1001;
    protected static final int TYPE_LUA_GROUP = 1002;
    protected static final int TYPE_LUA_MEMBER = 1003;

    protected LuaContact(long id) {
        super();
        this.rawset("id", id);
    }
}
