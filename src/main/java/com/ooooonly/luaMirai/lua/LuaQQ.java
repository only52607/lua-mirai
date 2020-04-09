package com.ooooonly.luaMirai.lua;


import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

public abstract class LuaQQ extends LuaTable {
    public static final int GET_NICK = 0;
    public static final int GET_AVATAR_URL = 1;
    public static final int IS_ACTIVE = 2;
    public static final int QUERY_REMARK = 3;
    public static final int QUERY_PROFILE = 4;

    public LuaQQ(LuaBot luaBot, long id) {
        this.set("id", LuaValue.valueOf(id));
        this.set("bot", luaBot);
        this.set("sendMsg", getSendFriendMsgFunction());
        this.set("getNick", getQQOpFunction(GET_NICK));
        this.set("getAvatarUrl", getQQOpFunction(GET_AVATAR_URL));
        this.set("isActive", getQQOpFunction(IS_ACTIVE));
        this.set("queryRemark", getQQOpFunction(QUERY_REMARK));
        this.set("queryProfile", getQQOpFunction(QUERY_PROFILE));
    }

    public static abstract class SendFriendMsgFunction extends TwoArgFunction {
        @Override
        public LuaValue call(LuaValue friend, LuaValue msg) {
            if (friend instanceof LuaQQ) {
                if (msg instanceof LuaString) return sendMsg((LuaQQ) friend, (LuaString) msg);
                if (msg instanceof LuaMsg) return sendMsg((LuaQQ) friend, (LuaMsg) msg);
            }
            return null;
        }

        public abstract LuaValue sendMsg(LuaQQ friend, LuaString msg);

        public abstract LuaValue sendMsg(LuaQQ friend, LuaMsg luaMsg);
    }

    public static abstract class QQOpFunction extends VarArgFunction {
        protected int type;

        public QQOpFunction(int type) {
            this.type = type;
        }

        @Override
        public Varargs onInvoke(Varargs varargs) {
            if (varargs.arg1() instanceof LuaQQ) {
                return op(varargs);
            }
            return LuaValue.NIL;
        }

        public abstract Varargs op(Varargs varargs);
    }

    public abstract SendFriendMsgFunction getSendFriendMsgFunction();

    public abstract QQOpFunction getQQOpFunction(int type);

    @Override
    public int type() {
        return 0;
    }

    @Override
    public String typename() {
        return "LuaQQ";
    }
}
