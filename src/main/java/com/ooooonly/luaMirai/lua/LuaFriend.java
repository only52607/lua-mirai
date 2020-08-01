package com.ooooonly.luaMirai.lua;


import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

public abstract class LuaFriend extends LuaContact {
    public static final int GET_NICK = 0;
    public static final int GET_AVATAR_URL = 1;
    public static final int IS_ACTIVE = 2;
    public static final int QUERY_REMARK = 3;
    public static final int QUERY_PROFILE = 4;
    public static final int SEND_MESSAGE = 5;
    public static final int SEND_IMG = 6;

    private static LuaTable metaTable;

    public LuaFriend(LuaBot luaBot, long id) {
        super(id);
        this.rawset("bot", luaBot);
    }

    @Override
    protected LuaTable getMetaTable() {
        if (metaTable != null) return metaTable;
        metaTable = new LuaTable();
        LuaTable index = new LuaTable();
        index.rawset("sendMsg", getOpFunction(SEND_MESSAGE));
        index.rawset("sendImg", getOpFunction(SEND_IMG));
        index.rawset("getNick", getOpFunction(GET_NICK));
        index.rawset("getAvatarUrl", getOpFunction(GET_AVATAR_URL));
        index.rawset("isActive", getOpFunction(IS_ACTIVE));
        //index.rawset("queryRemark", getOpFunction(QUERY_REMARK));
        //index.rawset("queryProfile", getOpFunction(QUERY_PROFILE));
        metaTable.set(INDEX, index);
        return metaTable;
    }

    @Override
    public int type() {
        return TYPE_LUA_FRIEND;
    }

}
