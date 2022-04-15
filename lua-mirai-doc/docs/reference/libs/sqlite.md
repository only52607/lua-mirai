# sqlite支持库

#### 支持库基于 [sqlite-jdbc](https://github.com/xerial/sqlite-jdbc) ，详细 api 文档请自行查询，用法与 java 类似。

### 创建 Sqlite 连接

##### 函数名：SqliteConnection

##### 参数列表：

| 参数 | 类型   | 描述                 | 可空  |
| ---- | ------ | -------------------- | ----- |
| path | String | sqlite 数据库文件路径 | False |

##### 返回值：

| 类型       | 描述           |
| ---------- | -------------- |
| Connection | Connection 对象 |

### 创建语句执行环境

##### 函数名： createStatement

##### 参数列表：

| 参数 | 类型       | 描述                 | 可空  |
| ---- | ---------- | -------------------- | ----- |
| conn | Connection | 打开的数据库         | False |
| path | String     | sqlite 数据库文件路径 | False |

##### 返回值：

| 类型       | 描述           |
| ---------- | -------------- |
| Statement | Statement 对象 |

### 创建语句执行环境

##### 函数名： createStatement

##### 参数列表：

| 参数 | 类型       | 描述                 | 可空  |
| ---- | ---------- | -------------------- | ----- |
| conn | Connection | 打开的数据库         | False |
| path | String     | sqlite 数据库文件路径 | False |

##### 返回值：

| 类型       | 描述           |
| ---------- | -------------- |
| Statement | Statement 对象 |

### 设置语句执行超时时间

##### 函数名： setQueryTimeout()


| 参数    | 类型      | 描述           | 可空  |
| ------- | --------- | -------------- | ----- |
| stat    | Statement | 语句的执行环境 | False |
| seconds | Float     | 设置超时的时间 | False |

##### 返回值：（空）

### 更新语句

executeUpdate ()

### 查询语句

executeQuery ()



## 如何定位一个数据库文件

下面是一个例子，关于如何建立对 Windows 下数据库 `C:\work\mydatabase.db` 的连接

```lua
local connection = SqliteConnection ("C:/work/mydatabase.db");
```

打开一个 UNIX (Linux, Mac OS X, etc.) 等的文件 `/home/leo/work/mydatabase.db`

```lua
local connection = SqliteConnection ("/home/leo/work/mydatabase.db");
```

## 如何使用内存上的数据库

SQLite 支持内存数据库管理, 不会创建任何数据库文件. 如何用 Java 连接数据库可以参考下面的例子


```lua
local connection = SqliteConnection (":memory:");
```

当然，你也可以像下面那样创建一个数据库
```lua
local connection = SqliteConnection ("");
```

## 如何使用在线备份和恢复功能

创建 `backup.db` 备份整个数据库的方法

```lua

-- 创建一个内存数据库
local conn = SqliteConnection ("");
local stmt = conn:createStatement();
-- 做一些更新
stmt:executeUpdate("create table sample(id, name)");
stmt:executeUpdate("insert into sample values(1, \"leo\")");
stmt:executeUpdate("insert into sample values(2, \"yui\")");
-- 把数据库内容 dump 入文件
stmt:executeUpdate("backup to backup.db");
-- 创建一个内存数据库
local conn = SqliteConnection ("");
-- 从备份文件中恢复数据库
local stat = conn:createStatement();
stat:executeUpdate("restore from backup.db");

```

## 创建一个二进制数据

1. 创建一个表，带有 blob 类型的一列 `create table T (id integer, data blob)`、
2. 用 `?` 创建一个初始化状态 `insert into T values(1, ?)`、
3. 用字节数组准备一个二进制数据 (e.g., `byte[] data = ...`)
4. `preparedStatement.setBytes(1, data)`
5. `preparedStatement.execute()...`

## 通过网络以只读打开数据库

外部数据库资源可以像下面这样被使用


```lua
local conn = SqliteConnection (":resource:http://www.xerial.org/svn/project/XerialJ/trunk/sqlite-jdbc/src/test/java/org/sqlite/sample.db"); 

```


## 配置一个连接


```java

config = new SQLiteConfig();
// config.setReadOnly(true);   
config.setSharedCache(true);
config.recursiveTriggers(true);
// ... other configuration can be set via SQLiteConfig object
Connection conn = DriverManager.getConnection("jdbc:sqlite:sample.db", config.toProperties());
```

## 如何使用加密的数据库

*__Important: xerial/sqlite-jdbc does not support encryption out of the box, you need a special .dll/.so__*

SQLite 支持通过特殊的 driver 和密钥加密数据库。使用一个被加密的数据库，你需要一个支持通过 `pragma key` or `pragma hexkey` 加密的 driver，例如，SQLite SSE 或者 SQLCipher，
SQLite support encryption of the database via special drivers and a key. To use an encrypted database you need a driver which supports encrypted database via `pragma key` or `pragma hexkey`, e.g. SQLite SSE or SQLCipher. You need to specify those drivers via directly referencing the .dll/.so through:

```
-Dorg.sqlite.lib.path=.
-Dorg.sqlite.lib.name=sqlite_cryption_support.dll
```

Now the only need to specify the password is via:
```java
Connection connection = DriverManager.getConnection("jdbc:sqlite:db.sqlite", "", "password");
```
### 二进制通行口令
If you need to provide the password in binary form, you have to specify how the provided .dll/.so needs it. There are two different modes available:

#### SSE:
The binary password is provided via `pragma hexkey='AE...'`

#### SQLCipher:
The binary password is provided via `pragma key="x'AE...'"`

You set the mode at the connectionstring level:
```java
Connection connection = DriverManager.getConnection("jdbc:sqlite:db.sqlite?hexkey_mode=sse", "", "AE...");
```



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
