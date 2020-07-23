# 事件列表

## 基本格式

``` lua
bot:subscribeXXX(
	function(xxx,xxx,xxx) --参数列表
        --处理事件
    end
)
```

<br/>

## 好友消息 

### 函数名: subscribeFriendMsg

### 传入的回调函数的参数列表:

| 参数类型                              | 描述           |
| ------------------------------------- | -------------- |
| [`MiraiBot`](/docs/miraibot.md)       | bot对象        |
| [`MiraiMsg`](/docs/miraimsg.md)       | 接收到的消息   |
| [`MiraiFriend`](/docs/miraifriend.md) | 发送消息的好友 |

<br/>

## 群消息 

### 函数名: subscribeGroupMsg

### 传入的回调函数的参数列表:

| 参数类型                                        | 描述             |
| ----------------------------------------------- | ---------------- |
| [`MiraiBot`](/docs/miraibot.md)                 | bot对象          |
| [`MiraiMsg`](/docs/miraimsg.md)                 | 接收到的消息     |
| [`MiraiGroup`](/docs/miraigroup.md)             | 发送消息的群     |
| [`MiraiGroupMember`](/docs/miraigroupmember.md) | 发送消息的群成员 |

<br/>

## 发出好友消息 

### 函数名: subscribeFriendMsgSend

### 传入的回调函数的参数列表:

| 参数类型                              | 描述             |
| ------------------------------------- | ---------------- |
| [`MiraiBot`](/docs/miraibot.md)       | bot对象          |
| [`MiraiMsg`](/docs/miraimsg.md)       | 发送的消息       |
| [`MiraiFriend`](/docs/miraifriend.md) | 接收到消息的好友 |

<br/>

## 发出群消息 

### 函数名: subscribeGroupMsgSend

### 传入的回调函数的参数列表:

| 参数类型                            | 描述         |
| ----------------------------------- | ------------ |
| [`MiraiBot`](/docs/miraibot.md)     | bot对象      |
| [`MiraiMsg`](/docs/miraimsg.md)     | 发送的消息   |
| [`MiraiGroup`](/docs/miraigroup.md) | 接收消息的群 |

<br/>

## bot在线

### 函数名: subscribeBotOnlineEvent

### 传入的回调函数的参数列表:

| 参数类型                        | 描述    |
| ------------------------------- | ------- |
| [`MiraiBot`](/docs/miraibot.md) | bot对象 |

<br/>

## bot离线

### 函数名: subscribeBotOfflineEvent

### 传入的回调函数的参数列表:

| 参数类型                        | 描述    |
| ------------------------------- | ------- |
| [`MiraiBot`](/docs/miraibot.md) | bot对象 |

<br/>

## bot重新登录

### 函数名: subscribeBotReloginEvent

### 传入的回调函数的参数列表:

| 参数类型                        | 描述    |
| ------------------------------- | ------- |
| [`MiraiBot`](/docs/miraibot.md) | bot对象 |

<br/>

## bot在群里的权限被更改

### 函数名: subscribeBotGroupPermissionChangeEvent

### 传入的回调函数的参数列表:

| 参数类型                            | 描述          |
| ----------------------------------- | ------------- |
| [`MiraiBot`](/docs/miraibot.md)     | bot对象       |
| [`MiraiGroup`](/docs/miraigroup.md) | 事件发生的群  |
| String                              | bot的新权限名 |
| String                              | bot的旧权限名 |

<br/>

## bot被禁言

### 函数名: subscribeBotMutedEvent

### 传入的回调函数的参数列表:

| 参数类型                            | 描述       |
| ----------------------------------- | ---------- |
| [`MiraiBot`](/docs/miraibot.md)     | bot对象    |
| [`MiraiGroup`](/docs/miraigroup.md) | 被禁言的群 |

<br/>

## bot加入群

### 函数名: subscribeBotJoinGroupEvent

### 传入的回调函数的参数列表:

| 参数类型                            | 描述     |
| ----------------------------------- | -------- |
| [`MiraiBot`](/docs/miraibot.md)     | bot对象  |
| [`MiraiGroup`](/docs/miraigroup.md) | 加入的群 |

<br/>

## bot被踢出群聊

### 函数名: subscribeBotKickedEvent

### 传入的回调函数的参数列表:

| 参数类型                            | 描述       |
| ----------------------------------- | ---------- |
| [`MiraiBot`](/docs/miraibot.md)     | bot对象    |
| [`MiraiGroup`](/docs/miraigroup.md) | 被踢出的群 |

<br/>

## bot退出群聊

### 函数名: subscribeBotLeaveEvent

### 传入的回调函数的参数列表:

| 参数类型                            | 描述     |
| ----------------------------------- | -------- |
| [`MiraiBot`](/docs/miraibot.md)     | bot对象  |
| [`MiraiGroup`](/docs/miraigroup.md) | 退出的群 |

