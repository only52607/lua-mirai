package com.ooooonly.luaMirai.lua;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public abstract class LuaBot extends LuaContact {
    public static final int LOGIN = 0;
    public static final int JOIN = 1;
    public static final int CLOSE_AND_JOIN = 2;
    public static final int GET_FRIEND = 3;
    public static final int GET_GROUP = 4;
    public static final int GET_SELF_QQ = 5;
    public static final int GET_ID = 6;
    public static final int SELF_QQ = 7;
    public static final int CONTAINS_FRIEND = 8;
    public static final int CONTAINS_GROUP = 9;
    public static final int IS_ACTIVE = 10;
    public static final int LAUNCH = 11;
    public static final int GET_FRIENDS = 12;
    public static final int GET_GROUPS = 13;

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
    public static final int EVENT_GROUP_MEMBER_JOIN_REQUEST = 120;
    public static final int EVENT_GROUP_MEMBER_LEAVE = 121;
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
    }

    @Override
    protected LuaTable getMetaTable() {
        if (metaTable != null) return metaTable;
        metaTable = new LuaTable();
        LuaTable index = new LuaTable();
        index.rawset("login", getOpFunction(LOGIN));
        index.rawset("join", getOpFunction(JOIN));
        index.rawset("closeAndJoin", getOpFunction(CLOSE_AND_JOIN));
        index.rawset("getFriend", getOpFunction(GET_FRIEND));
        index.rawset("getGroup", getOpFunction(GET_GROUP));
        index.rawset("getFriends", getOpFunction(GET_FRIENDS));
        index.rawset("getGroups", getOpFunction(GET_GROUPS));
        index.rawset("getSelfQQ", getOpFunction(GET_SELF_QQ));
        index.rawset("getId", getOpFunction(GET_ID));
        //index.rawset("selfQQ", getOpFunction(SELF_QQ));
        index.rawset("containsFriend", getOpFunction(CONTAINS_FRIEND));
        index.rawset("containsGroup", getOpFunction(CONTAINS_GROUP));
        index.rawset("isActive", getOpFunction(IS_ACTIVE));
        index.rawset("launch", getOpFunction(LAUNCH));

        index.rawset("subscribeFriendMsg", getSubscribeFunction(EVENT_MSG_FRIEND));
        index.rawset("subscribeGroupMsg", getSubscribeFunction(EVENT_MSG_GROUP));
        index.rawset("subscribeFriendMsgSend", getSubscribeFunction(EVENT_MSG_SEND_FRIEND));
        index.rawset("subscribeGroupMsgSend", getSubscribeFunction(EVENT_MSG_SEND_GROUP));

        index.rawset("subscribeBotOnlineEvent", getSubscribeFunction(EVENT_BOT_ONLINE));
        index.rawset("subscribeBotOfflineEvent", getSubscribeFunction(EVENT_BOT_OFFLINE));
        index.rawset("subscribeBotReloginEvent", getSubscribeFunction(EVENT_BOT_RE_LOGIN));

        index.rawset("subscribeBotGroupPermissionChangeEvent", getSubscribeFunction(EVENT_BOT_CHANGE_GROUP_PERMISSION));
        index.rawset("subscribeBotMutedEvent", getSubscribeFunction(EVENT_BOT_MUTED));
        index.rawset("subscribeBotJoinGroupEvent", getSubscribeFunction(EVENT_BOT_JOIN_GROUP));
        index.rawset("subscribeBotKickedEvent", getSubscribeFunction(EVENT_BOT_KICKED));
        index.rawset("subscribeBotLeaveEvent", getSubscribeFunction(EVENT_BOT_LEAVE));

        index.rawset("subscribeGroupRequestEvent", getSubscribeFunction(EVENT_GROUP_REQUEST));
        index.rawset("subscribeGroupNameChangedEvent", getSubscribeFunction(EVENT_GROUP_CHANGE_NAME));
        index.rawset("subscribeGroupSettingChangedEvent", getSubscribeFunction(EVENT_GROUP_CHANGE_SETTING));
        index.rawset("subscribeGroupEntranceAnnouncementChangedEvent", getSubscribeFunction(EVENT_GROUP_CHANGE_ENTRANCE_ANNOUNCEMENT));
        index.rawset("subscribeAllowAnonymousChangedEvent", getSubscribeFunction(EVENT_GROUP_CHANGE_ALLOW_ANONYMOUS));
        index.rawset("subscribeAllowConfessTalkChangedEvent", getSubscribeFunction(EVENT_GROUP_CHANGE_ALLOW_CONFESS_TALK));
        index.rawset("subscribeAllowMemberInviteChangedEvent", getSubscribeFunction(EVENT_GROUP_CHANGE_ALLOW_MEMBER_INVITE));

        index.rawset("subscribeMemberJoinEvent", getSubscribeFunction(EVENT_GROUP_MEMBER_JOIN));
        index.rawset("subscribeMemberJoinRequestEvent", getSubscribeFunction(EVENT_GROUP_MEMBER_JOIN_REQUEST));
        index.rawset("subscribeMemberKickEvent", getSubscribeFunction(EVENT_GROUP_MEMBER_LEAVE));//
        index.rawset("subscribeMemberLeaveEvent", getSubscribeFunction(EVENT_GROUP_MEMBER_LEAVE));
        index.rawset("subscribeMemberCardChangedEvent", getSubscribeFunction(EVENT_GROUP_MEMBER_CHANGE_CARD));
        index.rawset("subscribeMemberSpecialTitleChangeEvent", getSubscribeFunction(EVENT_GROUP_MEMBER_CHANGE_SPECIAL_TITLE));
        index.rawset("subscribeMemberPermissionChangedEvent", getSubscribeFunction(EVENT_GROUP_MEMBER_CHANGE_PERMISSION));
        index.rawset("subscribeMemberMutedEvent", getSubscribeFunction(EVENT_GROUP_MEMBER_MUTED));
        index.rawset("subscribeMemberUnmutedEvent", getSubscribeFunction(EVENT_GROUP_MEMBER_UN_MUTED));

        index.rawset("subscribeFriendRemarkChangedEvent", getSubscribeFunction(EVENT_FRIEND_CHANGE_REMARK));
        index.rawset("subscribeFriendAddEvent", getSubscribeFunction(EVENT_FRIEND_ADDED));
        index.rawset("subscribeFriendDeleteEvent", getSubscribeFunction(EVENT_FRIEND_DELETE));
        index.rawset("subscribeFriendRequestEvent", getSubscribeFunction(EVENT_FRIEND_REQUEST));

        metaTable.set(INDEX, index);
        return metaTable;
    }

    @Override
    public int type() {
        return TYPE_LUA_BOT;
    }
}
