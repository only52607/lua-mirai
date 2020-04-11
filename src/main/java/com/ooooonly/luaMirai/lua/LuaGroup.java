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
        index.set("sendMsg", getOpFunction(SEND_MSG));
        index.set("getMember", getOpFunction(GET_MEMBER));
        index.set("getAvatarUrl", getOpFunction(GET_AVATAR_URL));
        index.set("getBotMuteRemain", getOpFunction(GET_BOT_MUTE_REMAIN));
        index.set("getBotAsMember", getOpFunction(GET_BOT_AS_MEMBER));
        index.set("getBotPermission", getOpFunction(GET_BOT_PERMISSION));
        index.set("getName", getOpFunction(GET_NAME));
        index.set("getOwner", getOpFunction(GET_OWNER));
        index.set("contains", getOpFunction(CONTAINS));
        index.set("getMemberOrNull", getOpFunction(GET_MEMBER_OR_NULL));
        index.set("Quit", getOpFunction(QUIT));
        index.set("toFullString", getOpFunction(TO_FULL_STRING));
        metaTable.set("__index", index);
        return metaTable;
    }

    @Override
    public int type() {
        return TYPE_LUA_GROUP;
    }
}
