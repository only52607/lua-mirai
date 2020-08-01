# 消息对象（MiraiMsg）

## 消息构造

#### 构造一个空消息

Msg()

#### 构造纯文本消息

"任意内容"  或 Msg("任意内容")

#### **构造引用回复**

Quote(消息对象) 

#### 构造At消息

At(群成员)

#### 构造At全体消息

AtAll()

#### 构造图片消息

**通过ID构建：**  Image( 图片ID )

**通过url构建：** UploadImage( 图片URL ,群或好友 )

**通过本地路径构建：** ImageFile( 图片路径 ,群或好友 )

#### 构造表情

Face(表情代码)

## 消息拼接

使用appendXXX 或 + 或 .. 进行拼接，下面是一个简单的示例：

``` lua
Msg("hello"):appendText("world") + "lua" .. Msg():appendImage("http://xxxxx",sender) .. Face(1)
```

## 消息解析

### 使用字符串函数处理

MiraiMsg对象支持使用 [`lua字符串处理函数`](https://www.runoob.com/lua/lua-strings.html)。

如寻找消息中的文本可以使用以下方式：

``` lua
msg:find("pattern") --等同于 string.find (msg, "pattern")
```



### 消息遍历示例：

事件中接收到的消息往往是由多个消息类型组成的消息串，如需处理其中的单个消息，则需要调用toTable方法将消息拆分为多个消息对象，并进行遍历。

下面这个示例演示了如何查找消息中的图片并下载到本地。

``` lua
for i,m in ipairs(msg:toTable()) do
	if (m:find("mirai:image")) then
        m:downloadImage("C:\1.jpg")
    end
end
```

## 消息方法

| 方法名        | 参数   | 返回值      | 描述                                               |
| ------------- | ------ | ----------- | -------------------------------------------------- |
| recall        | 无     | 无          | 撤回此消息，仅适用于接收到的MiraiMsg对象。         |
| toTable       | 无     | Table       | 将消息拆分为多个子消息，并转化为列表。             |
| downloadImage | String | 无          | 将消息中含有的图片下载到本地，参数为本地文件路径。 |
| getImageUrl   | 无     | String      | 获取消息中图片的url地址                            |
| getSource     | 无     | MiraiSource | 获得消息的引用对象。                               |

