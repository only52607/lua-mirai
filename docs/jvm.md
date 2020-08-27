# 快速运行你的第一个lua-mirai脚本



## 1.配置java环境（具体配置过程请网上自行搜索，此处省略）



## 2.编译或下载最新版本[lua-mirai.jar](https://github.com/only52607/lua-mirai/releases)



## 3.在jar目录下，创建ai.lua文件，并填入以下代码。（注意填写账号密码）

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



## 4.启动cmd，进入ai.lua目录，执行以下代码

```powershell
java -jar jar文件名 exec lua文件名
```

### 例：

```powershell
java -jar lua-mirai-1.0.0.jar exec ai.lua
```



## 5.测试机器人

![aiDialog](/docs/res/aiDialog.png)

向机器人发送上述内容，若回复成功，则代表你已经成功运行了你的一个lua-mirai脚本。