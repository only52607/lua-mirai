package com.ooooonly.luaMirai.lua.lib;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

public class LuaJavaExLib extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue modName, LuaValue env) {
        Globals globals = env.checkglobals();

        globals.set("__bindClass__", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                JavaClassProxy proxy = new JavaClassProxy(globals, (LuaString) arg);
                JavaClassProxyMetaTable proxyMetatable = JavaClassProxyMetaTable.getInstance(globals);
                proxy.setmetatable(proxyMetatable);

                return proxy;
            }
        });
        globals.set("import", new OneArgFunction() {
            @Override
            public LuaValue call(LuaValue arg) {
                String name = arg.tojstring();
                JavaClassProxy proxy = new JavaClassProxy(globals, (LuaString) arg);
                JavaClassProxyMetaTable proxyMetatable = JavaClassProxyMetaTable.getInstance(globals);
                proxy.setmetatable(proxyMetatable);
                globals.set(name.substring(name.lastIndexOf(".") + 1), proxy);
                return LuaValue.NIL;
            }
        });

        return LuaValue.NIL;
    }

    static class JavaClassProxy extends LuaTable {
        public LuaValue clazz;
        public LuaString className;

        public JavaClassProxy(Globals globals, LuaString className) {
            LuaTable luajava = (LuaTable) globals.get("luajava");
            LuaFunction bindClass = (LuaFunction) luajava.get("bindClass");
            this.clazz = bindClass.call(className);
            this.className = className;
        }
    }

    static class JavaClassProxyMetaTable extends LuaTable {
        public static JavaClassProxyMetaTable instance;
        public static Globals globals;

        public static JavaClassProxyMetaTable getInstance(Globals globals) {
            if (instance != null) return instance;
            JavaClassProxyMetaTable.globals = globals;
            return new JavaClassProxyMetaTable();
        }

        public JavaClassProxyMetaTable() {
            LuaTable luajava = (LuaTable) globals.get("luajava");
            LuaFunction newInstance = (LuaFunction) luajava.get("new");
            LuaFunction createProxy = (LuaFunction) luajava.get("createProxy");
            this.set("__call", new VarArgFunction() {
                @Override
                public Varargs onInvoke(Varargs args) {

                    if (args.arg(2) instanceof LuaTable) {
                        return createProxy.invoke(new Varargs() {
                            @Override
                            public LuaValue arg(int i) {
                                if (i == 1) return ((JavaClassProxy) args.arg1()).className;
                                return args.arg(i);
                            }

                            @Override
                            public int narg() {
                                return args.narg();
                            }

                            @Override
                            public LuaValue arg1() {
                                return arg(1);
                            }

                            @Override
                            public Varargs subargs(int start) {
                                return args.subargs(start);
                            }
                        });
                    }
                    return newInstance.invoke(new Varargs() {
                        @Override
                        public LuaValue arg(int i) {
                            if (i == 1) return ((JavaClassProxy) args.arg1()).clazz;
                            return args.arg(i);
                        }

                        @Override
                        public int narg() {
                            return args.narg();
                        }

                        @Override
                        public LuaValue arg1() {
                            return arg(1);
                        }

                        @Override
                        public Varargs subargs(int start) {
                            return args.subargs(start);
                        }
                    });
                }
            });
            this.set("__index", new TwoArgFunction() {
                @Override
                public LuaValue call(LuaValue arg1, LuaValue arg2) {
                    return ((JavaClassProxy) arg1).clazz.get(arg2);
                }
            });
        }
    }

}
