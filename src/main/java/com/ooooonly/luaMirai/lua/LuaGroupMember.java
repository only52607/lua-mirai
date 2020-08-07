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
    public static final int IS_FRIEND = 10;
    public static final int AS_FRIEND = 11;
    public static final int GET_PERMISSION = 12;
    public static final int SEND_MEG = 13;
    public static final int SEND_IMG = 14;

    private static LuaTable metaTable;

    public LuaGroupMember(LuaBot luaBot, LuaGroup luaGroup, long id) {
        super(id);
        this.rawset("bot", luaBot);
        this.rawset("group", luaGroup);
    }

    @Override
    protected LuaTable getMetaTable() {
        if (metaTable != null) return metaTable;
        metaTable = new LuaTable();
        LuaTable index = new LuaTable();
        index.rawset("getNick", getOpFunction(GET_NICK));
        index.rawset("getNameCard", getOpFunction(GET_NAME_CARD));
        index.rawset("getMuteRemain", getOpFunction(GET_MUTE_REMAIN));
        index.rawset("getSpecialTitle", getOpFunction(GET_SPECIAL_TITLE));
        index.rawset("isMuted", getOpFunction(IS_MUTE));
        index.rawset("isAdministrator", getOpFunction(IS_ADMINISTRATOR));
        index.rawset("isOwner", getOpFunction(IS_OWNER));
        index.rawset("mute", getOpFunction(MUTE));
        index.rawset("unMute", getOpFunction(UN_MUTE));
        index.rawset("kick", getOpFunction(KICK));
        index.rawset("isFriend", getOpFunction(IS_FRIEND));
        index.rawset("asFriend", getOpFunction(AS_FRIEND));
        index.rawset("getPermission", getOpFunction(GET_PERMISSION));
        index.rawset("sendMsg", getOpFunction(SEND_MEG));
        index.rawset("sendImg", getOpFunction(SEND_IMG));
        metaTable.rawset(INDEX, index);
        return metaTable;
    }

    @Override
    public int type() {
        return TYPE_LUA_MEMBER;
    }
}
