package com.ooooonly.luaMirai.lua.lib

import com.ooooonly.luakt.edit
import com.ooooonly.luakt.luaFunctionOf
import net.mamoe.mirai.utils.DefaultLogger
import net.mamoe.mirai.utils.MiraiLoggerWithSwitch
import net.mamoe.mirai.utils.withSwitch
import org.luaj.vm2.Globals
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue
import org.luaj.vm2.lib.OneArgFunction
import org.luaj.vm2.lib.TwoArgFunction
import java.lang.Exception
import java.sql.*

open class SQLiteLib() : TwoArgFunction(){
    private var status = false;
    companion object{
        var logger: MiraiLoggerWithSwitch = DefaultLogger("SQLiteLib").withSwitch(true);
    }
    init{
        try{
            Class.forName("org.sqlite.JDBC");
            status = true;
        }catch(e: ClassNotFoundException){
            logger.error("初始化失败。");
            logger.error(e);
        }
    }
    class SQLite3(db_name: String?) : LuaTable(){
        private val memoryDatabase = ":memory:";
        private var connection: Connection? = null;
        private var statement: Statement? = null;

        init {
            var databaseName = db_name ?: ":memory:";
            try{
                connection = DriverManager.getConnection("jdbc:sqlite:${db_name ?: memoryDatabase}");
                statement = connection?.createStatement();
            }catch (e: SQLException){
                logger.error("${databaseName}打开失败。");
                logger.error(e);
            }

            this.edit {
                "exec"        to luaFunctionOf { _: LuaTable, operator: String -> exec(operator); }
                "nrows"       to luaFunctionOf { _: LuaTable, operator: String -> nrows(operator); }
                "createTable" to luaFunctionOf { _: LuaTable, table: String, values: LuaTable -> createTable(table, values); }
            }
        }
        fun exec(operator: String) : Int{
            var status: Int = -10;
            try{
                status = statement?.executeUpdate(operator) ?: -11;
            }catch (e: SQLException){
                logger.error(e);
            }
            return status;
        }
        private fun autoType(cursor: ResultSet, column: Int) : LuaValue{
            var value: LuaValue = LuaValue.NIL;
            var obj: Any = cursor.getObject(column) ?: return value;
            when(cursor.metaData.getColumnType(column)){
                Types.BIT, Types.BOOLEAN
                        -> value = LuaValue.valueOf(obj as Boolean);
                Types.CHAR, Types.NCHAR, Types.VARCHAR, Types.LONGVARCHAR,  Types.NVARCHAR, Types.LONGNVARCHAR
                        -> value = LuaValue.valueOf(obj as String);
                Types.DECIMAL, Types.FLOAT, Types.DOUBLE, Types.REAL, Types.NUMERIC
                        -> value = LuaValue.valueOf(obj as Double);
                Types.BLOB
                        -> value = LuaValue.valueOf(obj as ByteArray);
                Types.INTEGER
                        -> value = LuaValue.valueOf(obj as Int);
                Types.BIGINT
                        -> value = LuaValue.valueOf(obj as Int);//Int -> Long
                Types.SMALLINT
                        -> value = LuaValue.valueOf(obj as Int);//Int -> Short
                else    -> value = LuaValue.NIL;
            }
            return value;
        }
        fun nrows(operator: String) : LuaValue {
            var mTable: LuaValue = LuaValue.NIL;
            if(statement == null)   return mTable;
            var set: ResultSet;
            try{
                set = statement!!.executeQuery(operator);
                var tmp: LuaTable = LuaTable();
                while(set.next()){
                    var table: LuaTable = LuaTable();
                    var len: Int = set.metaData.columnCount;
                    for(i in 1..len){
                        try{
                            table.set(i, autoType(set, i));
                        }catch (e: SQLException){
                            logger.error("第${set.row}行第${i}列数据选择类型时出现异常。类型id: ${set.metaData.getColumnType(i)}");
                            logger.error(e);
                        }
                    }
                    tmp.set(set.row, table);
                }
                mTable = tmp;
            }catch (e: SQLException){
                logger.error(e);
            }
            return mTable;
        }

        fun createTable(table: String, values: LuaTable) : Int{
            var str: String = "";
            if(values.length() < 1) throw Exception("无变量列表。创建表操作失败。");
            for(i in 1..values.length()){
                var tmp = values[i].checktable();
                str += when(tmp.length()){
                    2 -> ", ${tmp[1].checkjstring()} ${tmp[2].checkjstring()}";
                    3 -> ", ${tmp[1].checkjstring()} ${tmp[2].checkjstring()} ${tmp[3].checkjstring()}";
                    else -> throw Exception("参数长度不合法。创建表操作失败。");
                }
            }
            return exec("create table if not exists $table ( ${str.substring(2)} );");
        }
    }
    fun create(db_name: String?): SQLite3{
        logger.info("create");
        return SQLite3(db_name);
    }
    override fun call(self: LuaValue?, env: LuaValue): LuaValue {
        env.checkglobals().set("SQLite3", object : OneArgFunction() {
            override fun call(name: LuaValue?): LuaValue {
                return create(name?.checkjstring());
            }
        });
        return LuaValue.NIL;
    }
}
