# 消息对象（MiraiMsg）

详情见 [`消息对象`](/docs/miraimsg.md)

## 消息构造

"任意内容" : 构造一个纯文本消息

Msg() : 构造一个空消息

Msg("任意内容") : 构造一个纯文本消息

Quote(消息对象) : 构造一个引用回复

At(群成员) : 构造一个At消息

AtAll() : 构造一个At全体消息

Image( 图片URL ,群或好友 ) : 构造一个图片

Face(表情代码) : 构造一个表情

所有消息类型见 [`消息类型`](/docs/miraimsg.md)

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

# 