# lua-mirai
 这是一个基于mirai-core接口实现的qq机器人框架，通过它，你将可以使用lua脚本语言开发qq机器人程序。

## 特性

* 配置简易，快速上手
    - 相比于同类型mirai框架，lua-mirai基于mirai-core开发，使用时省略了繁琐的依赖配置过程，你仅需java执行环境和lua-mirai核心jar包，便可以启动你的lua-mirai机器人
* 与java良好的交互性
    - 得益于luaj提供的luajava支持库，仅仅几行代码，便可以在lua中使用接近于java的语法方便地操作java类和对象。
* 热加载特性
    - lua语言天生支持反射机制，这意味这我们可以在lua脚本的任意地方再次加载一段包含lua代码的字符串以实现动态更新技术。

 
* 示例：一个简单的“复读机”脚本

 ``` LUA
local bot = Bot(qq账号, "qq密码"):login() --创建机器人并登录
bot:subscribeFriendMsg(function(bot, msg, sender) --监听好友消息
    sender:sendMsg(msg) --回复相同消息
end) 
bot:join() --挂起机器人
```


## 运行你的第一个lua-mirai脚本

1. 安装java运行环境(jdk/jre)
    - 请参阅 [`RUNOOB:Java 开发环境配置`](https://www.runoob.com/java/java-environment-setup.html)
2. 获取jar
    - 使用已编译jar包
        - [`lua-mirai.jar`](https://github.com/only52607/lua-mirai/releases/download/0.1/lua-mirai.jar)

    - 手动编译
        1. 打开idea，导入lua-mirai项目
        2. 点击上方工具栏 Build->Build Artifacts
        3. 稍等片刻，执行完毕后将会在工程目录out文件夹中找到jar包
        4. (重点!)使用压缩软件打开jar包,进入META-INF目录,并删除**当前目录下除了maven文件夹和MANIFEST.MF以外的所有文件**,并保存。
3. 开始使用
    - 使用控制台界面
        1. 进入jar包所在目录
        2. 创建main.lua文件，填入以下内容并将账号和密码更改为机器人的账号和密码
            ```LUA
            --来自示例复读机源码
            local bot = Bot(qq账号, "QQ密码"):login() --创建机器人并登录
            bot:subscribeFriendMsg(function(bot, msg, sender) --监听好友消息
               sender:sendMsg(msg) --回复相同消息
            end) 
            bot:join() --挂起机器人
            ```
        3. 打开cmd，进入jar包所在目录，假设jar包名称为lua-mirai.jar，则输入
            ```
            java -jar lua-mirai.jar main.lua
            ```
        4. 按下回车，等待片刻，并向机器人发送任意消息，机器人将会回复相同内容。
    - 启动可视化界面
        - 待完成，敬请期待。


## 开发lua-mirai脚本

### lua语言开发基础

请参阅 [`RUNOOB:Lua教程`](https://github.com/mamoe/mirai)

### 可用API列表

请参阅 [`lua-mirai api列表`](/docs/apis.md)

### 在lua中调用java类

请参阅 [`CSDN:在Lua中操作Java对象`](https://blog.csdn.net/lgj123xj/article/details/81677036)

### 在lua中动态加载lua代码或外部lua脚本   
 
* load   
> 用于加载一个数据块.从字符串或者函数中加载一个代码块为方法并返回.   
> 示例:   
>  load("print('Hello World!')")()   
> 输出   
> Hello World!
* loadfile   
> 类似于load，但传入参数为文件路径而不是字符串文本。
* dofile   
> 与loadfile类似，但加载后自动执行。

### 鸣谢

 - [`mirai`](https://github.com/mamoe/mirai): 即 `mirai-core`, 多平台 QQ Android 和 TIM PC 协议支持库与高效率的机器人框架.
 - [`luaj`](https://github.com/luaj/luaj): 一个 Java 的轻量级、高性能 Lua 解释器，基于 Lua 5.2.x 版本.

### 许可证

基于原项目mirai使用[`GNU AGPLv3`](https://choosealicense.com/licenses/agpl-3.0/)作为开发许可证，该项目同样使用了[`GNU AGPLv3`](https://choosealicense.com/licenses/agpl-3.0/) 作为开源许可证, 因此,在使用时务必遵守相应的规则.  
