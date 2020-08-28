本文档假定读者具有一定的 lua 开发基础，若你欠缺 lua 语言基础，请参阅[`RUNOOB:Lua 教程`](https://www.runoob.com/lua/lua-tutorial.html)

# Bot 对象获取

进行事件订阅或发送消息前，你需要获取 Bot 对象，获取 Bot 对象有以下方法：

#### 1.多脚本环境

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
bot.login() -- 登录
```

由于手动构造的 bot 不会自动登录，需要手动调用 login 函数进行登录。



# 通过 Bot 对象获取指定好友、群对象

``` lua
local friend = bot.getFriend(123456789) --123456789为指定好友 qq 号
local group = bot.getGroup(123456798) --123456789为指定群 qq 号
```

了解更多：[`Bot`](/docs/bot.md)

# 给好友、群发送纯文本消息

``` lua
local friend = bot.getFriend(123456789)
local group = bot.getGroup(123456798)

friend:sendMessage("你好！")
group:sendMessage("你好！")
```

# 发送复杂消息

``` lua
local friend = bot.getFriend(123456789)
friend:sendMessage("你好！" .. Face(1)) -- 添加一个表情，1为表情代码
```



了解更多消息结构请看：[`消息（Message）`](/docs/message.md)

# 获取好友名称等操作

``` lua
local friend = bot.getFriend(123456789)
local nick = friend.nick
print("好友的昵称为：" .. nick)
```

更多操作，请看：[好友 (Friend)](/docs/friend.md)  及 [`群 (Group)`](/docs/group.md)

# 订阅（监听）消息事件

```lua
function listener(event)
    local msg = event.message
    local sender = event.sender
    print("收到来自好友（" .. sender.nick .. "）的消息：" .. msg)
end


bot:subscribe("FriendMessageEvent",listener) -- 监听好友消息
```



# 订阅其他事件

[`事件(Events)`](/docs/events.md)



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

你可以在这里找到示例脚本： [`下载示例脚本(适用于 Android)`](https://github.com/only52607/lua-/tree/master/demos)

### API 列表

点此查看：[`API`](/docs/apis.md)