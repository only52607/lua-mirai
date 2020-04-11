package com.ooooonly.luaMirai.lua;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;


public abstract class LuaBot extends LuaContact {
    public static final int LOGIN = 0;
    public static final int JOIN = 1;
    public static final int CLOSE_AND_JOIN = 2;
    public static final int GET_FRIEND = 3;
    public static final int GET_GROUP = 4;
    public static final int GET_SELF_QQ = 5;
    public static final int GET_ID = 6;
    public static final int ADD_FRIEND = 7;
    public static final int CONTAINS_FRIEND = 8;
    public static final int CONTAINS_GROUP = 9;
    public static final int IS_ACTIVE = 10;

    public static final int EVENT_MSG_FRIEND = 100;
    public static final int EVENT_MSG_GROUP = 101;

    private static LuaTable metaTable;

    public LuaBot(long account, String password) {
        super(account);
        this.set("account", LuaValue.valueOf(account));
        this.set("password", LuaValue.valueOf(password));
    }

    @Override
    protected LuaTable getMetaTable() {
        if (metaTable != null) return metaTable;
        metaTable = new LuaTable();
        LuaTable index = new LuaTable();
        index.set("login", getOpFunction(LOGIN));
        index.set("join", getOpFunction(JOIN));
        index.set("closeAndJoin", getOpFunction(CLOSE_AND_JOIN));
        index.set("getFriend", getOpFunction(GET_FRIEND));
        index.set("getGroup", getOpFunction(GET_GROUP));
        index.set("getSelfQQ", getOpFunction(GET_SELF_QQ));
        index.set("getId", getOpFunction(GET_ID));
        index.set("addFriend", getOpFunction(ADD_FRIEND));
        index.set("containsFriend", getOpFunction(CONTAINS_FRIEND));
        index.set("containsGroup", getOpFunction(CONTAINS_GROUP));
        index.set("isActive", getOpFunction(IS_ACTIVE));

        index.set("subscribeFriendMsg", getSubscribeFunction(EVENT_MSG_FRIEND));
        index.set("subscribeGroupMsg", getSubscribeFunction(EVENT_MSG_GROUP));
        metaTable.set("__index", index);
        return metaTable;
    }

    @Override
    public int type() {
        return TYPE_LUA_GROUP;
    }
}
