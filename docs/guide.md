本文档假定读者具有一定的lua开发基础，若你欠缺lua语言基础，请参阅[`RUNOOB:Lua教程`](https://www.runoob.com/lua/lua-tutorial.html)

# 下载示例脚本
你可以在这里找到示例脚本： [`下载示例脚本(适用于Mirai Android)`](https://github.com/only52607/lua-mirai/tree/master/demos)

# 开始
进行事件订阅或发送消息前，你需要获取MiraiBot对象，获取MiraiBot对象有以下方法：

#### 1.MiraiAndroid内

##### 通过Event.onLoad事件传入[`MiraiBot`](/docs/miraibot.md)对象，示例：

``` lua
Event.onLoad = function (bot)
    print("载入Bot"..bot.id.."成功")
end
```

#### 2.JVM内

##### 使用Bot方法构造[`MiraiBot`](/docs/miraibot.md)对象，

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

--在这里对bot进行事件订阅操作
--如 bot.subsribeXXX()

bot.join() --挂起，防止进程结束

```

# 内置对象

## 消息（MiraiMsg）

在lua-mirai中，会话消息的构造和解析是由MiraiMsg对象完成的，详情见 [`MiraiMsg`](/docs/miraimsg.md)。

## 消息源（MiraiSource）

消息源对象是由消息对象创建的一个引用，可用于撤回，引用回复。
可通过sendMsg方法或msg:getSource()获取。

示例：
``` lua
--发送消息后立即撤回
local source = sender.sendMsg("看不见我")
source:recall() 
```

## 机器人 (MiraiBot)

详情见 [`MiraiBot`](/docs/miraibot.md)。

## 好友 (MiraiFriend)

详情见 [`MiraiFriend`](/docs/miraifriend.md)。

## 群 (MiraiGroup)

详情见 [`MiraiGroup`](/docs/miraigroup.md)。

## 群成员 (MiraiGroupMember)

详情见 [`MiraiGroupMember`](/docs/miraigroupmember.md)。

# 内置支持库列表

## Http支持库

详情见 [`Http支持库`](/docs/http.md)。

## Json支持库

详情见 [`json支持库`](/docs/json.md)。

## LuaJava支持库

详情见 [`LuaJava支持库`](/docs/luajava.md)。

# 增加的lua语法特性(基于lua5.2.x)

### 中文命名支持，示例：
``` lua
    function 相加(a,b)
        return a+b
    end
    function 输出(...)
        print(...)
    end
    输出(相加(1,1))
```