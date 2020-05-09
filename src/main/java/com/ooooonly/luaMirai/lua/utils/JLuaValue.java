package com.ooooonly.luaMirai.lua.utils;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public class JLuaValue {
    public static LuaValue toLuaValue(Object o) {
        if (o == null) return LuaValue.NIL;
        if (o instanceof Integer) return LuaValue.valueOf((Integer) o);
        if (o instanceof String) return LuaValue.valueOf((String) o);
        if (o instanceof Boolean) return LuaValue.valueOf((Boolean) o);
        if (o instanceof Double) return LuaValue.valueOf((Double) o);
        if (o instanceof Float) return LuaValue.valueOf((Float) o);
        if (o instanceof List) {
            LuaTable table = new LuaTable();
            List list = (List) o;
            for (Object o1 : list) table.insert(table.length(), toLuaValue(o1));
            return table;
        }
        if (o instanceof Map) {
            LuaTable table = new LuaTable();
            Map<Object, Object> map = (Map) o;
            for (Object entry : map.entrySet()) {
                Map.Entry entryTmp = (Map.Entry) entry;
                Object key = entryTmp.getKey();
                Object value = entryTmp.getValue();
                if (key != null && value != null) {
                    table.set(
                            LuaValue.valueOf(key.toString()),
                            toLuaValue(value)
                    );
                }
            }
            return table;
        }
        Field[] fields = o.getClass().getDeclaredFields();
        LuaTable table = new LuaTable();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                String name = field.getName();
                LuaValue value = toLuaValue(field.get(o));
                if (name != null && value != null)
                    table.set(name, value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return table;
    }

    public static Object fromLuaValue(LuaValue table, Class clazz) {
        try {
            Object instance = clazz.getDeclaredConstructor().newInstance();
            //TODO
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
