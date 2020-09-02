# 快速运行你的第一个 lua-mirai 脚本



#### 1.配置 java 环境（具体配置过程请网上自行搜索，此处省略）



#### 2.编译或下载最新版本[lua-mirai.jar](https://github.com/only52607/lua-mirai/releases)



#### 3.在 jar 目录下，创建 ai.lua 文件，并填入以下代码。（注意填写账号密码）

```lua
function listener(event)
    local msg = event.message
    local sender = event.sender
    local rep = msg:gsub("吗",""):gsub("?","!"):gsub("？","！")
    sender:sendMessage(Quote(msg) .. rep)
end

function onLoad(bot)
    bot:subscribe("FriendMessageEvent",listener)
end

local bot = Bot(账号,"密码")
bot:login()
onLoad(bot)
```



#### 4.启动 cmd，进入 ai.lua 目录，执行以下代码

```powershell
java -jar <jar 文件名> exec <lua 文件名>
```

#### 例：

```powershell
java -jar lua-mirai-1.1.0.jar exec ai.lua
```



#### 5.测试机器人

![aiDialog](/docs/res/aiDialog.png)

向机器人发送上述内容，若回复成功，则代表你已经成功运行了你的一个 lua-mirai 脚本。



# 编写自己的 lua 脚本

[`lua-mirai 脚本开发指南`](/docs/guide.md)

