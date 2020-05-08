package com.ooooonly.luaMirai.lua;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

public abstract class LuaObject extends LuaTable {
    protected static final int TYPE_CONTACT = 100;
    protected static final int TYPE_MSG = 101;
    protected static final int TYPE_SOURCE = 102;

    protected LuaObject() {
        this.setmetatable(getMetaTable());
    }

    protected abstract LuaTable getMetaTable();

    protected abstract OpFunction getOpFunction(int opcode);

    protected SubscribeFunction getSubscribeFunction(int opcode) {
        return null;
    }

    public static abstract class OpFunction extends VarArgFunction {
        protected int opcode;

        public OpFunction(int opcode) {
            this.opcode = opcode;
        }

        @Override
        public Varargs onInvoke(Varargs varargs) {
            return op(varargs);
        }

        public abstract Varargs op(Varargs varargs);
    }

    public static abstract class SubscribeFunction extends TwoArgFunction {
        protected int opcode;

        public SubscribeFunction(int opcode) {
            this.opcode = opcode;
        }

        @Override
        public LuaValue call(LuaValue self, LuaValue listener) {
            if (!(listener instanceof LuaFunction)) return LuaValue.NIL;
            onSubscribe(self, (LuaFunction) listener);
            return self;
        }

        public abstract LuaValue onSubscribe(LuaValue self, LuaFunction listener);
    }

    @Override
    public String typename() {
        return this.getClass().getSimpleName();
    }
}
