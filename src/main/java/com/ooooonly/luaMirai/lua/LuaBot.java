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
    public static final int EVENT_MSG_SEND_FRIEND = 102;
    public static final int EVENT_MSG_SEND_GROUP = 103;
    public static final int EVENT_BOT_ONLINE = 104;
    public static final int EVENT_BOT_OFFLINE = 105;
    public static final int EVENT_BOT_RE_LOGIN = 106;
    public static final int EVENT_BOT_CHANGE_GROUP_PERMISSION = 107;
    public static final int EVENT_BOT_MUTED = 108;
    public static final int EVENT_BOT_JOIN_GROUP = 109;
    public static final int EVENT_BOT_KICKED = 110;
    public static final int EVENT_BOT_LEAVE = 111;
    public static final int EVENT_GROUP_CHANGE_NAME = 112;
    public static final int EVENT_GROUP_CHANGE_SETTING = 113;
    public static final int EVENT_GROUP_CHANGE_ENTRANCE_ANNOUNCEMENT = 114;
    public static final int EVENT_GROUP_CHANGE_ALLOW_ANONYMOUS = 115;
    public static final int EVENT_GROUP_CHANGE_ALLOW_CONFESS_TALK = 116;
    public static final int EVENT_GROUP_CHANGE_ALLOW_MEMBER_INVITE = 117;
    public static final int EVENT_GROUP_REQUEST = 118;
    public static final int EVENT_GROUP_MEMBER_JOIN = 119;
    public static final int EVENT_GROUP_MEMBER_INVITED = 120;
    public static final int EVENT_GROUP_MEMBER_KICKED = 121;
    public static final int EVENT_GROUP_MEMBER_CHANGE_CARD = 122;
    public static final int EVENT_GROUP_MEMBER_CHANGE_SPECIAL_TITLE = 123;
    public static final int EVENT_GROUP_MEMBER_CHANGE_PERMISSION = 124;
    public static final int EVENT_GROUP_MEMBER_MUTED = 125;
    public static final int EVENT_GROUP_MEMBER_UN_MUTED = 126;
    public static final int EVENT_FRIEND_CHANGE_REMARK = 127;
    public static final int EVENT_FRIEND_ADDED = 128;
    public static final int EVENT_FRIEND_DELETE = 129;
    public static final int EVENT_FRIEND_REQUEST = 130;

    private static LuaTable metaTable;

    public LuaBot(long account, String password) {
        super(account);
        this.set("account", LuaValue.valueOf(account));
        this.set("password", LuaValue.valueOf(password));
        /*
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();*/
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