<br/>

## 群名被更改

### 函数名: subscribeGroupNameChangedEvent

### 传入的回调函数的参数列表:

| 参数类型                                        | 描述             |
| ----------------------------------------------- | ---------------- |
| [`MiraiBot`](/docs/miraibot.md)                 | bot对象          |
| [`MiraiGroup`](/docs/miraigroup.md)             | 事件发生的群     |
| [`MiraiGroupMember`](/docs/miraigroupmember.md) | 修改群名的群成员 |
| String                                          | 新的群名         |
| String                                          | 旧的群名         |

<br/>

## 新成员加入群聊

### 函数名: subscribeMemberJoinEvent

### 传入的回调函数的参数列表:

| 参数类型                                        | 描述       |
| ----------------------------------------------- | ---------- |
| [`MiraiBot`](/docs/miraibot.md)                 | bot对象    |
| [`MiraiGroup`](/docs/miraigroup.md)             | 加入的群聊 |
| [`MiraiGroupMember`](/docs/miraigroupmember.md) | 新成员     |

<br/>

## 新成员被邀请加入群聊

### 函数名: subscribeMemberInvitedEvent

### 传入的回调函数的参数列表:

| 参数类型                                        | 描述       |
| ----------------------------------------------- | ---------- |
| [`MiraiBot`](/docs/miraibot.md)                 | bot对象    |
| [`MiraiGroup`](/docs/miraigroup.md)             | 加入的群聊 |
| [`MiraiGroupMember`](/docs/miraigroupmember.md) | 新成员     |

<br/>

## 群员被移出群聊

### 函数名: subscribeMemberKickEvent

### 传入的回调函数的参数列表:

| 参数类型                                        | 描述         |
| :---------------------------------------------- | ------------ |
| [`MiraiBot`](/docs/miraibot.md)                 | bot对象      |
| [`MiraiGroup`](/docs/miraigroup.md)             | 群对象       |
| [`MiraiGroupMember`](/docs/miraigroupmember.md) | 被移出的成员 |

<br/>

## 群员名片被改变

### 函数名: subscribeMemberCardChangedEvent

### 传入的回调函数的参数列表:

| 参数类型                                        | 描述           |
| :---------------------------------------------- | -------------- |
| [`MiraiBot`](/docs/miraibot.md)                 | bot对象        |
| [`MiraiGroup`](/docs/miraigroup.md)             | 群对象         |
| [`MiraiGroupMember`](/docs/miraigroupmember.md) | 名片改变的成员 |
| String                                          | 新名片         |
| String                                          | 旧名片         |

<br/>

## 群员头衔被改变

### 函数名: subscribeMemberSpecialTitleChangeEvent

### 传入的回调函数的参数列表:

| 参数类型                                        | 描述           |
| :---------------------------------------------- | -------------- |
| [`MiraiBot`](/docs/miraibot.md)                 | bot对象        |
| [`MiraiGroup`](/docs/miraigroup.md)             | 群对象         |
| [`MiraiGroupMember`](/docs/miraigroupmember.md) | 名片改变的成员 |
| String                                          | 新头衔         |
| String                                          | 旧头衔         |

<br/>

## 群员权限被改变

### 函数名: subscribeMemberPermissionChangedEvent

### 传入的回调函数的参数列表:

| 参数类型                                        | 描述           |
| :---------------------------------------------- | -------------- |
| [`MiraiBot`](/docs/miraibot.md)                 | bot对象        |
| [`MiraiGroup`](/docs/miraigroup.md)             | 群对象         |
| [`MiraiGroupMember`](/docs/miraigroupmember.md) | 权限改变的成员 |
| String                                          | 新权限名       |
| String                                          | 旧权限名       |

<br/>

## 群员被禁言

### 函数名: subscribeMemberMutedEvent

### 传入的回调函数的参数列表:

| 参数类型                                        | 描述             |
| :---------------------------------------------- | ---------------- |
| [`MiraiBot`](/docs/miraibot.md)                 | bot对象          |
| [`MiraiGroup`](/docs/miraigroup.md)             | 群对象           |
| [`MiraiGroupMember`](/docs/miraigroupmember.md) | 被禁言的成员     |
| Integer                                         | 禁言时间，单位秒 |

<br/>

## 群员被解除禁言

### 函数名: subscribeMemberUnmutedEvent

### 传入的回调函数的参数列表:

| 参数类型                                        | 描述             |
| :---------------------------------------------- | ---------------- |
| [`MiraiBot`](/docs/miraibot.md)                 | bot对象          |
| [`MiraiGroup`](/docs/miraigroup.md)             | 群对象           |
| [`MiraiGroupMember`](/docs/miraigroupmember.md) | 被解除禁言的成员 |

