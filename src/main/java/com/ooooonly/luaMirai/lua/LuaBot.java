package com.ooooonly.luaMirai.lua;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

/**
 * Bot's metatable
 */
public abstract class LuaBot extends LuaTable {
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

    public LuaBot(long account, String password) {
        this.set("account", LuaValue.valueOf(account));
        this.set("password", LuaValue.valueOf(password));
        this.set("login", getBotOpFunction(LOGIN));
        this.set("join", getBotOpFunction(JOIN));
        this.set("closeAndJoin", getBotOpFunction(CLOSE_AND_JOIN));
        this.set("getFriend", getBotOpFunction(GET_FRIEND));
        this.set("getGroup", getBotOpFunction(GET_GROUP));
        this.set("getSelfQQ", getBotOpFunction(GET_SELF_QQ));
        this.set("getId", getBotOpFunction(GET_ID));
        this.set("addFriend", getBotOpFunction(ADD_FRIEND));
        this.set("containsFriend", getBotOpFunction(CONTAINS_FRIEND));
        this.set("containsGroup", getBotOpFunction(CONTAINS_GROUP));
        this.set("isActive", getBotOpFunction(IS_ACTIVE));

        this.set("subscribeFriendMsg", getSubscribeFriendMsgFunction());
        this.set("subscribeGroupMsg", getSubscribeGroupMsgFunction());

    }

    public static abstract class BotOpFunction extends VarArgFunction {
        protected int type;

        public BotOpFunction(int type) {
            this.type = type;
        }

        @Override
        public Varargs onInvoke(Varargs varargs) {
            if (varargs.arg1() instanceof LuaBot) {
                return op(varargs);
            }
            return LuaValue.NIL;
        }

        public abstract Varargs op(Varargs varargs);
    }

    public static abstract class SubscribeMsgFunction extends TwoArgFunction {
        @Override
        public LuaValue call(LuaValue bot, LuaValue id) {
            if (bot instanceof LuaBot) return subscribeFriendMsg((LuaBot) bot, id.checkfunction());
            return null;
        }

        public abstract LuaValue subscribeFriendMsg(LuaBot luaBot, LuaFunction listener);
    }

    public abstract BotOpFunction getBotOpFunction(int type);

    public abstract SubscribeMsgFunction getSubscribeFriendMsgFunction();

    public abstract SubscribeMsgFunction getSubscribeGroupMsgFunction();

    @Override
    public int type() {
        return 0;
    }

    @Override
    public String typename() {
        return "LuaBot";
    }
}
