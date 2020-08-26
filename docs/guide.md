本文档假定读者具有一定的lua开发基础，若你欠缺lua语言基础，请参阅[`RUNOOB:Lua教程`](https://www.runoob.com/lua/lua-tutorial.html)

# 一个lua-mirai脚本的基本执行流程
一般来说，一个lua-mirai脚本有以下的执行流程

1. 获得bot对象
2. 定义监听函数
3. 通过bot对象传入监听函数

下面是一个简单的示例，该示例实现了”复读机“的功能：

```lua
--1.获得bot对象

local bot = Bot(qq账号,"qq密码","C:\\device.json")
bot:login() --登录


--2.定义监听函数

function listener(event)
    --print(event)
    local msg = event.message
    local sender = event.sender
    sender:sendMessage(Quote(msg) .. msg) --引用并回复相同消息
end


--3.通过bot对象传入监听函数

bot:subscribe("FriendMessageEvent",listener)

```

进行事件订阅或发送消息前，你需要获取Bot对象，获取Bot对象有以下方法：

#### 1.Android内

##### 通过Event.onLoad事件传入[`Bot`](/docs/bot.md)对象，示例：

``` lua
Event.onLoad = function (bot)
    print("载入Bot"..bot.id.."成功")
end
```

#### 2.JVM内

##### 使用Bot方法构造[`Bot`](/docs/bot.md)对象，

##### 参数列表：

| 参数       | 类型    | 描述                               | 可空  |
| ---------- | ------- | ---------------------------------- | ----- |
| account    | Integer | 账号                               | False |
| password   | String  | 密码                               | False |
| deviceInfo | String  | 设备信息路径，不填则使用随机信息。 | True  |

##### 示例：

``` lua
local bot = Bot(qq账号,"qq密码","C:\\device.json")
bot.login() --登录
```

# 了解更多

### 下载示例脚本

你可以在这里找到示例脚本： [`下载示例脚本(适用于 Android)`](https://github.com/only52607/lua-/tree/master/demos)

### API列表

点此查看：[`API`](/docs/apis.md)