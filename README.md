# lua-mirai
 这是一个基于mirai-core接口实现的qq机器人框架，通过它，你将可以使用lua脚本语言快速开发qq机器人程序。

## 特性

* 配置简易，快速上手
    - 简单几步，便可以快速搭建你的qq机器人。
* 与java良好的交互性
    - lua-mirai支持使用接近于java的语法与java进行交互。
* 热加载特性
    - lua支持反射机制，你可以在lua脚本的任意处加载一段包含lua代码的字符串。

* 示例：一个简单的“复读机”脚本

 ``` LUA
local bot = Bot(qq账号, "qq密码"):login() --创建机器人并登录
bot:subscribeFriendMsg(function(bot, msg, sender) --监听好友消息
    sender:sendMsg(msg) --回复相同消息
end) 
bot:join() --挂起机器人
```


## 使用lua-mirai脚本
### 安卓端
请参阅 [`MiraiAndroid`](https://github.com/mzdluo123/MiraiAndroid)

API文档[`lua-mirai android api`](https://github.com/only52607/lua-mirai/blob/master/docs/miraiandroid.md)

## 开发lua-mirai脚本

### lua语言开发基础

请参阅 [`RUNOOB:Lua教程`](https://www.runoob.com/lua/lua-tutorial.html)

### 可用API列表

请参阅 [`lua-mirai api列表`](/docs/apis.md)

### 在lua中调用java类

#### 使用import命令导入java类
##### 示例：
``` lua
    import "java.lang.Thread" 
```

#### 使用java类静态成员和静态方法
``` lua
    类名.静态成员名或方法名(参数)
```
##### 示例
``` lua
    import "java.lang.String"
    print(String.join("-","lua","mirai"))
```

#### 创建java类对象，并调用方法
``` lua
    对象=类名(构造参数)
    对象:方法名(参数)
```
    
##### 示例：
``` lua
    import "java.lang.String"
    instance = String("lua-mirai")
    print(instance:toUpperCase())
```

#### 实现java类方法，并生成对象
``` lua
    对象 = 类名{
        方法名=function (参数)
        end
    }
```
    
##### 示例：
``` lua
    --实现Runnable接口并创建线程
    import "java.lang.Thread"
    import "java.lang.Runnable"
    Thread(
        Runnable{
            run=function()
                    print "Hello World!"
                end
        }
    ):start()
```


其他用法请参阅 [`CSDN:在Lua中操作Java对象`](https://blog.csdn.net/lgj123xj/article/details/81677036)

### 中文命名支持，示例：
``` lua
    function 相加(a,b)
        return a+b
    end
    function 输出(...)
        print(...)
    end
    输出(相加(1,1))
```

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
