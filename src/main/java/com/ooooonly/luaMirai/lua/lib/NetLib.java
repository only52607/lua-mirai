package com.ooooonly.luaMirai.lua.lib;

import com.ooooonly.luaMirai.lua.LuaBot;
import com.ooooonly.luaMirai.lua.utils.JLuaValue;
import net.mamoe.mirai.event.Event;
import org.luaj.vm2.*;
import org.luaj.vm2.lib.TwoArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class NetLib extends TwoArgFunction {
    @Override
    public LuaValue call(LuaValue modName, LuaValue env) {
        Globals globals = env.checkglobals();
        LuaTable netTable = new LuaTable();
        LuaFunction requestFun = new VarArgFunction() {
            @Override
            public Varargs onInvoke(Varargs args) {
                RequestParams rp = new RequestParams();
                rp.urlString = args.optjstring(1, "");
                rp.method = args.optjstring(2, "GET");
                rp.postData = args.optjstring(3, "");
                LuaValue arg4 = args.arg(4);

                //Other parameters
                //TODO ("use serializer")
                if (arg4 instanceof LuaTable) {
                    LuaValue tmp;
                    rp.followRedirects = ((tmp = arg4.rawget("followRedirects")) == LuaValue.NIL) ? rp.followRedirects : tmp == LuaValue.TRUE;
                    rp.connectTimeout = ((tmp = arg4.rawget("connectTimeout")) == LuaValue.NIL) ? rp.connectTimeout : tmp.optint(rp.connectTimeout);
                    rp.readTimeout = ((tmp = arg4.rawget("readTimeout")) == LuaValue.NIL) ? rp.readTimeout : tmp.optint(rp.readTimeout);

                    //requestPropertyTable
                    Map<String, List<String>> requestProperty = null;
                    LuaValue requestPropertyTable = arg4.rawget("requestProperty");
                    if (requestPropertyTable instanceof LuaTable) {
                        requestProperty = new HashMap();
                        LuaValue[] keys = ((LuaTable) requestPropertyTable).keys();
                        for (int i = 0; i < keys.length; i++) {
                            LuaValue value = requestPropertyTable.get(keys[i]);
                            List<String> list = new ArrayList();
                            if (value instanceof LuaTable) {
                                for (int j = 0; j < value.length(); j++) {
                                    list.add(value.get(j).toString());
                                }
                            } else {
                                list.add(value.toString());
                            }
                            requestProperty.put(keys[i].toString(), list);
                        }
                    }
                    rp.requestProperty = requestProperty;
                }

                //Send request
                try {
                    Response response = makeRequest(rp);
                    return LuaValue.varargsOf(
                            new LuaValue[]{
                                    LuaValue.valueOf(response.content), JLuaValue.toLuaValue(response)
                            }
                    );
                } catch (Exception e) {
                    throw new LuaError(e.getMessage());
                }
            }
        };

        netTable.set("get", new VarArgFunction() {
            @Override
            public Varargs onInvoke(Varargs args) {
                return requestFun.invoke(
                        LuaValue.varargsOf(
                                new LuaValue[]{
                                        args.arg(1),
                                        LuaString.valueOf("GET"),
                                        LuaValue.NIL,
                                        args.arg(2)
                                }
                        )
                );
            }
        });
        netTable.set("post", new VarArgFunction() {
            @Override
            public Varargs onInvoke(Varargs args) {
                return requestFun.invoke(
                        LuaValue.varargsOf(
                                new LuaValue[]{
                                        args.arg(1),
                                        LuaString.valueOf("POST"),
                                        args.arg(2),
                                        args.arg(3)
                                }
                        )
                );
            }
        });
        netTable.set("request", requestFun);
        globals.set("net", netTable);
        globals.set("Net", netTable);
        return LuaValue.NIL;
    }

    /*
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
    */
    private Response makeRequest(RequestParams rp) throws Exception {
        URL url = new URL(rp.urlString);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setConnectTimeout(rp.connectTimeout);
        urlConnection.setReadTimeout(rp.readTimeout);
        urlConnection.setDoInput(true);
        urlConnection.setInstanceFollowRedirects(rp.followRedirects);
        if (rp.method.equals("POST")) {
            urlConnection.setRequestMethod(rp.method);
            urlConnection.setDoOutput(true);
            urlConnection.setUseCaches(false);
            OutputStream out = urlConnection.getOutputStream();
            out.write(rp.postData.getBytes());
            out.flush();
            out.close();
        }
        for (Map.Entry<String, List<String>> entry : rp.requestProperty.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            boolean first = true;
            for (String value : values) {
                if (first) {
                    urlConnection.setRequestProperty(key, value);
                    first = false;
                    continue;
                }
                urlConnection.addRequestProperty(key, value);
            }
        }

        Response response = new Response();
        response.responseCode = urlConnection.getResponseCode();
        response.responseMessage = urlConnection.getResponseMessage();
        response.headerFields = urlConnection.getHeaderFields();
        StringBuilder sb = new StringBuilder();
        if (response.responseCode == 200) {
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            char[] bytes = new char[1024];
            int len = 0;
            while ((len = br.read(bytes)) != -1) {
                sb.append(new String(bytes, 0, len));
            }
        }
        response.content = sb.toString();
        return response;
    }

    private static class RequestParams {
        String urlString;
        String method = "GET";
        String postData;
        Integer connectTimeout = 3000;
        Integer readTimeout = 3000;
        Boolean followRedirects = true;
        Map<String, List<String>> requestProperty = new HashMap();
    }

    private static class Response {
        String content;
        Integer responseCode;
        String responseMessage;
        Map<String, List<String>> headerFields;
    }
}