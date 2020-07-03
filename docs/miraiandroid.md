本文档假定读者具有一定的lua开发基础，若你欠缺lua语言基础，请参阅[`RUNOOB:Lua教程`](https://www.runoob.com/lua/lua-tutorial.html)

# 下载示例脚本
你可以在这里找到示例脚本： [`下载示例脚本`](https://github.com/only52607/lua-mirai/tree/master/demos)

# 开始
## 【MiraiAndroid内使用】 Event.onLoad 
当脚本被载入后，Event.onLoad函数被调用并传入MiraiBot对象，你可以在此函数内进行一些初始化操作，如监听消息事件。
``` lua
Event.onLoad = function (bot)
    print("载入Bot"..bot.id.."成功")
end
```

## 【MiraiAndroid内使用】 Event.onFinish
当脚本被卸载前，Event.onFinish将被调用，你可以在此函数内保存数据。
``` lua
Event.onFinish = function ()
    print("脚本被卸载")
end
```

## 【单独运行】 Bot对象创建

``` lua
local bot = Bot(qq账号,"qq密码") --获得bot对象
bot.login() --登录

--在这里对bot进行事件订阅操作
--如 bot.subsribeXXX()

bot.join() --挂起，防止进程结束

```

# 事件
## 事件监听

一个事件监听是由MiraiBot对象创建的，基本格式为bot:subscribeXXXXX(function () end)

## 事件拦截

事件被拦截意味着消息只被当前脚本接收，事件监听器默认不会拦截事件，若需要拦截事件，请在监听器内返回非空值。
``` lua
bot:subscribeFriendMsg(
    function (bot, msg, sender)
        return true --事件被拦截，此脚本以后的所有脚本将不会收到该事件
    end
)
```

## 好友消息事件
示例：
``` lua
bot:subsribeFriendMsg(
    function (bot, msg, sender)
    end
)
```
监听器含有三个参数，分别是

bot:收到消息的机器人对象
    
msg:接收到的消息对象
    
sender:发送信息的好友对象
    

## 群消息事件
``` lua
bot:subsribeGroupMsg(
    function (bot, msg, group, sender)
    end
)
```
监听器含有四个参数，分别是

bot:收到消息的机器人对象
    
msg:接收到的消息对象

group:发送信息的群对象

sender:发送信息的群成员对象

## 其他事件
见 [`事件类型`](/docs/events.md)


# 消息对象（MiraiMsg）
## 消息构造
"任意内容" : 构造一个纯文本消息

Msg() : 构造一个空消息

Msg("任意内容") : 构造一个纯文本消息

Quote(消息对象) : 构造一个引用回复

At(群成员) : 构造一个At消息

AtAll() : 构造一个At全体消息

Image( 图片URL ,群或好友 ) : 构造一个图片

Face(表情代码) : 构造一个表情

所有消息类型见 [`消息类型`](/docs/msgtypes.md)

## 消息拼接
使用appendXXX 或 + 或 .. 进行拼接，下面是一个简单的示例：

``` lua
Msg("hello"):appendText("world") + "lua" .. Msg():appendImage("http://xxxxx",sender) .. Face(1)
```

## 消息处理
msg对象支持使用lua的所有标准字符串处理函数

如寻找消息中的文本可以使用以下方式：

``` lua
msg:find("pattern")
```

以上代码等同于 

``` lua
string.find(msg,"pattern")
```

所有字符串处理函数见 [`lua字符串处理`](https://www.runoob.com/lua/lua-strings.html)

## 消息撤回
``` lua
msg:recall()
--或 bot:recall(msg)
```

# 消息源对象（MiraiSource）
消息源对象是由消息对象创建的一个引用，可用于撤回，引用回复。
可通过sendMsg方法或msg:getSource()获取。

示例：
``` lua
--发送消息后立即撤回
local source = sender.sendMsg("看不见我")
source:recall() 
```

# 机器人对象 (MiraiBot)
## 属性
|  属性名   | 类型  | 描述  |
|  ----  | ----  | ----  |
| id  | Integer | 机器人的qq号码  |

## 方法

### getFriend (获取好友)

#### 参数列表：

|  参数   | 类型  | 描述  |
|  ----  | ----  | ----  |
| id  | Integer | 好友qq号  |

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| MiraiFriend | 获取到的好友对象  |


### getGroup (获取群)

#### 参数列表：

|  参数   | 类型  | 描述  |
|  ----  | ----  | ----  |
| id  | Integer | 群号  |

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| MiraiGroup | 获取到的群对象  |

### getSelfQQ (将MiraiBot对象转为MiraiFriend对象)

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| MiraiFriend | 转换后的对象  |

### getId (获取机器人的qq号码)

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| Integer | qq号  |

### addFriend (将MiraiBot对象转为MiraiFriend对象)

#### 参数列表：

|  参数   | 类型  | 描述  |
|  ----  | ----  | ----  |
| id  | Integer | 好友qq号  |

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| MiraiFriend | 获取到的好友对象  |

### containsFriend (判断好友是否存在)

#### 参数列表：

|  参数   | 类型  | 描述  |
|  ----  | ----  | ----  |
| id  | Integer | 好友qq号  |

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| Boolean | 是否存在  |

### containsGroup (判断群是否存在)

#### 参数列表：

|  参数   | 类型  | 描述  |
|  ----  | ----  | ----  |
| id  | Integer | 群号  |

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| Boolean | 是否存在  |

### isActive (是否在线)

#### 参数列表：

|  参数   | 类型  | 描述  |
|  ----  | ----  | ----  |
| id  | Integer | 好友qq号  |

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| MiraiFriend | 获取到的好友对象  |

### subscribeFriendMsg (订阅好友消息)

#### 参数列表：

|  参数   | 类型  | 描述  |
|  ----  | ----  | ----  |
| callback  | Function | 回调函数  |


### subscribeGroupMsg (订阅群消息)

#### 参数列表：

|  参数   | 类型  | 描述  |
|  ----  | ----  | ----  |
| callback  | Function | 回调函数  |


# 好友对象 (MiraiFriend)

## 属性

|  属性名   | 类型  | 描述  |
|  ----  | ----  | ----  |
| id  | Integer | 好友的qq号码  |
| bot  | MiraiBot | bot对象  |

## 方法

### getNick (获取昵称)

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| String | 昵称  |


### sendMsg (发送消息)

#### 参数列表：

|  参数   | 类型  | 描述  |
|  ----  | ----  | ----  |
| msg  | MiraiMsg | 消息对象  |


### getAvatarUrl (获取头像地址)

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| String | 获取到的头像地址  |


### isActive (是否在线)

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| Boolean | 是否在线  |


# 群对象 (MiraiGroup)

## 属性

|  属性名   | 类型  | 描述  |
|  ----  | ----  | ----  |
| id  | Integer | 群号码  |
| bot  | MiraiBot | bot对象  |

## 方法

### sendMsg (发送消息)

#### 参数列表：

|  参数   | 类型  | 描述  |
|  ----  | ----  | ----  |
| msg  | MiraiMsg | 消息对象  |


### getMember (获取群成员)

#### 参数列表：

|  参数   | 类型  | 描述  |
|  ----  | ----  | ----  |
| id  | Integer | 群成员qq号  |

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| MiraiGroupMember | 获取到的群成员对象  |

### getAvatarUrl (获取群头像地址)

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| String | 群头像地址  |

### getBotMuteRemain (获取机器人被禁言剩余时间)

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| Integer | 剩余时间  |


### getBotAsMember (将当前机器人转换为群成员对象)

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| MiraiGroupMember | 转换后的群成员对象  |


### getBotPermission (获取当前机器人在本群的权限)

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| String | 权限名  |


### getName (获取群名)

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| String | 群名  |


### getOwner (获取群主)

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| MiraiGroupMember | 群主对象  |

### contains (判断是否包含群成员)

#### 参数列表：

|  参数   | 类型  | 描述  |
|  ----  | ----  | ----  |
| id  | Integer | 群成员qq号  |

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| MiraiGroupMember | 群主对象  |

### getMemberOrNull (获取群成员对象，失败返回Nil)

#### 参数列表：

|  参数   | 类型  | 描述  |
|  ----  | ----  | ----  |
| id  | Integer | 群成员qq号  |

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| MiraiGroupMember | 群成员对象  |


### quit (退出群)


# 群成员对象 (MiraiGroupMember)

## 属性

|  属性名   | 类型  | 描述  |
|  ----  | ----  | ----  |
| id  | Integer | 群员的qq号码  |
| bot  | MiraiBot | bot对象  |
| group  | MiraiGroup | 群员所在群  |

## 方法

### getNick (获取昵称)

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| String | 昵称  |

### getNameCard (获取群名片)

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| String | 群名片  |


### getMuteRemain (获取被禁言剩余时间)

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| Integer | 剩余时间  |


### getSpecialTitle (获取群头衔)

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| String | 群头衔  |

### isMuted (判断是否被禁言)

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| Boolean | 是否被禁言  |


### isAdministrator (判断是否为管理员)

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| Boolean | 是否为管理员  |


### isOwner (判断是否为群主)

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| Boolean | 是否为群主  |

### mute (禁言)

#### 参数列表：

|  参数   | 类型  | 描述  |
|  ----  | ----  | ----  |
| id  | Integer | 禁言时间  |

### unMute (解除禁言)

### kick (移除群聊)

### isFriend (判断是否为好友)

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| Boolean | 是否为好友  |


### asFriend (转为MiraiFriend对象)

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| MiraiFriend | 转换后的MiraiFriend对象  |


### getPermission (获取群员权限)

#### 返回值：

| 类型 | 描述  |
| ----  | ----  |
| String | 权限名  |

### sendMsg (发送私聊消息)

#### 参数列表：

|  参数   | 类型  | 描述  |
|  ----  | ----  | ----  |
| msg  | MiraiMsg | 消息对象  |

# 网络请求

## get请求
Net.get(请求url, 附加参数)

## post请求
Net.get(请求url, 提交参数 , 附加参数)

# 与Java类交互

## 在lua中调用java类

### 使用import命令导入java类
#### 示例：
``` lua
    import "java.lang.Thread" 
```

### 使用java类静态成员和静态方法
``` lua
    类名.静态成员名或方法名(参数)
```
#### 示例
``` lua
    import "java.lang.String"
    print(String.join("-","lua","mirai"))
```

### 创建java类对象，并调用方法
``` lua
    对象=类名(构造参数)
    对象:方法名(参数)
```
    
#### 示例：
``` lua
    import "java.lang.String"
    instance = String("lua-mirai")
    print(instance:toUpperCase())
```

### 实现java类方法，并生成对象
``` lua
对象 = 类名{
    方法名=function (参数)
    end
}
```
    
#### 示例：
``` lua
--实现Runnable接口并创建线程
import "java.lang.Thread"
import "java.lang.Runnable"
Thread(
    Runnable{
        run = function() print "Hello World!" end
    }
):start()
```

其他用法请参阅 [`CSDN:在Lua中操作Java对象`](https://blog.csdn.net/lgj123xj/article/details/81677036)

## 增加的Lua语法特性(基于lua5.2.x)

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