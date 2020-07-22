

# lua-mirai

 这是一个qq机器人lua语言框架，你可以使用lua语言快速开发qq机器人。

## 开始使用

### 安卓端

请参阅 [`MiraiAndroid`](https://github.com/mzdluo123/MiraiAndroid)

### PC端(JVM)

请参阅 [`lua-mirai jvm`](/docs/jvm.md)

### 作为依赖引入第三方项目

#### Gradle

添加jcenter仓库

``` groovy
buildscript{
    //...
    repositories {
        //...
        jcenter() 
    }
}
```

添加mirai以及lua-mirai依赖

```groovy
dependencies {
    implementation "net.mamoe:mirai-core-qqandroid:$MIRAI_VERSION" //Mirai Core
    implementation "com.ooooonly:luaMirai:$LUAMIRAI_VERSION" //lua-mirai
}
```



## 开发lua-mirai脚本

### lua语言开发基础

请参阅 [`RUNOOB:Lua教程`](https://www.runoob.com/lua/lua-tutorial.html)

### lua-mirai开发指南

请参阅 [`lua-mirai 指南`](/docs/guide.md)

### 第三方库

 - [`mirai`](https://github.com/mamoe/mirai): 即 `mirai-core`, 多平台 QQ Android 和 TIM PC 协议支持库与高效率的机器人框架.
 - [`luaj`](https://github.com/luaj/luaj): 一个 Java 的轻量级、高性能 Lua 解释器，基于 Lua 5.2.x 版本.
 - okhttp3

### 许可证

基于原项目mirai使用[`GNU AGPLv3`](https://choosealicense.com/licenses/agpl-3.0/)作为开发许可证，该项目同样使用了[`GNU AGPLv3`](https://choosealicense.com/licenses/agpl-3.0/) 作为开源许可证, 因此,在使用时务必遵守相应的规则.  
