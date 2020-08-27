# 消息对象（Message）

## 构造方式

#### ~~空消息~~

##### ~~方法名：Message~~

##### ~~参数：无~~

##### ~~示例：~~

```lua
Message()
```

~~<br />~~

~~<br />~~

#### 纯文本

##### 1.直接使用字符串：

##### 示例：

```lua
"消息"
```

<br />

##### 2.Text方法：

##### 方法名：Text

##### 参数：

| 参数名  | 类型   | 说明           |
| ------- | ------ | -------------- |
| content | String | 纯文本消息内容 |

示例：

```lua
Text("消息内容")
```

<br />

<br />

#### **引用回复**

##### 方法名：Quote

##### 参数：

| 参数名 | 类型                          | 说明               |
| ------ | ----------------------------- | ------------------ |
| source | [`Message`](/docs/message.md) | 必须是接收到的消息 |

示例：

```lua
Quote(message)
```

<br />

<br />

#### At

##### 方法名：At

##### 参数：

| 参数名 | 类型                                                      | 说明       |
| ------ | --------------------------------------------------------- | ---------- |
| target | [`Member`](/docs/member.md) / [`Friend`](/docs/friend.md) | 被At的对象 |

示例：

```lua
At(sender)
```

<br />

<br />

#### At全体

##### 方法名：AtAll

##### 参数：无

##### 示例：

```lua
AtAll()
```

<br />

<br />

#### 图片

**1.通过ID构建：**

##### 方法名：Image

##### 参数：

| 参数名 | 类型   | 说明                                                         |
| ------ | ------ | ------------------------------------------------------------ |
| uuid   | 图片ID | 图片id，格式形如 “/XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX”<br />注：好友消息和群消息具有不同格式的图片id |

示例：

```lua
Image("/XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX")
```

<br />

**2.通过url构建：** 

##### 方法名：ImageUrl

##### 参数：

| 参数名 | 类型                                                         | 说明              |
| ------ | ------------------------------------------------------------ | ----------------- |
| url    | String                                                       | 完整的图片url地址 |
| target | [`Group`](/docs/group.md) / [`Friend`](/docs/friend.md) | 将接受消息的对象  |

示例：

```lua
ImageUrl("http://xxxx/xxx.jpg",group) --网络url

ImageUrl("file:/xxxxx/xxx.jpg",friend) --本地url
```

<br />

**3.通过本地路径构建：** 

##### 方法名：ImageFile

##### 参数：

| 参数名 | 类型                                                         | 说明                       |
| ------ | ------------------------------------------------------------ | -------------------------- |
| path   | String                                                       | 图片本地绝对路径或相对路径 |
| target | [`Group`](/docs/group.md) / [`Friend`](/docs/friend.md) | 将接受消息的对象           |

示例：

```lua
ImageFile("xxx.jpg",group)
```

<br />

<br />

#### 闪照

需要先构造普通图片

##### 方法名：FlashImage

##### 参数：

| 参数名 | 类型                            | 说明         |
| ------ | ------------------------------- | ------------ |
| image  | [`Message`](/docs/message.md) | 普通图片消息 |

示例：

```lua
FlashImage(ImageUrl("http://xxxx/xxx.jpg",group)) -- 使用url构造图片，并转换为闪照
```

<br />

<br />

#### 表情

##### 方法名：Face

##### 参数：

| 参数名 | 类型    | 说明     |
| ------ | ------- | -------- |
| code   | Integer | 表情代码 |

示例：

```lua
Face(1)
```

<br />

<br />

#### Poke消息

戳一戳等特殊消息

##### 方法名：Poke

##### 参数：

| 参数名 | 类型    | 说明     |
| ------ | ------- | -------- |
| code   | Integer | poke代码 |

##### 可用代码：标*的为vip专属消息

> ```
> 0 -> 戳一戳
> 1 -> 比心
> 2 -> 点赞
> 3 -> 心碎
> 4 -> 666
> 5 -> 放大招
> 6 -> 宝贝球*
> 7 -> 召唤术*
> 8 -> 让你皮*
> 9 -> 结印*
> 10-> 手雷*
> 11-> 勾引
> 12-> 抓一下*
> 13-> 碎屏*
> 14-> 敲门*
> 15-> 玫瑰花*
> ```

示例：

```lua
Poke(0)
```

<br />

<br />

#### 转发消息 （测试版）

##### 方法名：Forward

##### 参数：

| 参数名 | 类型  | 说明         |
| ------ | ----- | ------------ |
| info   | Table | 转发消息描述 |

下面这个示例描述了转发消息的参数：

