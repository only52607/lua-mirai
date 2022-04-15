# 基本使用

lua-mirai基于lua5.2版本，如果您使用了lua5.3中的部分特性，如位运算符，那么您可能需要更换为其他写法。同时，代码中的关键词支持unicode命名，不支持导入c语言编写的扩展库。

关于Lua5.2的其他说明，可以参阅以下链接。

[Lua 5.2 Reference Manual](https://www.lua.org/manual/5.2/manual.md)

[RUNOOB:Lua 教程](https://www.runoob.com/lua/lua-tutorial.md)

### lua-mirai api的命名风格

为了与lua标准库区分开，lua-mirai的api设计采用与java语言一致的命名风格（驼峰式命名法），对于类、table的命名采用大驼峰式命名，如`Http`、`Json`等，对于变量、方法或成员则采用小驼峰式命名，如`sendMessage`、`getFriend`等，且一般情况下，单词不使用缩写形式。

对于对象 (userdata) 的无参成员，一般使用名词命名，如`friend.name`、`friend.nick`，而不是`friend:getName`、`friend:getNick`，对于含参成员或方法，则采用**动词或动词+名词**的形式，如`bot:getFriend`，`friend:sendMessage`。

举个例子，假设您需要使用 id 为 123456 的账号向 id 为 987654 的账号发送"Hello"的消息，您的代码应该下面这样的：

```lua
local friend = bot:getFriend(987654) -- 首先获取friend对象
friend:sendMessage("Hello") --利用friend对象发送消息
```

### lua-mirai对象的基本类型——userdata

userdata是lua mirai中所有对象的基本类型，由于lua中并没有真正的面向对象设计，对于对象的方法的调用是在参数列表开头增加一个代表对象自身的参数实现的，当使用冒号":"的方式调用，lua会自动将对象作为第一个参数传入，如像friend对象发送消息，你需要写出下面这样的代码：

```lua
friend:sendMessage("Hello")
```

若您坚持不使用冒号调用，那么您的代码可能是下面这样的

```lua
friend.sendMessage(friend, "Hello")
```

## 脚本头信息 (Since 2.3.0)

脚本头信息是脚本开头包含的一段脚本基本信息，通常用来表示当前脚本的名称或版本号等信息。

基本格式：

脚本头部以`-- LuaMiraiScript --`开始，以`-- /LuaMiraiScript --`结束，且开始行必须处于脚本第一行。

脚本头部内容是以`--`注释符开始的键值对信息，以`:`作为分割符。

当前可用字段：

| 字段名    | 描述     |
| ------- | -------- |
| name  | 名称 |
| version | 版本号 |
| description | 描述信息   |
| author  | 作者     |

示例：
```
-- LuaMiraiScript --
-- name: 脚本示例
-- version: 1.0
-- description: 这是一个简单的脚本
-- author: ooooonly
-- /LuaMiraiScript --
```


## Bot对象

[Bot](./bot.md)对象代表了机器人自身，friend对象或group对象均由其产生，通常获取 Bot 对象有以下方法：

### 单独构造

#### 使用 Bot 方法构造 [Bot](./bot.md) 对象

``` lua
local bot = Bot(qq 账号,"qq 密码","device.json")
bot:login() -- 登录
```

由于手动构造的 Bot 不会自动登录，需要手动调用 login 函数进行登录。

### 获取已创建的Bot对象（适合插件环境使用）
对于上述使用 Bot 函数创建的对象，Bot 对象创建完毕后，该对象会保存入名为`Bots`的全局表中，如下：
``` lua
for id, bot in pairs(Bots) do --遍历所有的bot对象
    print(id..":"..tostring(bot.md))
end
```

> 了解更多：[Bot](./bot.md)

## 事件监听
### 监听全部事件
使用Event.subscribe方法可以对全局产生的事件进行监听，如下：
```lua
Event.subscribe(function(event)
    print("接收到事件：" .. tostring(event))
end)
```
### 监听特定事件
如果你想监听某一特定类型事件，可以按照下面的方法进行。

```lua
Event.subscribe(bot, "FriendMessageEvent", function(event)
    print("接收到事件：" .. tostring(event))
end)
```
在上面的示例中，事件监听器只会收到来自这个bot的好友消息事件。

> 了解更多：[事件（Event）](./event.md)

## 常用操作

### 通过 Bot 对象获取指定好友、群对象

``` lua
local friend = bot:getFriend(123456789) --123456789为指定好友 qq 号
local group = bot:getGroup(123456798) --123456789为指定群 qq 号
```

> 了解更多：[Bot](./bot.md)

### 给好友、群发送纯文本消息

``` lua
local friend = bot:getFriend(123456789)
local group = bot:getGroup(123456798)

friend:sendMessage("你好！")
group:sendMessage("你好！")
```

### 发送复杂消息

``` lua
local friend = bot:getFriend(123456789)
friend:sendMessage("你好！" .. Face(1)) -- 添加一个表情，1为表情代码
```

> 了解更多[消息 (Message)](./message.md)

### 获取好友名称等操作

``` lua
local friend = bot:getFriend(123456789)
local nick = friend.nick
print("好友的昵称为：" .. nick)
```

> 更多操作，请看：[好友 (Friend)](./contact.md) 及 [群 (Group)](./contact.md)

