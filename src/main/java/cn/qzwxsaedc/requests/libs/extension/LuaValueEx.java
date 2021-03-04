package cn.qzwxsaedc.requests.libs;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;

public class LuaValueEx {
    public static Object to_object(LuaValue value, Class<?> type){
        if(value.isstring() && type == String.class)
            return value.tojstring();
        else if(value.isboolean() && (type == boolean.class || type == Boolean.class))
            return value.toboolean();
        else if (value.isint() && (type == int.class || type == Integer.class))
            return value.toint();
        else if (value.islong() && (type == long.class || type == Long.class))
            return value.tolong();
        else if (value.isnumber()) {
            if (type == float.class || type == Float.class)
                return value.tofloat();
            else if (type == double.class || type == Double.class)
                return value.todouble();
            else if (type == short.class || type == Short.class)
                return value.toshort();
            else if (type == byte.class || type == Byte.class)
                return value.tobyte();
            else
                throw new ClassCastException(String.format("无法进行转换: 不支持的类型 %s", type.getName()));
        }
        else if (value.isnil())
            return null;
        else if (value.istable() && type == LuaTable.class)
            return value.checktable();
        else
            throw new ClassCastException(
                    String.format("源类型和目标类型冲突: 需要的类型为%s，但提供的类型为%s",
                            type.getName(),
                            value.typename()));
    }
}
