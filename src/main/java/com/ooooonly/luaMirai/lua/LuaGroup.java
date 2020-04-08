package com.ooooonly.luaMirai.lua;

import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

public abstract class LuaGroup extends LuaTable {
    public LuaGroup(LuaBot luaBot, long id) {
        this.set("id", LuaValue.valueOf(id));
        this.set("bot", luaBot);
        this.set("sendMsg", getSendGroupMsgFunction());
        this.set("getMember", getGetMemberFunction());
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

    public static abstract class GetMemberFunction extends TwoArgFunction {
        @Override
        public LuaValue call(LuaValue group, LuaValue memberId) {
            if (group instanceof LuaGroup) {
                return getMember((LuaGroup) group, memberId);
            }
            return null;
        }

        public abstract LuaValue getMember(LuaGroup group, LuaValue memberId);
    }

    public abstract LuaGroup.SendGroupMsgFunction getSendGroupMsgFunction();

    public abstract LuaGroup.GetMemberFunction getGetMemberFunction();

    @Override
    public int type() {
        return 0;
    }

    @Override
    public String typename() {
        return "LuaGroup";
    }
}
