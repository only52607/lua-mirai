本教程适用于具有一定的 lua 开发基础，想要通过lua语言快速开发一个机器人的读者。若您欠缺 lua 语言基础，请参阅[`RUNOOB:Lua 教程`](https://www.runoob.com/lua/lua-tutorial.html)

# 约定

### api设计

lua-mirai的api基于面向对象风格进行设计，若您之前习惯于面向过程开发模式，那么您也许需要花些时间对该风格进行适应。举个例子，假设您需要使用id为123456的账号向id为987654的账号发送"Hello"的消息，那么在面向过程的设计中，您的代码可能是下面这样的：

```lua
send_message(123456,987654,"Hello")
```

而在lua-mirai中，它看起来是下面这样的：

```lua
local friend = bot:getFriend(987654) -- 首先获取friend对象
friend:sendMessage("Hello") --利用friend对象发送消息
```

### 命名风格

lua-mirai的api系统采用与java语言一致的命名风格（驼峰式命名法），对于类、table的命名采用大驼峰式命名，如`Http`、`Json`等，对于变量、方法或成员则采用小驼峰式命名，如`sendMessage`、`getFriend`等，且一般情况下，单词不使用缩写形式。

对于对象（userdata）的无参成员，一般使用名词命名，如`friend.name`、`friend.nick`，而不是`friend:getName`、`friend:getNick`，对于含参成员或方法，则采用**动词或动词+名词**的形式，如`bot:getFriend`，`friend:sendMessage`。

### 使用冒号“:”调用对象（userdata）方法

基于lua中并没有真正的面向对象设计，对于对象的方法的调用是在参数列表开头增加一个代表对象自身的参数实现的，当使用冒号":"的方式调用，lua会自动将对象作为第一个参数传入，如像friend对象发送消息，你需要写出下面这样的代码：

```lua
friend:sendMessage("Hello")
```

若您坚持不适用冒号调用，那么您的代码可能是下面这样的

```lua
friend.sendMessage(friend,"Hello")
```

### 基于lua5.2版本

lua-mirai内部基于lua5.2版本，如果您使用了lua5.3中的部分特性，如位运算符，那么您可能需要更换为其他写法。同时，代码中的关键词支持unicode命名，不支持导入c语言编写的扩展库。

<br />

# 一切从Bot对象开始

[`Bot`](/docs/bot.md)对象是lua-mirai脚本的基础，它代表了机器人自身，friend对象或group对象均由其产生，进行事件订阅或发送消息前，你需要获取 Bot 对象，获取 Bot 对象有以下方法：

#### 1.多脚本环境（Web端管理）

##### 通过 onLoad 事件传入[`Bot`](/docs/bot.md)对象，示例：

``` lua
function onLoad(bot)
    print("载入 Bot"..bot.id.."成功")
end
```

这样，当后台新增加一个 bot 时，便会通过 onLoad 事件通知你的脚本。

#### 2.单脚本环境

##### 使用 Bot 方法构造[`Bot`](/docs/bot.md)对象，示例：

``` lua
local bot = Bot(qq 账号,"qq 密码","device.json")
bot:login() -- 登录
```

由于手动构造的 bot 不会自动登录，需要手动调用 login 函数进行登录。

<br />

# 通过 Bot 对象获取指定好友、群对象

``` lua
local friend = bot:getFriend(123456789) --123456789为指定好友 qq 号
local group = bot:getGroup(123456798) --123456789为指定群 qq 号
```

了解更多：[`Bot`](/docs/bot.md)

<br />

# 给好友、群发送纯文本消息

``` lua
local friend = bot:getFriend(123456789)
local group = bot:getGroup(123456798)

friend:sendMessage("你好！")
group:sendMessage("你好！")
```

<br />

# 发送复杂消息

``` lua
local friend = bot:getFriend(123456789)
friend:sendMessage("你好！" .. Face(1)) -- 添加一个表情，1为表情代码
```

了解更多消息结构请看：[`消息（Message）`](/docs/message.md)

<br />

# 获取好友名称等操作

``` lua
local friend = bot:getFriend(123456789)
local nick = friend.nick
print("好友的昵称为：" .. nick)
```

更多操作，请看：[好友 (Friend)](/docs/friend.md)  及 [`群 (Group)`](/docs/group.md)

<br />

# 订阅（监听）消息事件

```lua
function listener(event)
    local msg = event.message
    local sender = event.sender
    print("收到来自好友（" .. sender.nick .. "）的消息：" .. msg)
end


bot:subscribe("FriendMessageEvent",listener) -- 监听好友消息
```

<br />

# 订阅其他事件

[`事件(Events)`](/docs/events.md)

<br />

# 一个 lua-mirai 脚本的基本执行流程

一般来说，一个 lua-mirai 脚本有以下的执行流程

1. 获得 bot 对象
2. 定义监听函数
3. 通过 bot 对象传入监听函数

下面是一个简单的示例，该示例实现了”复读机“的功能：

```lua
--1.获得 bot 对象

local bot = Bot(qq 账号,"qq 密码","C:\\device.json")
bot:login() -- 登录


--2.定义监听函数

function listener(event)
    --print(event)
    local msg = event.message
    local sender = event.sender
    sender:sendMessage(Quote(msg) .. msg) -- 引用并回复相同消息
end


--3.通过 bot 对象传入监听函数

bot:subscribe("FriendMessageEvent",listener)
```

# 了解更多

### 下载示例脚本

你可以在这里找到示例脚本： [`下载示例脚本(适用于 Android)`](https://github.com/only52607/lua-mirai/tree/master/demos)

### API 列表

点此查看：[`API`](/docs/apis.md)