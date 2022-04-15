# 消息（Meesage）

> [消息(Message)参考](../reference/message.md)中包含了当前`LM`所有可用的消息类型。

## 消息与消息元素

### 介绍

`消息`是`Contact`对象进行交流的实体，基于消息的复杂性，lua-mirai中的`消息`通常由不同的`消息元素`组合而成。

> 如一个图片与文本消息，一般会由三个不同类型的`消息元素`构成。分别是`Source`，`Image` 和 `PlainText`。

### 消息元素构造

1. 字符串的形式

在`LM`中，一个字符串可代表一个纯文本类型的消息元素。如"Hello"代表一个纯文本消息元素。

> "Hello" 等同于 PlainText("Hello")

2. 消息元素构造函数

`LM`提供了丰富的消息元素构造函数，例如

```lua
Face(1) -- 构造一个表情
AtAll() -- 构造"@全体成员" 
```

> [消息(Message)参考](../reference/message.md)中包含了当前`LM`所有可用的消息构造函数。

### 消息元素组合

将多个消息元素拼接可构成一个`消息`，而`消息`之间同样也可以进行拼接，构成新的`消息`。

#### 组合方式

在`LM`中，可以使用lua拼接操作符 `..` 或 `+` 进行拼接，下面是一个简单的示例：

``` lua
ImageUrl("http://xxxxx", sender) .. Face(1) .. "hello"
```


## 消息解析

### 消息元素遍历
消息实现了lua中的表的所有方法，因此可以使用lua中处理表的方式处理组合消息，示例：
``` lua
Event.subscribe(function(event)
    if (type(event)=="FriendMessageEvent") then
        for i,v in ipairs(event.message) do -- 遍历所有消息元素
            print(tostring(v))
        end
    end
end)
```
> 当你对message对象使用tostring或print方法输出时，消息会被采用一种`mirai码`的编码方式以字符串的形式表示，如`[mirai:image:xxxxxx]`。
> 同样地，也可以借助`Code`构造函数以使用`mirai码`构造消息，如`Code("[mirai:image:xxxxxx]")`

### 获取消息中包含消息元素的个数
输出个数
``` lua
print("length:" .. tostring(#event.message))
```

### 获取某个位置的消息元素
获取位置1处的消息元素：
``` lua
print(message[1])
```

### 判断消息元素类型
遍历所有消息元素并输出类型
``` lua
for i,v in ipairs(event.message) do
    print(type(v))
end
```

### 检索指定类型的消息元素
获取类型为`At`的消息元素
``` lua
print(event.message["At"])
```