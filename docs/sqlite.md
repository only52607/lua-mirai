#Lua 中使用 SQLite3

##创建数据库对象
####函数名: SQLite3
####参数列表
|参数名|参数类型|可选|描述|
|-----|-------|---|---|
|database|String|是|数据库名称，也可以为文件路径.<br>默认创建内存数据库.|
####示例
```lua
local db = SQLite3("example.db") -- 在脚本的运行目录创建example.db数据库文件

local memory_db = SQLite3()      -- 在内存中创建一个数据库，程序退出时销毁
```

####返回值
返回一个table，用于对创建的数据库的操作

##操作数据库
###1. exec函数
####参数列表
|参数名|参数类型|可选|描述|
|---|---|---|---|
|sql|String|否|不进行语法检查. 请保证语法的正确.<br>用于执行类的sql语句.|
####示例
```lua
local memory_db = SQLite3() -- 在内存中创建一个数据库，程序退出时销毁
memory_db:exec("create table if not exists example (id integer primary key, content varchar);") -- 通过exec()创建一个表
memory_db:exec([[
insert into example values(1, 'Hello World');
insert into example values(2, 'Hello Lua');
insert into example values(3, 'Hello Sqlite3');
]]) -- 一次执行多个sql语句
```
##2. nrows函数

####参数列表
|参数名|参数类型|可选|描述|
|-----|-------|---|---|
|sql|String|否|不进行语法检查. 请保证语法的正确.<br>用于查询类的sql语句.|
该函数会返回一个嵌套Table。<br>
由于只能在使用`as`语句时获取到可用的列标题，因此使用Integer作为key, 与查询时选择的列顺序相对应.<br>

####示例
```lua
local memory_db = SQLite3() -- 在内存中创建一个数据库，程序退出时销毁
memory_db:exec("create table if not exists example (id integer primary key, content varchar);") -- 通过exec()创建一个表
memory_db:exec([[
insert into example values(1, 'Hello World');
insert into example values(2, 'Hello Lua');
insert into example values(3, 'Hello Sqlite3');
]]) -- 一次执行多个sql语句
for id, table in ipairs(memory_db:nrows("select * from example;") do -- 通过nrows()查询表中的数据
    print(id, table[1])
end
```
##3*. createTable函数

####参数列表
|参数名|参数类型|可选|描述|
|-----|-------|---|---|
|table_name|String|否|将要创建的表的名称|
|values|Table|否|表的列表组|
该函数可以通过Table创建一个表，默认使用`create if not exists`以防止重复创建表。<br>
效果与`exec("create if not exists ...")`相同。
#####列表组
每个列表组使用一个Table，所有键需要封装到一个大的Table里.<br>
每个列表需要三个参数: 列表标题，数据类型，约束类型，分别为一个String存储到Table中。其中约束类型为可选参数。所有的约束类型共用一个String。<br>
例如，一个标题为id，数据类型为integer，要求非空(not null)，且不存在相同值(unique)的列可以这样来添加:
```lua
{"id", "integer", "not null unique"}
```
####示例
```lua
local memory_db = SQLite3() -- 在内存中创建一个数据库，程序退出时销毁
memory_db:createTable("example", {
    {"id",      "integer", "primary key"},
    {"content", "varchar"}
}) -- createTable()创建一个表
memory_db:exec([[
insert into example values(1, 'Hello World');
insert into example values(2, 'Hello Lua');
insert into example values(3, 'Hello Sqlite3');
]]) -- 一次执行多个sql语句
for id, table in ipairs(memory_db:nrows("select * from example;") do -- 通过nrows()查询表中的数据
    print(id, table[1])
end
```
##注意事项
1. 仅支持了部分常用的数据类型，例如varchar, char, integer, blob等。如果查询时返回了nil，可能是数据类型不被支持。
2. *理论上* 支持所有的sqlite操作。但仍有可能有些操作会执行失败。
3. exec函数内部使用的executeUpdate()函数，每次操作都会直接更新数据库。
4. 为了避免不必要的开销和某些诡异的bug，SQLite3()函数*不要* 在任何可能会重复执行的地方使用。
5. jdbc-sqlite3在查询时无法获取到准确的列标题，获取到的大多为标题+数据类型+约束类型(例如"idintegerprimarykey")。