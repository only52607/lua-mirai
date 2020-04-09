package com.ooooonly.luaMirai.lua;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.VarArgFunction;


abstract public class LuaGroupMember extends LuaTable {

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

    public LuaGroupMember(LuaBot luaBot, LuaGroup luaGroup, long id) {
        this.set("bot", luaBot);
        this.set("group", luaGroup);
        this.set("id", LuaValue.valueOf(id));
        this.set("getNick", getMemberOpFunction(GET_NICK));
        this.set("getNameCard", getMemberOpFunction(GET_NAME_CARD));
        this.set("getMuteRemain", getMemberOpFunction(GET_MUTE_REMAIN));
        this.set("getSpecialTitle", getMemberOpFunction(GET_SPECIAL_TITLE));
        this.set("isMuted", getMemberOpFunction(IS_MUTE));
        this.set("isAdministrator", getMemberOpFunction(IS_ADMINISTRATOR));
        this.set("isOwner", getMemberOpFunction(IS_OWNER));
        this.set("mute", getMemberOpFunction(MUTE));
        this.set("unMute", getMemberOpFunction(UN_MUTE));
        this.set("kick", getMemberOpFunction(KICK));
    }


    public static abstract class MemberOpFunction extends VarArgFunction {
        protected int type;

        public MemberOpFunction(int type) {
            this.type = type;
        }

        @Override
        public Varargs onInvoke(Varargs varargs) {
            if (varargs.arg1() instanceof LuaGroupMember) {
                return op(varargs);
            }
            return LuaValue.NIL;
        }

        public abstract Varargs op(Varargs varargs);
    }

    public abstract LuaGroupMember.MemberOpFunction getMemberOpFunction(int type);

    @Override
    public int type() {
        return 0;
    }

    @Override
    public String typename() {
        return "LuaGroupMember";
    }
}
