package com.ooooonly.luaMirai.lua;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

public abstract class LuaContact extends LuaObject {
    protected long id;
    protected static final int TYPE_LUA_BOT = 100;
    protected static final int TYPE_LUA_QQ = 101;
    protected static final int TYPE_LUA_GROUP = 102;
    protected static final int TYPE_LUA_MEMBER = 103;

    protected LuaContact(long id) {
        super();
        this.rawset("id", id);
    }
}
