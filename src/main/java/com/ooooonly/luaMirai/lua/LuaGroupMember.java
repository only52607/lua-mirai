package com.ooooonly.luaMirai.lua;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;


abstract public class LuaGroupMember extends LuaContact {

    public static final int GET_NICK = 0;
    public static final int GET_NAME_CARD = 1;
    public static final int GET_MUTE_REMAIN = 2;
    public static final int GET_SPECIAL_TITLE = 3;
    public static final int IS_MUTE = 4;
    public static final int IS_ADMINISTRATOR = 5;
    public static final int IS_OWNER = 6;
    public static final int MUTE = 7;
    public static final int UN_MUTE = 8;
    public static final int KICK = 9;

    private static LuaTable metaTable;

    public LuaGroupMember(LuaBot luaBot, LuaGroup luaGroup, long id) {
        super(id);
        this.set("bot", luaBot);
        this.set("group", luaGroup);
    }

    @Override
    protected LuaTable getMetaTable() {
        if (metaTable != null) return metaTable;
        metaTable = new LuaTable();
        LuaTable index = new LuaTable();
        index.set("getNick", getOpFunction(GET_NICK));
        index.set("getNameCard", getOpFunction(GET_NAME_CARD));
        index.set("getMuteRemain", getOpFunction(GET_MUTE_REMAIN));
        index.set("getSpecialTitle", getOpFunction(GET_SPECIAL_TITLE));
        index.set("isMuted", getOpFunction(IS_MUTE));
        index.set("isAdministrator", getOpFunction(IS_ADMINISTRATOR));
        index.set("isOwner", getOpFunction(IS_OWNER));
        index.set("mute", getOpFunction(MUTE));
        index.set("unMute", getOpFunction(UN_MUTE));
        index.set("kick", getOpFunction(KICK));
        metaTable.set("__index", index);
        return metaTable;
    }

    @Override
    public int type() {
        return TYPE_LUA_MEMBER;
    }
}
