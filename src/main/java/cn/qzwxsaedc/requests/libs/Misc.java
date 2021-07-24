package cn.qzwxsaedc.requests.libs;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.lang.reflect.Array;
import java.util.TreeMap;

import static cn.qzwxsaedc.requests.libs.LuaValueEx.*;

public class Misc {
    public static <K, T>TreeMap<K, T> luaValueToTreeMap(Class<?> keyType, Class<?> valType, LuaValue luaValue){
        LuaTable table = luaValue.checktable();
        TreeMap<K, T> map = new TreeMap<>();
        for(LuaValue key : table.keys())
            map.put(((K) to_object(key, keyType)),
                    (T)to_object(table.get(key), valType));
        return map;
    }

    public static <T>T[] luaValueToArray(Class<T[]> type, LuaValue luaValue){
        LuaTable table = luaValue.checktable();
        T[] objects = (T[]) Array.newInstance(type.getComponentType(), table.length());

        for(int i = 1; i <= objects.length; i++)
            objects[i - 1] = (T)to_object(luaValue.get(i), type.getComponentType());

        return objects;
    }

    public static Object luaValueToClass(Class<?> type, LuaValue luaValue){
        return to_object(luaValue, type);
    }
}
