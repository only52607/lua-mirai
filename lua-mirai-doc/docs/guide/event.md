# 事件（Event）

## 介绍
lua-mirai 基于事件驱动机制构建，lua-mirai 中的事件包括但不限于好友、群消息事件等，当事件发生时，`LM`将会通过回调函数通知监听者。

> [事件(Event)参考](../reference/event.md)中包含了`LM`所有可用的消息类型

## Event表
`Event`是一个全局变量，它包含了所有有关于事件的操作，包括`subscribe`方法等。

## 事件监听

### 启动监听
`Event.subscribe`方法是启动事件监听的入口方法，它的基本形式如下

```lua
Event.subscribe([<过滤条件>..],(<监听器>))
```

#### 过滤条件
过滤条件是一个可选参数，它可以传入多个，它的取值有下面几种情况，
1. `Bot`或`Contact`对象：
当过滤条件是一个bot的时候，监听器只会收到该bot或contact下的事件。
2. string字符串：
当过滤条件是一个string的时候，该string标识了一个事件，如`FriendMessageEvent`，这意味着监听器只会收到该类型的事件。
3. 自定义function：
当过滤条件是一个function的时候，该function会接收一个`event`对象，当函数返回一个非`nil`值时，则代表该事件是可接收的。

#### 监听器
监听器是一个lua函数，当事件发生时，该函数会接收到一个`event`类型的对象，该对象包含了机器人对象，发送者，消息等信息。

> 在不同的事件类型中，event对象中包含的成员不同，如需查看当前event对象的所有成员，可通过读取元属性实现，具体请参阅：[lua语言扩展](../reference/libs/luaex.md)

#### 监听示例

##### 监听全部事件
当过滤条件个数为零时，该监听器会接收到所有bot产生的所有事件，如下：
```lua
Event.subscribe(function (event)
    print("接收到事件：" .. tostring(event))
end)
```
##### 监听特定事件

下面的示例中，事件监听器会收到来自这个bot所有事件。

```lua
Event.subscribe(bot,function (event)
    print("接收到事件：" .. tostring(event))
end)
```

同理，在下面的示例中，事件监听器只会收到来自这个bot的好友消息事件。

```lua
Event.subscribe(bot,"FriendMessageEvent",function (event)
    print("接收到事件：" .. tostring(event))
end)
```

> 为了使用上的方便，`Event`对象被添加到`Bot`以及`Contact`对象的`metatable`中，因此，上述代码等价于：
> ```lua
> bot:subscribe("FriendMessageEvent",function (event)
>     print("接收到事件：" .. tostring(event))
> end)
> ```

### 判断事件类型
对event对象使用type方法可返回一个消息的具体类型，例如：
```lua
bot:subscribe("FriendMessageEvent",function (event)
    print(type(event))
end)
```

> [事件(Event)参考](../reference/event.md)中包含了`LM`所有可用的消息类型

### 调用事件方法
事件对象是一个`userdata`类型的对象，内置了部分成员方法。
> 参阅：[lua语言扩展](../reference/libs/luaex.md)

### 取消监听
调用`subscribe` 方法后会得到一个 `CompeletableJob` 对象，调用此对象的 `cancel` 方法即可取消此监听，如下：
``` lua
function listener(event)
    print(event)
end
local job = bot:subscribe("FriendMessageEvent",listener)
job:cancel()
```