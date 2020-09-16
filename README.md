

# lua-mirai

 这是一个实现了[`mirai`](https://github.com/mamoe/mirai)规范的 lua 快速开发框架。



## 使用声明

1、本项目仅供学习参考，禁止用于任何商业用途。

2、本项目不含有任何旨在破坏用户计算机数据和获取用户隐私的恶意代码，不含有任何跟踪、监视用户计算机功能代码，不会收集任何用户个人信息，不会泄露用户隐私。

3、本项目不提供任何具体功能实现，仅仅只是对原项目[`mirai`](https://github.com/mamoe/mirai)的二次封装。因使用本项目造成或与本项目有关的一切争议或法律纠纷，将由原项目[`mirai`](https://github.com/mamoe/mirai)全部承担。

4、任何单位或个人认为本项目可能涉嫌侵犯其合法权益，应该及时提出反馈，我们将会第一时间对违规内容给予删除等相关处理。

### QQ 交流群:120408574

## 快速开始

### 安卓端

请使用 [`MiraiAndroid`](https://github.com/mzdluo123/MiraiAndroid)
开发文档:[`MiraiAndroid lua 脚本开发指南`](/docs/miraiandroid.md)

### PC 端

- [x] 控制台界面： [`在 JVM 上运行 lua-mirai`](/docs/jvm.md)
- [x] Web 界面（预览版）[`运行 lua-mirai并使用浏览器管理`](/docs/web.md)
- [ ] Swing Gui 界面


### 作为项目依赖

#### Gradle

添加 jcenter 仓库

``` groovy
buildscript{
    //...
    repositories {
        //...
        jcenter() 
    }
}
```

添加 mirai 以及 lua-mirai 依赖

```groovy
dependencies {
    implementation "net.mamoe:mirai-core-qqandroid:$MIRAI_VERSION" //Mirai Core
    implementation "com.ooooonly:luaMirai:$LUAMIRAI_VERSION" //lua-mirai
}
```



## lua-mirai 脚本开发

### lua 语言基础

请参阅 [`30分钟快速入门 lua 语言`](https://www.runoob.com/lua/lua-tutorial.html)

### lua-mirai 开发指南

[`lua-mirai 开发指南`](/docs/guide.md)

[`lua-mirai API 列表`](/docs/apis.md)



## 部分引用库

 - Bot实现核心： [`mirai`](https://github.com/mamoe/mirai)
 - Lua语言支持： [`luakt`](https://github.com/only52607/luakt)
 - web管理后端核心： [vert.x](https://github.com/eclipse-vertx/vert.x) / [vertx-web](https://github.com/vert-x3/vertx-web)

## 许可证

基于原项目[`mirai`](https://github.com/mamoe/mirai)使用[`GNU AGPLv3`](https://choosealicense.com/licenses/agpl-3.0/)作为开发许可证，该项目同样使用了[`GNU AGPLv3`](https://choosealicense.com/licenses/agpl-3.0/) 作为开源许可证, 因此,在使用时务必遵守相应的规则. 。

