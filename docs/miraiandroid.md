# MiraiAndroid示例脚本

你可以在这里找到适用于MiraiAndroid的示例脚本： [`示例脚本`](https://github.com/only52607/lua-mirai/tree/master/demos)

## 脚本描述

执行脚本过程中，MiraiAndroid会读取脚本内名为**Info**的表，它包含了脚本的基本信息，包括名字、作者、版本、描述等信息。

关于**Info**表可用属性信息如下：

| 属性名      | 说明     |
| ----------- | -------- |
| name        | 脚本名   |
| author      | 作者     |
| version     | 版本     |
| description | 脚本描述 |

### 示例：

```lua
Info={
    name="简单的lua脚本",
    author="ooooonly",
    version="1.0",
    description="一个简单的脚本"
}
```

## 脚本生命周期

关于脚本生命周期的函数包含在**Event**表中，当脚本被加载或卸载时，会执行**Event**表内对应回调函数。

下面列出了Event表的可用属性信息：

| 属性名   | 参数说明                                                     | 说明         |
| -------- | ------------------------------------------------------------ | ------------ |
| onLoad   | Function，其中Function接受一个[`MiraiBot`](/docs/miraibot.md)类型参数 | 脚本载入事件 |
| onFinish | Function                                                     | 脚本卸载事件 |

### 示例：

```lua
function Event.onLoad(bot)
	print("脚本已加载！")
end

Event.onFinish = function () 
    print("脚本被卸载！") 
end
```

## 