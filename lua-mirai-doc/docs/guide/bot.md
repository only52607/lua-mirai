# 机器人 (Bot)

> [Bot参考](../reference/bot.md)中包含了Bot对象的所有属性和方法。

## 获取Bot对象

[Bot](../reference/bot.md)对象代表了机器人自身，通常获取 Bot 对象有以下方法：

### 单独构造

#### 使用 Bot 方法构造[Bot](../reference/bot.md)对象

##### 示例：

``` lua
local bot = Bot(qq 账号, "qq 密码", "device.json")
bot:login() -- 登录
```

由于手动构造的 bot 不会自动登录，需要手动调用 login 函数进行登录。

##### 参数列表：

| 参数     | 类型         | 描述     | 可空  |
| -------- | ------------ | -------- | ----- |
| account  | number          | 账号     | False |
| password | string       | 密码     | False |
| config   | string/table | 构造信息 | True  |

当 config 为 string 时，代表设备信息路径，如文件不存在则自动创建。

当 config 为 table 时，该table代表bot附加构造信息，table可取以下成员

| 参数                      | 类型     | 描述                                                         |
| ------------------------- | -------- | ------------------------------------------------------------ |
| protocol                  | string   | 登录协议，可能的取值如下<br/>"ANDROID_PHONE" 安卓手机协议<br/>"ANDROID_PAD" 安卓平板协议<br/>"ANDROID_WATCH" 安卓手表协议 |
| fileBasedDeviceInfo       | string   | 设备信息路径                                                 |
| heartbeatPeriodMillis     | number      | 心跳周期（毫秒，默认为60000）                                |
| heartbeatTimeoutMillis    | number      | 每次心跳时等待结果的时间（毫秒，默认为5000）                 |
| firstReconnectDelayMillis | number      | 心跳失败后的第一次重连前的等待时间（毫秒，默认为5000）       |
| reconnectPeriodMillis     | number      | 重连失败后, 继续尝试的每次等待时间（毫秒，默认为5000）       |
| reconnectionRetryTimes    | number      | 最多尝试多少次重连，默认为2147483648次                       |
| noBotLog                  | Boolean  | 值为true时，不输出bot的log信息                               |
| noNetworkLog              | Boolean  | 值为true时，不输出network的log信息                           |
| botLogger                 | Function | bot日志拦截器，接收一个字符串参数。                          |
| networkLogger             | Function | network日志拦截器，接收一个字符串参数。                      |

示例1：
``` lua
local bot = Bot(qq 账号,"qq 密码","device.json")
bot:login() -- 登录
```
示例2：
``` lua
local bot = Bot(qq 账号, "qq 密码", {
        protocol = "ANDROID_WATCH", --使用手表协议登录
        fileBasedDeviceInfo = "device.json", --指定设备信息路径
        noNetworkLog = true --不显示网络日志
    })
bot:login() -- 登录
```

### 获取已创建的bot对象（适合作为插件情况下使用）
对于上述使用Bot函数创建的对象，Bot对象创建完毕后，该对象会保存入名为`Bots`的全局表中

##### 示例：

``` lua
for id, bot in pairs(Bots) do --遍历所有的bot对象
    prnumber(id .. ":" .. tostring(bot))
end
```


