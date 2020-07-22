package com.ooooonly.luaMirai.lua;

import org.luaj.vm2.*;
import org.luaj.vm2.lib.OneArgFunction;
import org.luaj.vm2.lib.VarArgFunction;

public abstract class LuaMsg extends LuaSource {
    public static final int APPEND_TEXT = 0;
    public static final int APPEND_FACE = 1;
    public static final int APPEND_IMAGE = 2;
    public static final int APPEND_IMAGE_FLASH = 2;
    public static final int APPEND_AT = 3;
    public static final int APPEND_AT_ALL = 4;
    public static final int APPEND_LONG_TEXT = 5;
    public static final int APPEND_SHARE = 6;
    public static final int APPEND_FORWARD = 7;
    public static final int APPEND_POKE = 8;
    public static final int APPEND_SERVICE = 9;
    public static final int APPEND_XML = 10;
    public static final int APPEND_JSON = 11;
    public static final int SET_QUOTE = 12;
    public static final int GET_SOURCE = 13;
    public static final int GET_QUOTE = 14;
    public static final int TO_TABLE = 15;
    public static final int DOWNLOAD_IMAGE = 16;
    public static final int GET_IMAGE_URL = 17;

    private static LuaTable metaTable;

    public LuaMsg() {
        super();
    }

    @Override
    protected LuaTable getMetaTable() {
        if (metaTable != null) return metaTable;
        metaTable = new LuaTable();
        LuaTable index = new LuaTable();

        //将String类的metatable复制到msg
        LuaTable stringMetaTable = (LuaTable) LuaString.s_metatable.get(INDEX);
        LuaValue[] keys = stringMetaTable.keys();
        for (int i = 0; i < keys.length; i++) index.set(keys[i], stringMetaTable.get(keys[i]));

        index.set("appendText", getOpFunction(APPEND_TEXT));
        index.set("appendFace", getOpFunction(APPEND_FACE));
        index.set("appendImage", getOpFunction(APPEND_IMAGE));
        index.set("appendAt", getOpFunction(APPEND_AT));
        index.set("appendAtAll", getOpFunction(APPEND_AT_ALL));
        index.set("appendLongText", getOpFunction(APPEND_LONG_TEXT));
        index.set("appendShare", getOpFunction(APPEND_SHARE));
        index.set("appendService", getOpFunction(APPEND_SERVICE));
        index.set("appendJson", getOpFunction(APPEND_JSON));
        index.set("appendXml", getOpFunction(APPEND_XML));
        index.set("appendPoke", getOpFunction(APPEND_POKE));
        index.set("appendForward", getOpFunction(APPEND_FORWARD));

        index.set("setQuote", getOpFunction(SET_QUOTE));
        index.set("getQuote", getOpFunction(GET_QUOTE));
        index.set("recall", getOpFunction(RECALL));
        index.set("getSource", getOpFunction(GET_SOURCE)); //获得消息的一个引用，可用于撤回，引用回复
        index.set("downloadImage", getOpFunction(DOWNLOAD_IMAGE)); //获得消息的一个引用，可用于撤回，引用回复
        index.set("getImageUrl", getOpFunction(GET_IMAGE_URL)); //获得消息的一个引用，可用于撤回，引用回复
        index.set("toTable", getOpFunction(TO_TABLE)); //消息转为表

        metaTable.set(INDEX, index);
        return metaTable;
    }

    @Override
    public String checkjstring() {
        return toString();
    }

    @Override
    public LuaString checkstring() {
        return LuaString.valueOf(toString());
    }

    @Override
    public LuaValue tostring() {
        return LuaString.valueOf(this.toString());
    }

    @Override
    public String toString() {
        return tostring().toString();
    }

    @Override
    public int type() {
        return TYPE_MSG;
    }
}
