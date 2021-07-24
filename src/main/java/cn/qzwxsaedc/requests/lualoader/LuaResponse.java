package cn.qzwxsaedc.requests.lualoader;

import cn.qzwxsaedc.requests.Response;

import java.io.IOException;

public class LuaResponse extends Response {
    public LuaResponse(okhttp3.Response response) throws IOException {
        super(response);
    }
}
