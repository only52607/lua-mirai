package com.ooooonly.luaMirai.lua;


import org.luaj.vm2.LuaString;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.TwoArgFunction;

public abstract class LuaQQ extends LuaTable {
    public LuaQQ(LuaBot luaBot, long id) {
        this.set("id", LuaValue.valueOf(id));
        this.set("bot", luaBot);
        this.set("sendMsg", getSendFriendMsgFunction());
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

    public abstract SendFriendMsgFunction getSendFriendMsgFunction();

    @Override
    public int type() {
        return 0;
    }

    @Override
    public String typename() {
        return "LuaQQ";
    }
}
