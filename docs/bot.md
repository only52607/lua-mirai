# 机器人 (Bot)

## 构造方式

#### 1.单脚本运行环境

##### 使用 Bot 方法构造[`Bot`](/docs/bot.md)对象，

##### 参数列表：

| 参数     | 类型         | 描述     | 可空  |
| -------- | ------------ | -------- | ----- |
| account  | Integer      | 账号     | False |
| password | String       | 密码     | False |
| config   | String/Table | 构造信息 | True  |

##### 当config为String时，代表设备信息路径，如文件不存在则自动创建。

##### 当config为Table时，该table代表bot附加构造信息，table可取以下成员

| 参数                      | 类型     | 描述                                                         |
| ------------------------- | -------- | ------------------------------------------------------------ |
| protocol                  | String   | 登录协议，可能的取值如下<br />"ANDROID_PHONE" 安卓手机协议<br />"ANDROID_PAD" 安卓平板协议<br />"ANDROID_WATCH" 安卓手表协议 |
| fileBasedDeviceInfo       | String   | 设备信息路径                                                 |
| heartbeatPeriodMillis     | Integer  | 心跳周期（毫秒，默认为60000）                                |
| heartbeatTimeoutMillis    | Integer  | 每次心跳时等待结果的时间（毫秒，默认为5000）                 |
| firstReconnectDelayMillis | Integer  | 心跳失败后的第一次重连前的等待时间（毫秒，默认为5000）       |
| reconnectPeriodMillis     | Integer  | 重连失败后, 继续尝试的每次等待时间（毫秒，默认为5000）       |
| reconnectionRetryTimes    | Integer  | 最多尝试多少次重连，默认为2147483648次                       |
| noBotLog                  | Boolean  | 值为true时，不输出bot的log信息                               |
| noNetworkLog              | Boolean  | 值为true时，不输出network的log信息                           |
| botLogger                 | Function | bot日志拦截器，接收一个字符串参数。                          |
| networkLogger             | Function | network日志拦截器，接收一个字符串参数。                      |

##### 示例1：

``` lua
local bot = Bot(qq 账号,"qq 密码","device.json")
bot.login() -- 登录
```

##### 示例2：

``` lua
local bot = Bot(qq 账号,"qq 密码",{
        protocol = "ANDROID_WATCH", --使用手表协议登录
        fileBasedDeviceInfo = "device.json", --指定设备信息路径
        noNetworkLog = true --不显示网络日志
    })
bot.login() -- 登录
```



#### 2.多脚本运行环境

##### 通过 onLoad 函数获取，示例

``` lua
function onLoad(bot)
	print(bot.id)
end
```



## 属性

| 属性名 | 类型    | 描述           |
| ------ | ------- | -------------- |
| id     | Integer | 机器人的 qq 号码 |
| selfQQ     | [`Friend`](/docs/friend.md) | bot 自身的 Friend 对象 |
| isOnline     | Boolean | 是否在线 |

## 方法

### getFriend (获取好友)

#### 参数列表：

| 参数 | 类型    | 描述     |
| ---- | ------- | -------- |
| id   | Integer | 好友 qq 号 |

#### 返回值：

| 类型                                  | 描述             |
| ------------------------------------- | ---------------- |
| [`Friend`](/docs/friend.md) | 获取到的好友对象 |

### getFriends (获取好友列表) 耗时操作，慎用

#### 参数列表：无

#### 返回值：

| 类型                                  | 描述             |
| ------------------------------------- | ---------------- |
| Table of [`Friend`](/docs/friend.md) | 获取到的好友对象列表 |


### getGroup (获取群) 

#### 参数列表：

| 参数 | 类型    | 描述 |
| ---- | ------- | ---- |
| id   | Integer | 群号 |

#### 返回值：

| 类型                                | 描述           |
| ----------------------------------- | -------------- |
| [`Group`](/docs/group.md) | 获取到的群对象 |

### getGroups (获取群列表) 耗时操作，慎用

#### 参数列表：无

#### 返回值：

| 类型                                  | 描述             |
| ------------------------------------- | ---------------- |
| Table of [`Group`](/docs/group.md) | 获取到的群对象列表 |


### containsFriend (判断好友是否存在)

#### 参数列表：

| 参数 | 类型    | 描述     |
| ---- | ------- | -------- |
| id   | Integer | 好友 qq 号 |

#### 返回值：

| 类型    | 描述     |
| ------- | -------- |
| Boolean | 是否存在 |

### containsGroup (判断群是否存在)

#### 参数列表：

| 参数 | 类型    | 描述 |
| ---- | ------- | ---- |
| id   | Integer | 群号 |

#### 返回值：

| 类型    | 描述     |
| ------- | -------- |
| Boolean | 是否存在 |



### subscribe (订阅事件)

##### 详细用法见[`事件`](/docs/events.md)

#### 参数列表：

| 参数     | 类型     | 描述     |
| -------- | -------- | -------- |
| event    | String   | 事件标识 |
| callback | Function | 回调函数 |

### launch(立即启动一个线程)

#### 参数列表：

| 参数  | 类型     | 描述       |
| ----- | -------- | ---------- |
| block | Function | 线程主函数 |


### close (退出该 bot)

### 参数列表：

无