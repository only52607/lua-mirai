package cn.qzwxsaedc.requests;


import kotlin.Suppress;
import okhttp3.OkHttpClient;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.lang.reflect.Method;

import static cn.qzwxsaedc.requests.entity.libs.Misc.*;

@Suppress(names = "unused")
public class LuaRequests extends Requests{
    private static final OkHttpClient CLIENT = new OkHttpClient();
    private void set_params (LuaValue value) {
        try{
            super.setParams(luaValueToTreeMap(String.class, String.class, value));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void set_data (LuaValue value) {
        try{
            setData((String) luaValueToClass(String.class, value));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void set_form (LuaValue value) {
        try{
            setForm((String) luaValueToClass(String.class, value));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void set_headers (LuaValue value) {
        try{
            setHeaders(luaValueToTreeMap(String.class, String.class, value));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void set_files (LuaValue value) {
        try{
            if(value.istable())
                setFiles(luaValueToArray(String[].class, value));
            else
                setFiles(new String[]{(String)luaValueToClass(String.class, value)});
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void set_auth (LuaValue value) {
        try{
            setAuth(luaValueToArray(String[].class, value));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void set_timeout (LuaValue value) {
        try{
            if(value.istable())
                setTimeout(luaValueToArray(Long[].class, value));
            else
                setTimeout(new Long[]{(Long)luaValueToClass(Long[].class, value)});
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void set_allow_redirects (LuaValue value) {
        try{
            setAllowRedirects((boolean) luaValueToClass(boolean.class, value));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void set_verify (LuaValue value) {
        try{
            setVerify((String) luaValueToClass(String.class, value));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void set_stream (LuaValue value) {
        try{
            setStream((boolean) luaValueToClass(boolean.class, value));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void set_cert (LuaValue value) {
        try{
            setCert((String) luaValueToClass(String.class, value));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public LuaRequests(OkHttpClient client){
        super(client);
    }

    public static Response call_request(String method, String url, LuaTable table) throws Exception {
        Requests requests = new LuaRequests(CLIENT);

        for(LuaValue key : table.keys()){
            Method setter = LuaRequests.class.getDeclaredMethod("set_" + key.tojstring(), LuaValue.class);
            setter.setAccessible(true);
            System.err.println(key.tojstring());
            setter.invoke(requests, table.get(key));
        }

        return requests.requests(method, url);
    }
}
