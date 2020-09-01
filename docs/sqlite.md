# Sqlite支持库

#### 支持库基于[sqlite-jdbc](https://github.com/xerial/sqlite-jdbc)，详细api文档请自行查询，用法与java类似。

### 创建Sqlite连接

##### 函数名：SqliteConnection

##### 参数列表：

| 参数 | 类型   | 描述                 | 可空  |
| ---- | ------ | -------------------- | ----- |
| path | String | sqlite数据库文件路径 | False |

##### 返回值：

| 类型       | 描述           |
| ---------- | -------------- |
| Connection | Connection对象 |

#### 示例：

```lua
local connection = SqliteConnection("person.db") -- 创建connection
local statement = connection:createStatement() -- 创建一个statement，以执行sql语句
statement:setQueryTimeout(30) -- 设置查询超时时间
statement:executeUpdate("drop table if exists person") -- 执行sql语句
statement:executeUpdate("create table person (id integer, name string)")
statement:executeUpdate("insert into person values(1, 'leo')")
statement:executeUpdate("insert into person values(2, 'yui')")
local rs = statement:executeQuery("select * from person") -- 执行sql语句并返回结果
while rs:next() do --遍历返回结果
	print("name = " .. rs:getString("name"))
	print("id = " .. tostring(rs:getInt("id")))
end
```
