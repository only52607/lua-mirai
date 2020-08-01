package com.ooooonly.luaMirai.lua;

import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

public abstract class LuaGroup extends LuaContact {
    public static final int GET_MEMBER = 0;
    public static final int GET_AVATAR_URL = 1;
    public static final int GET_BOT_AS_MEMBER = 2;
    public static final int GET_BOT_MUTE_REMAIN = 3;
    public static final int GET_BOT_PERMISSION = 4;
    public static final int GET_NAME = 5;
    public static final int GET_OWNER = 6;
    public static final int CONTAINS = 7;
    public static final int GET_MEMBER_OR_NULL = 8;
    public static final int QUIT = 10;
    public static final int TO_FULL_STRING = 11;
    public static final int SEND_MSG = 12;
    public static final int SEND_IMG = 13;

    private static LuaTable metaTable;

    public LuaGroup(LuaBot luaBot, long id) {
        super(id);
        this.rawset("bot", luaBot);
    }

    @Override
    protected LuaTable getMetaTable() {
        if (metaTable != null) return metaTable;
        metaTable = new LuaTable();
        LuaTable index = new LuaTable();
        index.rawset("sendMsg", getOpFunction(SEND_MSG));
        index.rawset("sendImg", getOpFunction(SEND_IMG));
        index.rawset("getMember", getOpFunction(GET_MEMBER));
        index.rawset("getAvatarUrl", getOpFunction(GET_AVATAR_URL));
        index.rawset("getBotMuteRemain", getOpFunction(GET_BOT_MUTE_REMAIN));
        index.rawset("getBotAsMember", getOpFunction(GET_BOT_AS_MEMBER));
        index.rawset("getBotPermission", getOpFunction(GET_BOT_PERMISSION));
        index.rawset("getName", getOpFunction(GET_NAME));
        index.rawset("getOwner", getOpFunction(GET_OWNER));
        index.rawset("contains", getOpFunction(CONTAINS));
        index.rawset("getMemberOrNull", getOpFunction(GET_MEMBER_OR_NULL));
        index.rawset("quit", getOpFunction(QUIT));
        //index.set("toFullString", getOpFunction(TO_FULL_STRING));
        metaTable.rawset(INDEX, index);
        return metaTable;
    }

    @Override
    public int type() {
        return TYPE_LUA_GROUP;
    }
}
