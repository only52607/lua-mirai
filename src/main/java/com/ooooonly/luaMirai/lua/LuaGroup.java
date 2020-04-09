package com.ooooonly.luaMirai.lua;

import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

public abstract class LuaGroup extends LuaTable {
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

    public LuaGroup(LuaBot luaBot, long id) {
        this.set("id", LuaValue.valueOf(id));
        this.set("bot", luaBot);
        this.set("sendMsg", getSendGroupMsgFunction());
        this.set("getMember", getGroupOpFunction(GET_MEMBER));
        this.set("getAvatarUrl", getGroupOpFunction(GET_AVATAR_URL));
        this.set("getBotMuteRemain", getGroupOpFunction(GET_BOT_MUTE_REMAIN));
        this.set("getBotAsMember", getGroupOpFunction(GET_BOT_AS_MEMBER));
        this.set("getBotPermission", getGroupOpFunction(GET_BOT_PERMISSION));
        this.set("getName", getGroupOpFunction(GET_NAME));
        this.set("getOwner", getGroupOpFunction(GET_OWNER));
        this.set("contains", getGroupOpFunction(CONTAINS));
        this.set("getMemberOrNull", getGroupOpFunction(GET_MEMBER_OR_NULL));
        this.set("Quit", getGroupOpFunction(QUIT));
        this.set("toFullString", getGroupOpFunction(TO_FULL_STRING));
    }

    public static abstract class SendGroupMsgFunction extends TwoArgFunction {
        @Override
        public LuaValue call(LuaValue group, LuaValue msg) {
            if (group instanceof LuaGroup) {
                if (msg instanceof LuaString) return sendMsg((LuaGroup) group, (LuaString) msg);
                if (msg instanceof LuaMsg) return sendMsg((LuaGroup) group, (LuaMsg) msg);
            }
            return null;
        }

        public abstract LuaValue sendMsg(LuaGroup group, LuaString msg);

        public abstract LuaValue sendMsg(LuaGroup group, LuaMsg luaMsg);
    }

    public static abstract class GroupOpFunction extends VarArgFunction {
        protected int type;

        public GroupOpFunction(int type) {
            this.type = type;
        }

        @Override
        public Varargs onInvoke(Varargs varargs) {
            if (varargs.arg1() instanceof LuaGroup) {
                return op(varargs);
            }
            return LuaValue.NIL;
        }

        public abstract Varargs op(Varargs varargs);
    }

    public abstract SendGroupMsgFunction getSendGroupMsgFunction();

    public abstract GroupOpFunction getGroupOpFunction(int type);

    @Override
    public int type() {
        return 0;
    }

    @Override
    public String typename() {
        return "LuaGroup";
    }
}
