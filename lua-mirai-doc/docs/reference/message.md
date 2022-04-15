# 消息（Message）类型参考

> 一个消息构造方法可能会有多个等同的名称，通常使用`/`进行分割，比如`Text/PlainText/Plain`

### 纯文本

1. 直接使用字符串：

示例：

```lua
"消息"
```

2. Text/PlainText/Plain 方法：

方法名：Text/PlainText/Plain

参数：

| 参数名  | 类型   | 说明           |
| ------- | ------ | -------------- |
| content | String | 纯文本消息内容 |

示例：

```lua
Text("消息内容")
```

### **Mirai 码消息**

方法名：Code/MiraiCode

参数：

| 参数名 | 类型     | 说明     |
| ------ | -------- | -------- |
| code   | `String` | Mirai 码 |

示例：

```lua
Code("[mirai:image:xxxxxx]")
```


### **引用回复**

方法名：Quote/QuoteReply/Reply

参数：

| 参数名 | 类型                                     | 说明               |
| ------ | ---------------------------------------- | ------------------ |
| source | [Message](../guide/message/README.md) | 必须是接收到的消息 |

示例：

```lua
Quote(message) -- 后必须拼接内容
```

### At

方法名：At

参数：

| 参数名 | 类型                                                         | 说明         |
| ------ | ------------------------------------------------------------ | ------------ |
| target | [Member](./contact?id=群成员-member)/[Friend](./contact.md#好友-friend) | 被 At 的对象 |

示例：

```lua
At(sender)
```

### At 全体

方法名：AtAll

参数：无

示例：

```lua
AtAll()
```

### 图片

1. 通过 ID 构建：

方法名：Image/ImageId

参数：

| 参数名 | 类型    | 说明                                                         |
| ------ | ------- | ------------------------------------------------------------ |
| uuid   | 图片 ID | 图片 id，格式形如 “/XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX”注：好友消息和群消息具有不同格式的图片 id |

示例：

```lua
Image("/XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX")
```

2. 通过 url 构建：

方法名：ImageUrl

参数：

| 参数名 | 类型                                                         | 说明                |
| ------ | ------------------------------------------------------------ | ------------------- |
| url    | String                                                       | 完整的图片 url 地址 |
| target | [Group](./contact.md#群-group)/[Friend](./contact.md#好友-friend) | 将接受消息的对象    |

示例：

```lua
ImageUrl("http://xxxx/xxx.jpg", group) -- 网络 url

ImageUrl("file:///xxxxx/xxx.jpg", friend) -- 本地 url
```

3. 通过文件构建：

方法名：ImageFile

参数：

| 参数名 | 类型                                                         | 说明                       |
| ------ | ------------------------------------------------------------ | -------------------------- |
| path   | String                                                       | 图片本地绝对路径或相对路径 |
| target | [Group](./contact.md#群-group)/[Friend](./contact.md#好友-friend) | 将接受消息的对象           |

示例：

```lua
ImageFile("xxx.jpg",group)
```

### 闪照

注：需要先构造普通图片

方法名：Flash/FlashImage

参数：

| 参数名 | 类型                                     | 说明         |
| ------ | ---------------------------------------- | ------------ |
| image  | [Message](../guide/message/README.md) | 普通图片消息 |

示例：

```lua
Flash(ImageUrl("http://xxxx/xxx.jpg",group)) -- 使用 url 构造图片，并转换为闪照
```

### 表情

方法名：Face

参数：

| 参数名 | 类型 | 说明     |
| ------ | ---- | -------- |
| code   | Int  | 表情代码 |

示例：

```lua
Face(1)
```


### Vip 表情

方法名：VipFace

参数：

| 参数名 | 类型 | 说明     |
| ------ | ---- | -------- |
| code   | Int  | 表情代码 |

示例：

```lua
VipFace(1)
```


### Poke 消息

#### 说明：戳一戳等特殊消息

方法名：Poke/PokeMessage

参数：

| 参数名 | 类型 | 说明      |
| ------ | ---- | --------- |
| code   | Int  | poke 代码 |

#### 可用代码：标*的为 vip 专属消息

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

### 转发消息

方法名：Forward/ForwardMessage

参数：

| 参数名 | 类型  | 说明         |
| ------ | ----- | ------------ |
| info   | Table | 转发消息描述 |

下面这个示例用于构建一个简单的转发消息

```lua
Forward ({
     title = "群聊的聊天记录",
     brief = "[聊天记录]",
     source = "聊天记录",
     preview = {
         "消息概览1",
         "消息概览2"
     }, -- 可省略
     summary = "查看 1 条转发消息", -- 可省略
     content = {
         {
             senderId = 123456789, -- 发送者 qq 号
             time = 987654, -- 发送的时间戳
             senderName = "发送者昵称1",
             message = Text("消息1") .. Face(0) -- 显示的消息内容
         },
         {
             senderId = 123456789,
             time = 987654,
             senderName = "发送者昵称2",
             message = Text("消息2") .. Face(0) -- 显示的消息内容
         },
     }
 })
```

### App 分享

#### 说明：小程序，如音乐分享。大部分 JSON 消息为此类型，另外一部分为 Service 消息。

方法名：App/LightApp

参数：

| 参数名  | 类型   | 说明                          |
| ------- | ------ | ----------------------------- |
| content | String | 消息内容，一般为一段 JSON 文本 |

示例：

完整示例请见：[音乐卡片](/demos/音乐卡片.lua)、解析可见：[Json 消息格式略解](./message/jsonmessage.md)

![音乐卡片示例](../res/musicCardSample.png)

```lua
formAMusicShare = function (title, srcUrl, desc, preview, jmpUrl, tag)
local __format__ = [[
{"app":"com.tencent.structmessage","config":{"autosize":true,"forward":true,"type":"normal"},
"desc":"音乐","meta":{"music":{"desc":"%s","jumpUrl":"%s","musicUrl":"%s","preview":"%s",
"tag":"%s","title":"%s"}},"prompt":"[%s]%s","view":"music"}]]
    return string.format (__format__:gsub("\n", ""),
        desc, jmpUrl, srcUrl, preview, tag, title, tag, title)
end
App("天外来物", "http://music.163.com/song/media/outer/url?id=1463165983", "薛之谦",
    "http://p4.music.126.net/HvB44MNINoLar8HFbRjIGQ==/109951165142435842.jpg", "QQbot 音乐")
```
如果想制作别的 App 分享，就像是 bilibili 那种大张图片的，可以通过自己发送这样一个消息，然后截获 bot 获得的消息，分析里面的 json 构成，然后可以删减一些不必要的东西，然后用 `string.format()` 构造一个 json 文本，填入 `App()` 并发送

### Service 消息

#### 说明：服务消息，可以是 JSON 消息或  [XML 消息](./message/xmlmessage.md)。JSON 消息更多情况下通过 App 发送。

方法名：Service/SimpleServiceMessage

参数：

| 参数名     | 类型    | 说明                                                         |
| ---------- | ------- | ------------------------------------------------------------ |
| service_id | Int | 消息类型，目前未知, XML 一般为 60, JSON 一般为 1             |
| content    | String  | 消息内容，可为 JSON 文本或 XML 文本，详情请参阅[XML 消息格式详解](./message/xmlmessage.md) |

示例：

```lua
Service(60, "xxxxxxx") 
```

### 音乐卡片分享

方法名：Music/MusicShare

参数：

| 参数名     | 类型       | 说明     |
| ---------- | ---------- | -------- |
| kindId     | Int/String | 音乐类型 |
| title      | String     | 标题     |
| summary    | String     | 副标题   |
| jumpUrl    | String     | 跳转url  |
| pictureUrl | String     | 图片url  |
| musicUrl   | String     | 音乐url  |


### 语音

方法名：Voice/Audio

参数：

| 参数名   | 类型             | 说明 |
| -------- | ---------------- | ---- |
| contact | Contact          |   接收语音对象   |
| fileName      | String |   语音文件路径   |
| formatName | String     |   语音格式名   |