```lua
Forward { --可省略括号
	title = "群聊的聊天记录",
    brief = "[聊天记录]",
    source = "聊天记录",
    preview = { 
        "消息概览1",
        "消息概览2"
    }, --可省略
    summary = "查看 1 条转发消息" --可省略,
    content = {
        {
            senderId = 123456789, --发送者qq号
            time = 987654, --发送的时间戳
            senderName = "发送者昵称1",
            message = Message("消息1") .. Face(0) --显示的消息内容
        },
        {
            senderId = 123456789, 
            time = 987654,
            senderName = "发送者昵称2",
            message = Message("消息2") .. Face(0) --显示的消息内容
        },
    }
}

```

<br />

<br />

#### App分享（测试版）

小程序，如音乐分享。

大部分 JSON 消息为此类型，另外一部分为 Service消息。

##### 方法名：App

##### 参数：

| 参数名  | 类型   | 说明                          |
| ------- | ------ | ----------------------------- |
| content | String | 消息内容，一般为一段JSON 文本 |

示例（完整示例请见：[音乐卡片](../demos/音乐卡片.lua)、解析可见：[jsonmessage.md](./jsonmessage.md)）：
![音乐卡片示例](./res/musicCardSample.png)
```lua
formAMusicShare = function (title, srcUrl, desc, preview, jmpUrl, tag)
local __format__ = [[
{"app":"com.tencent.structmessage","config":{"autosize":true,"forward":true,"type":"normal"},
"desc":"音乐","meta":{"music":{"desc":"%s","jumpUrl":"%s","musicUrl":"%s","preview":"%s",
"tag":"%s","title":"%s"}},"prompt":"[%s]%s","view":"music"}]]
    return string.format (__format__:gsub("\n", ""),
        desc, jmpUrl, srcUrl, preview, tag, title, tag, title)
end
App ("天外来物", "http://music.163.com/song/media/outer/url?id=1463165983", "薛之谦",
    "http://p4.music.126.net/HvB44MNINoLar8HFbRjIGQ==/109951165142435842.jpg", "QQbot 音乐")
```
如果想制作别的 App 分享，就像是 bilibili 那种大张图片的，可以通过自己发送这样一个消息，然后截获 bot 获得的消息，分析里面的 json 构成，然后可以删减一些不必要的东西，然后用 `string.format()` 构造一个 json 文本，填入 `App()` 并发送

<br />

<br />

#### Service消息（测试版）

服务消息，可以是 JSON 消息或  [`XML消息`](/docs/xmlmessage.md)。

 JSON 消息更多情况下通过 App 发送。

##### 方法名：Service

##### 参数：

| 参数名     | 类型    | 说明                                                         |
| ---------- | ------- | ------------------------------------------------------------ |
| service_id | Integer | 消息类型，目前未知, XML 一般为 60, JSON 一般为 1             |
| content    | String  | 消息内容，可为 JSON 文本或 XML 文本，详情请参阅[`XML消息格式详解`](/docs/xmlmessage.md) |

示例：

```lua
Service(60,"xxxxxxx") 
```

<br />

<br />

<br />

## 消息拼接

使用操作符 + 或 .. 进行拼接，下面是一个简单的示例：

``` lua
ImageUrl("http://xxxxx",sender) .. Face(1) .. "hello"
```

<br />

<br />

## 消息解析

### 使用字符串函数处理

Message对象支持使用 [`lua字符串处理函数`](https://www.runoob.com/lua/lua-strings.html)。

如寻找消息中的文本可以使用以下方式：

``` lua
message:find("pattern") --等同于 string.find (message, "pattern")
```

<br />

### 消息遍历示例：

事件中接收到的消息往往是由多个消息类型组成的消息串，如需处理其中的单个消息，则需要调用toTable方法将消息拆分为多个消息对象，并进行遍历。

下面这个示例演示了如何查找消息中的图片并下载到本地。

``` lua
for i,m in ipairs(message:toTable()) do
	if (m:find(":image")) then
        m:downloadImage("C:\1.jpg")
    end
end
```



<br /><br />

## 方法

| 方法名        | 参数   | 返回值 | 描述                                               |
| ------------- | ------ | ------ | -------------------------------------------------- |
| recall        | 无     | 无     | 撤回此消息，仅适用于接收到的Message对象。         |
| toTable       | 无     | Table  | 将消息拆分为多个子消息，并转化为列表。             |
| downloadImage | String | 无     | 将消息中含有的图片下载到本地，参数为本地文件路径。 |
| getImageUrl   | 无     | String | 获取消息中图片的url地址                            |

