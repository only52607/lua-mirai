package com.ooooonly.luaMirai.lua.lib;

import com.ooooonly.luaMirai.lua.LuaBot;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetLib extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue modName, LuaValue env) {
        Globals globals = env.checkglobals();
        LuaTable netTable = new LuaTable();
        netTable.set("get", new Get());
        netTable.set("post", new Post());
        globals.set("net", netTable);
        return LuaValue.NIL;
    }

    public class Get extends VarArgFunction {
        @Override
        public Varargs onInvoke(Varargs varargs) {
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(varargs.checkjstring(1));
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(5 * 1000);
                urlConnection.setReadTimeout(5 * 1000);
                if (urlConnection.getResponseCode() == 200) {
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    char[] bytes = new char[1024];
                    int len = 0;
                    while ((len = br.read(bytes)) != -1) {
                        sb.append(new String(bytes, 0, len));
                    }
                }
            } catch (Exception e) {
                throw new LuaError(e);
            }
            System.out.println(sb);
            return LuaValue.valueOf(sb.toString());
        }
    }

    public class Post extends VarArgFunction {
        @Override
        public Varargs onInvoke(Varargs varargs) {
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(varargs.checkjstring(1));
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setConnectTimeout(5 * 1000);
                urlConnection.setReadTimeout(5 * 1000);
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoOutput(true);
                urlConnection.getOutputStream().write(varargs.optjstring(2, "").getBytes());
                if (urlConnection.getResponseCode() == 200) {
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
                    char[] bytes = new char[1024];
                    int len = 0;
                    while ((len = br.read(bytes)) != -1) {
                        sb.append(new String(bytes, 0, len));
                    }
                }
            } catch (Exception e) {
                throw new LuaError(e);
            }
            return LuaValue.valueOf(sb.toString());
        }
    }

}