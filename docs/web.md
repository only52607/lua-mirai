# 快速运行你的第一个 lua-mirai 脚本(基于[mirado-rua](https://github.com/only52607/mirado-rua))



#### 1.配置 java 环境（具体配置过程请网上自行搜索，此处省略）



#### 2.编译或下载最新版本[lua-mirai.jar](https://github.com/only52607/lua-mirai/releases)



#### 3.创建 ai.lua 文件，并填入以下代码。

```lua
Info = {
	name = "人工智能",
    version = "v1.1",
    author = "ooooonly",
    description = "核心代码为一行的人工智能",
    usage = "向机器人私聊发送以下内容：\n1.在吗？\n2.会说汉语吗?"
}

function listener(event)
    local msg = event.message
    local sender = event.sender
    local rep = msg:gsub("吗",""):gsub("?","!"):gsub("？","！")
    sender:sendMessage(Quote(msg) .. rep)
end

function onLoad(bot)
    bot:subscribe("FriendMessageEvent",listener)
end
```



#### 4.启动 cmd，进入 ai.lua 目录，执行以下代码

```powershell
java -jar <jar 文件名> web
```

#### 例：

```powershell
java -jar lua-mirai-1.2.0.jar web
```



#### 5.进入浏览器，进入http://localhost，默认登录账号密码均为"admin"。



#### 6.进入bot添加界面，填写bot的账号密码，创建bot。



#### 7.进入脚本管理界面，添加刚刚编写的ai.lua脚本



#### 8.测试机器人

向机器人发送上述内容，若回复成功，则代表你已经成功运行了你的一个 lua-mirai 脚本。



# 编写自己的 lua 脚本

[`lua-mirai 脚本开发指南`](/docs/guide.md)

