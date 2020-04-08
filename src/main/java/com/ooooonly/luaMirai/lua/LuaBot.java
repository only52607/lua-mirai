package com.ooooonly.luaMirai.lua;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;

/**
 * Bot's metatable
 */
public abstract class LuaBot extends LuaTable {

    public LuaBot(long account, String password) {
        this.set("account", LuaValue.valueOf(account));
        this.set("password", LuaValue.valueOf(password));
        this.set("login", getLoginFunction());
        this.set("getFriend", getGetFriendFunction());
        this.set("getGroup", getGetGroupFunction());
        this.set("join", getJoinFunction());
        this.set("subscribeFriendMsg", getSubscribeFriendMsgFunction());
        this.set("subscribeGroupMsg", getSubscribeGroupMsgFunction());
        this.set("close", getCloseFunction());
    }

    public static abstract class LoginFunction extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue bot) {
            if (bot instanceof LuaBot) return login((LuaBot) bot);
            return null;
        }

        public abstract LuaValue login(LuaBot luaBot);
    }

    public static abstract class JoinFunction extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue bot) {
            if (bot instanceof LuaBot) return join((LuaBot) bot);
            return null;
        }

        public abstract LuaValue join(LuaBot luaBot);
    }

    public static abstract class CloseFunction extends OneArgFunction {
        @Override
        public LuaValue call(LuaValue bot) {
            if (bot instanceof LuaBot) return close((LuaBot) bot);
            return null;
        }

        public abstract LuaValue close(LuaBot luaBot);
    }

    public static abstract class GetFriendFunction extends TwoArgFunction {
        @Override
        public LuaValue call(LuaValue bot, LuaValue id) {
            if (bot instanceof LuaBot) return getFriend((LuaBot) bot, id.checklong());
            return null;
        }

        public abstract LuaQQ getFriend(LuaBot luaBot, long id);
    }

    public static abstract class SubscribeMsgFunction extends TwoArgFunction {
        @Override
        public LuaValue call(LuaValue bot, LuaValue id) {
            if (bot instanceof LuaBot) return subscribeFriendMsg((LuaBot) bot, id.checkfunction());
            return null;
        }

        public abstract LuaValue subscribeFriendMsg(LuaBot luaBot, LuaFunction listener);
    }

    public static abstract class GetGroupFunction extends TwoArgFunction {
        @Override
        public LuaValue call(LuaValue bot, LuaValue id) {
            if (bot instanceof LuaBot) return getGroup((LuaBot) bot, id.checklong());
            return null;
        }

        public abstract LuaGroup getGroup(LuaBot luaBot, long id);
    }


    public abstract LoginFunction getLoginFunction();

    public abstract GetFriendFunction getGetFriendFunction();

    public abstract GetGroupFunction getGetGroupFunction();

    public abstract JoinFunction getJoinFunction();

    public abstract SubscribeMsgFunction getSubscribeFriendMsgFunction();

    public abstract SubscribeMsgFunction getSubscribeGroupMsgFunction();

    public abstract CloseFunction getCloseFunction();

    @Override
    public int type() {
        return 0;
    }

    @Override
    public String typename() {
        return "LuaBot";
    }
}
