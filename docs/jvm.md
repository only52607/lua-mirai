# 在jvm上单独运行lua-mirai



## 1.配置java环境（具体配置过程请网上自行搜索，此处省略）



## 2.编译或下载最新版本[lua-mirai.jar](https://github.com/only52607/lua-mirai/releases)



## 3.编写脚本，或者你可以使用下面的示例脚本（注意将下面的123456789以及abcdedfg替换为bot的账号密码）

```lua
--获得bot对象
bot = Bot(123456789,"abcdefg")

--登录bot
bot:login()

-- 订阅好友消息并回复相同的内容
bot:subscribeFriendMsg(
    function(bot, msg, sender)
  		sender:sendMsg(msg)
	end
)
```



## 4.进入脚本目录，运行java -jar lua-mirai.jar 脚本路径

### 例如

```powershell
java -jar lua-mirai-0.3.jar test.lua
```



## 5.退出脚本请Ctrl + C 或直接关闭控制台