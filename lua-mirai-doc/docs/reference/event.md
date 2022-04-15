# 事件类型（Event）参考

> 带`*`的事件为多个事件的组合

### Event*
#### 说明：所有事件
#### Event 对象成员：


### BotEvent*
#### 说明：有关一个 Bot 的所有事件
#### Event 对象成员：


### BotPassiveEvent*

#### 说明：被动接收的事件，这些事件可能与机器人有关
#### Event 对象成员：


### BotActiveEvent*
##### 说明：主动发起的动作的事件
##### Event 对象成员：


### BotOnlineEvent
##### 说明: 登录完成, 好友列表, 群组列表初始化完成
##### Event 对象成员：

| 成员名 | 成员类型                           | 描述          |
| ------ | ---------------------------------- | ------------- |
| bot    |  [Bot](./bot.md) | 来源 Bot 对象 |


### BotOfflineEvent 
##### 说明: 离线
##### Event 对象成员：


| 成员名 | 成员类型                           | 描述          |
| ------ | ---------------------------------- | ------------- |
| bot    |  [Bot](./bot.md) | 来源 Bot 对象 |
| cause  |                                    | 原因          |

### BotOfflineEvent.Active 
##### 说明: 主动离线
##### Event 对象成员：


| 成员名 | 成员类型              | 描述        |
| ------ | --------------------- | ----------- |
| bot    |  [Bot](./bot.md) | 来源 Bot 对象 |
| cause  |        | 原因        |

### BotOfflineEvent.Force 
##### 说明: 被挤下线
##### Event 对象成员：


| 成员名  | 成员类型              | 描述        |
| ------- | --------------------- | ----------- |
| bot     |  [Bot](./bot.md) | 来源 Bot 对象 |
| title   |        | 标题        |
| message | [Message](../guide/message.md) | 消息        |

### BotOfflineEvent.MsfOffline 
##### 说明: 被服务器断开
##### Event 对象成员：


| 成员名 | 成员类型              | 描述        |
| ------ | --------------------- | ----------- |
| bot    |  [Bot](./bot.md) | 来源 Bot 对象 |
| cause  |        | 原因        |

### Dropped 
##### 说明: 因网络问题而掉线
##### Event 对象成员：


| 成员名 | 成员类型              | 描述        |
| ------ | --------------------- | ----------- |
| bot    |  [Bot](./bot.md) | 来源 Bot 对象 |
| cause  |        | 原因        |

### BotOfflineEvent.RequireReconnect 
##### 说明: 服务器主动要求更换另一个服务器
##### Event 对象成员：


| 成员名 | 成员类型              | 描述        |
| ------ | --------------------- | ----------- |
| bot    |  [Bot](./bot.md) | 来源 Bot 对象 |

### BotReloginEvent 
##### 说明: 主动或被动重新登录. 在此事件广播前就已经登录完毕.
##### Event 对象成员：


| 成员名 | 成员类型              | 描述        |
| ------ | --------------------- | ----------- |
| bot    |  [Bot](./bot.md) | 来源 Bot 对象 |
| cause  |        | 原因        |

### BotAvatarChangedEvent 
##### 说明: 头像被修改（通过其他客户端修改了头像）
##### Event 对象成员：


| 成员名 | 成员类型              | 描述        |
| ------ | --------------------- | ----------- |
| bot    |  [Bot](./bot.md) | 来源 Bot 对象 |

### FriendMessageEvent 

##### 说明: 机器人收到的好友消息的事件

##### Event 对象成员：

| 成员名             | 成员类型                                                     | 描述           |
| ------------------ | ------------------------------------------------------------ | -------------- |
| bot                |  [Bot](./bot.md)                           | 来源 Bot 对象  |
| sender/user/friend | [Friend](./contact.md#好友-friend) | 发送消息的好友 |
| message            | [Message](../guide/message.md)                   | 收到的消息     |





### GroupMessageEvent 

##### 说明: 机器人收到的群消息的事件

##### Event 对象成员：

| 成员名     | 成员类型                                                | 描述                                                         |
| ---------- | ------------------------------------------------------- | ------------------------------------------------------------ |
| bot        |  [Bot](./bot.md)                               | 来源 Bot 对象                                                |
| group      | [Group](./contact.md#群-group)                          | 来源群                                                       |
| sender     | [Member](./contact.md#群成员-member) | 发送消息的成员                                               |
| senderName | string                                                  | 成员昵称                                                     |
| message    | [Message](../guide/message.md)/MessageChain    | 收到的消息                                                   |
| source     | MessageSource                                           | 收到的消息源，可用于引用和撤回。                             |
| time       | number                                                     | 接收到的时间，时间戳格式。                                   |
| permission | MemberPermission                                        | 成员权限<br/>OWNER：群主<br/>ADMINISTRATOR：管理员<br/>MEMBER：普通成员 |

### GroupEvent*
##### 说明: 有关群的事件
##### Event 对象成员：


| 成员名 | 成员类型                                          | 描述          |
| :----- | ------------------------------------------------- | ------------- |
| bot    |  [Bot](./bot.md)                | 来源 Bot 对象 |
| group  | [Group](./contact.md#群-group) | 来源群        |

### GroupMemberEvent 
##### 说明: 有关群成员的事件
##### Event 对象成员：


| 成员名 | 成员类型                                           | 描述          |
| ------ | ------------------------------- | ------------- |
| bot    |  [Bot](./bot.md)                 | 来源 Bot 对象 |
| group  | [Group](./contact.md#群-group)  | 来源群        |
| member | [Member](./contact.md#群-group) | 被邀请的人    |

### MemberJoinEvent 
##### 说明: 成员已经加入群的事件
##### Event 对象成员：


| 成员名 | 成员类型                   | 描述        |
| ------ | -------------------------- | ----------- |
| bot    |  [Bot](./bot.md)      | 来源 Bot 对象 |
| group  | [Group](./contact.md#群-group)  | 来源群      |
| member | [Member](./contact.md#群-group) | 被邀请的人  |

### MemberJoinEvent.Invite 
##### 说明: 被邀请加入群
##### Event 对象成员：


| 成员名 | 成员类型                   | 描述        |
| ------ | -------------------------- | ----------- |
| bot    |  [Bot](./bot.md)      | 来源 Bot 对象 |
| group  | [Group](./contact.md#群-group)  | 来源群      |
| member | [Member](./contact.md#群-group) | 被邀请的人  |

### MemberJoinEvent.Active 
##### 说明: 成员主动加入群
##### Event 对象成员：


| 成员名 | 成员类型                   | 描述        |
| ------ | -------------------------- | ----------- |
| bot    |  [Bot](./bot.md)      | 来源 Bot 对象 |
| group  | [Group](./contact.md#群-group)  | 来源群      |
| member | [Member](./contact.md#群-group) | 被邀请的人  |

### MemberLeaveEvent 
##### 说明: 成员已经离开群的事件
##### Event 对象成员：


### MemberLeaveEvent.Kick 
##### 说明: 成员被踢出群. 成员不可能是机器人自己.
##### Event 对象成员：


| 成员名   | 成员类型                   | 描述        |
| -------- | -------------------------- | ----------- |
| bot      |  [Bot](./bot.md)      | 来源 Bot 对象 |
| group    | [Group](./contact.md#群-group)  | 来源群      |
| member   | [Member](./contact.md#群-group) | 被踢的人    |
| operator | [Member](./contact.md#群-group) | 操作员      |

### MemberLeaveEvent.Quit 
##### 说明: 成员主动离开
##### Event 对象成员：


| 成员名   | 成员类型                   | 描述        |
| -------- | -------------------------- | ----------- |
| bot      |  [Bot](./bot.md)      | 来源 Bot 对象 |
| group    | [Group](./contact.md#群-group)  | 来源群      |
| member   | [Member](./contact.md#群-group) | 离开的人    |

### MemberCardChangeEvent 
##### 说明: 成员群名片改动. 此事件广播前修改就已经完成.
##### Event 对象成员：


| 成员名 | 成员类型                   | 描述        |
| ------ | -------------------------- | ----------- |
| bot    |  [Bot](./bot.md)      | 来源 Bot 对象 |
| group  | [Group](./contact.md#群-group)  | 来源群      |
| member | [Member](./contact.md#群-group) | 被改的人    |
| origin | string               | 原名片      |
| new    | string               | 现名片      |
| member | [Member](./contact.md#群-group) | 操作员    |

### MemberHonorChangeEvent.Achieve

##### 说明: 

##### Event 对象成员：

### MemberSpecialTitleChangeEvent 

##### 说明: 成员群头衔改动. 一定为群主操作
##### Event 对象成员：


| 成员名 | 成员类型                   | 描述                               |
| ------ | -------------------------- | ---------------------------------- |
| bot    |  [Bot](./bot.md)      | 来源 Bot 对象                        |
| group  | [Group](./contact.md#群-group)  | 来源群                             |
| member | [Member](./contact.md#群-group) | 被改的人                           |
| origin | string               | 原头衔                             |
| new    | string               | 现头衔                             |
| member | [Member](./contact.md#群-group) | 操作员（nil 为机器人，反之为群主） |

### MemberPermissionChangeEvent 
##### 说明: 成员权限改变的事件. 成员不可能是机器人自己.
##### Event 对象成员：


| 成员名 | 成员类型                   | 描述        |
| ------ | -------------------------- | ----------- |
| bot    |  [Bot](./bot.md)      | 来源 Bot 对象 |
| group  | [Group](./contact.md#群-group)  | 来源群      |
| member | [Member](./contact.md#群-group) | 被改的人    |
| origin | MemberPermission     | 原权限      |
| new    | MemberPermission     | 现权限      |

### MemberMuteEvent 
##### 说明: 群成员被禁言事件. 被禁言的成员都不可能是机器人本人
##### Event 对象成员：


| 成员名          | 成员类型                   | 描述         |
| --------------- | -------------------------- | ------------ |
| bot             |  [Bot](./bot.md)      | 来源 Bot 对象  |
| group           | [Group](./contact.md#群-group)  | 来源群       |
| member          | [Member](./contact.md#群-group) | 被禁言的人   |
| durationSeconds | number                  | 禁言时长(秒) |
| operator        | [Member](./contact.md#群-group) | 操作员       |

### MemberUnmuteEvent 
##### 说明: 群成员被取消禁言事件. 被禁言的成员都不可能是机器人本人
##### Event 对象成员：


| 成员名          | 成员类型                   | 描述         |
| --------------- | -------------------------- | ------------ |
| bot             |  [Bot](./bot.md)      | 来源 Bot 对象  |
| group           | [Group](./contact.md#群-group)  | 来源群       |
| member          | [Member](./contact.md#群-group) | 被取消禁言的人   |
| operator        | [Member](./contact.md#群-group) | 操作员       |

### GroupOperableEvent 
##### 说明: 可由 [Member] 或 [Bot] 操作的事件
##### Event 对象成员：


### GroupSettingChangeEvent 
##### 说明: 群设置改变. 此事件广播前修改就已经完成.        
##### Event 对象成员：


| 成员名 | 成员类型                   | 描述        |
| ------ | -------------------------- | ----------- |
| bot    |  [Bot](./bot.md)      | 来源 Bot 对象 |
| group  | [Group](./contact.md#群-group)  | 来源群      |
| member | [Member](./contact.md#群-group) | 操作员      |
| origin | `T ?`                    | 原设置      |
| new    | `T ?`                    | 现设置      |

### GroupNameChangeEvent 
##### 说明: 群名改变. 此事件广播前修改就已经完成.
##### Event 对象成员：


| 成员名 | 成员类型                   | 描述                       |
| ------ | -------------------------- | -------------------------- |
| bot    |  [Bot](./bot.md)      | 来源 Bot 对象                |
| group  | [Group](./contact.md#群-group)  | 来源群                     |
| member | [Member](./contact.md#群-group) | 操作员（`nil` 为机器人操作） |
| origin | string               | 原群名                     |
| new    | string               | 现群名                     |

### GroupEntranceAnnouncementChangeEvent 
##### 说明: 入群公告改变. 此事件广播前修改就已经完成.
##### Event 对象成员：


| 成员名 | 成员类型                   | 描述                           |
| ------ | -------------------------- | ------------------------------ |
| bot    |  [Bot](./bot.md)      | 来源 Bot 对象                    |
| group  | [Group](./contact.md#群-group)  | 来源群                         |
| member | [Member](./contact.md#群-group) | 操作员（`nil` 为机器人操作？） |
| origin | string               | 原公告                         |
| new    | string               | 现公告                         |

### GroupMuteAllEvent 
##### 说明: 群 "全员禁言" 功能状态改变. 此事件广播前修改就已经完成.
##### Event 对象成员：


| 成员名 | 成员类型                   | 描述                           |
| ------ | -------------------------- | ------------------------------ |
| bot    |  [Bot](./bot.md)      | 来源 Bot 对象                    |
| group  | [Group](./contact.md#群-group)  | 来源群                         |
| member | [Member](./contact.md#群-group) | 操作员（`nil` 为机器人操作？） |
| origin | boolean              | 原禁言状态                     |
| new    | boolean              | 现禁言状态                     |

### GroupAllowAnonymousChatEvent 
##### 说明: 群 "匿名聊天" 功能状态改变. 此事件广播前修改就已经完成.
##### Event 对象成员：


| 成员名 | 成员类型                   | 描述                           |
| ------ | -------------------------- | ------------------------------ |
| bot    |  [Bot](./bot.md)      | 来源 Bot 对象                    |
| group  | [Group](./contact.md#群-group)  | 来源群                         |
| member | [Member](./contact.md#群-group) | 操作员（`nil` 为机器人操作？） |
| origin | boolean              | 原匿名聊天状态                 |
| new    | boolean              | 现匿名聊天状态                 |

### GroupAllowConfessTalkEvent 
##### 说明: 群 "坦白说" 功能状态改变. 此事件广播前修改就已经完成.
##### Event 对象成员：


| 成员名 | 成员类型                   | 描述                           |
| ------ | -------------------------- | ------------------------------ |
| bot    |  [Bot](./bot.md)      | 来源 Bot 对象                    |
| group  | [Group](./contact.md#群-group)  | 来源群                         |
|        |                      | 无法获取操作人（？） |
| origin | boolean              | 原坦白说状态                     |
| new    | boolean              | 现坦白说状态                     |

### GroupAllowMemberInviteEvent 
##### 说明: 群 "允许群员邀请好友加群" 功能状态改变. 此事件广播前修改就已经完成.
##### Event 对象成员：


| 成员名 | 成员类型                   | 描述                           |
| ------ | -------------------------- | ------------------------------ |
| bot    |  [Bot](./bot.md)      | 来源 Bot 对象                    |
| group  | [Group](./contact.md#群-group)  | 来源群                         |
| member | [Member](./contact.md#群-group) | 操作员（`nil` 为机器人操作？） |
| origin | boolean              | 原允许群员邀请好友加群状态     |
| new    | boolean              | 现允许群员邀请好友加群状态     |

### BotGroupPermissionChangeEvent 
##### 说明: Bot 在群里的权限被改变. 操作人一定是群主
##### Event 对象成员：


| 成员名 | 成员类型                  | 描述        |
| ------ | ------------------------- | ----------- |
| bot    |  [Bot](./bot.md)     | 来源 Bot 对象 |
| group  | [Group](./contact.md#群-group) | 来源群      |
| orgin  | MemberPermission    | 原先的权限  |
| new    | MemberPermission    | 现在的权限  |

### BotMuteEvent 
##### 说明: Bot 被禁言
##### Event 对象成员：


| 成员名          | 成员类型                  | 描述         |
| --------------- | ------------------------- | ------------ |
| bot             |  [Bot](./bot.md)     | 来源 Bot 对象  |
| group           | [Group](./contact.md#群-group) | 来源群       |
| durationSeconds | number                 | 被禁言的秒数 |
| member          | [Member](./contact.md#群-group)              | 操作人员     |

### BotUnmuteEvent 
##### 说明: Bot 被取消禁言
##### Event 对象成员：


| 成员名          | 成员类型                  | 描述         |
| --------------- | ------------------------- | ------------ |
| bot             |  [Bot](./bot.md)     | 来源 Bot 对象  |
| group           | [Group](./contact.md#群-group) | 来源群       |
| member          | [Member](./contact.md#群-group)              | 操作人员     |

### BotJoinGroupEvent 
##### 说明: Bot 成功加入了一个新群
##### Event 对象成员：


| 成员名          | 成员类型                  | 描述         |
| --------------- | ------------------------- | ------------ |
| bot             |  [Bot](./bot.md)     | 来源 Bot 对象  |
| group           | [Group](./contact.md#群-group) | 来源群       |

### BotJoinGroupEvent.Active 
##### 说明: 不确定. 可能是主动加入
##### Event 对象成员：


| 成员名          | 成员类型                  | 描述         |
| --------------- | ------------------------- | ------------ |
| bot             |  [Bot](./bot.md)     | 来源 Bot 对象  |
| group           | [Group](./contact.md#群-group) | 来源群       |

### BotJoinGroupEvent.Invite 
##### 说明: Bot 被一个群内的成员直接邀请加入了群
##### Event 对象成员：


| 成员名          | 成员类型                  | 描述         |
| --------------- | ------------------------- | ------------ |
| bot             |  [Bot](./bot.md)     | 来源 Bot 对象  |
| group           | [Group](./contact.md#群-group) | 来源群       |
| member          | [Member](./contact.md#群-group)              | 操作人员     |

### BotInvitedJoinGroupRequestEvent 
##### 说明: [Bot] 被邀请加入一个群
##### Event 对象成员：


| 成员名   | 成员类型                   | 描述        |
| -------- | -------------------------- | ----------- |
| bot      |  [Bot](./bot.md)      | 来源 Bot 对象 |
| group    | [Group](./contact.md#群-group)  | 来源群      |
| member   | [Member](./contact.md#群-group) | 邀请人    |


### MemberJoinRequestEvent 
##### 说明: 一个账号请求加入群事件, [Bot] 在此群中是管理员或群主.
##### Event 对象成员：


| 成员名   | 成员类型                   | 描述        |
| -------- | -------------------------- | ----------- |
| bot      |  [Bot](./bot.md)      | 来源 Bot 对象 |
| group    | [Group](./contact.md#群-group)  | 来源群      |
| member   | [Member](./contact.md#群-group) | 请求人    |


### BotLeaveEvent 
##### 说明: 机器人被踢出群或在其他客户端主动退出一个群
##### Event 对象成员：


| 成员名   | 成员类型                    | 描述        |
| -------- | --------------------------- | ----------- |
| bot      |  [Bot](./bot.md)       | 来源 Bot 对象 |
| group    | [Group](./contact.md#群-group)   | 来源群      |
| operator | [Member](./contact.md#群成员-member) | 操作员      |

### BotLeaveEvent.Active 
##### 说明: 机器人主动退出一个群
##### Event 对象成员：


| 成员名 | 成员类型                  | 描述        |
| ------ | ------------------------- | ----------- |
| bot    |  [Bot](./bot.md)     | 来源 Bot 对象 |
| group  | [Group](./contact.md#群-group) | 来源群      |

### BotLeaveEvent.Kick 
##### 说明: 机器人被管理员或群主踢出群
##### Event 对象成员：


| 成员名   | 成员类型                    | 描述        |
| -------- | --------------------------- | ----------- |
| bot      |  [Bot](./bot.md)       | 来源 Bot 对象 |
| group    | [Group](./contact.md#群-group)   | 来源群      |
| operator | [Member](./contact.md#群成员-member) | 操作员      |

### FriendEvent*
##### 说明: 有关好友的事件
##### Event 对象成员：


### FriendRemarkChangeEvent 
##### 说明: 好友昵称改变事件
##### Event 对象成员：


| 成员名  | 成员类型                    | 描述           |
| ------- | --------------------------- | -------------- |
| bot     |  [Bot](./bot.md)       | 来源 Bot 对象    |
| friend  | [Friend](./contact.md#好友-friend) | 来源 Friend 对象 |
| newName | string                | 新名字         |

### FriendAddEvent 
##### 说明: 成功添加了一个新好友的事件
##### Event 对象成员：


| 成员名  | 成员类型                    | 描述           |
| ------- | --------------------------- | -------------- |
| bot     |  [Bot](./bot.md)       | 来源 Bot 对象    |
| friend  | [Friend](./contact.md#好友-friend) | 来源 Friend 对象 |

### FriendDeleteEvent 
##### 说明: 好友已被删除的事件
##### Event 对象成员：


| 成员名  | 成员类型                    | 描述           |
| ------- | --------------------------- | -------------- |
| bot     |  [Bot](./bot.md)       | 来源 Bot 对象    |
| friend  | [Friend](./contact.md#好友-friend) | 来源 Friend 对象 |

### FriendAvatarChangedEvent 
##### 说明: 头像被修改. 在此事件广播前就已经修改完毕.
##### Event 对象成员：


| 成员名  | 成员类型                    | 描述           |
| ------- | --------------------------- | -------------- |
| bot     |  [Bot](./bot.md)       | 来源 Bot 对象    |
| friend  | [Friend](./contact.md#好友-friend) | 来源 Friend 对象 |

### NewFriendRequestEvent 
##### 说明: 一个账号请求添加机器人为好友的事件
##### Event 对象成员：


| 成员名 | 成员类型                      | 描述                            |
| ------ | ----------------------------- | ------------------------------- |
| bot    |  [Bot](./bot.md)         | 来源 Bot 对象                     |
| group  | [Group](./contact.md#群-group)     | 来源 Group 对象(nil 则为其他途径) |
| friend | [Friend](./contact.md#好友-friend)   | 来源 Friend 对象  |
| msg    | [Message](../guide/message.md) | 申请消息                        |

### MessagePreSendEvent*
##### 说明: 在发送消息前广播的事件
##### Event 对象成员：


| 成员名  | 成员类型                                   | 描述                                      |
| ------- | ------------------------------------------ | ----------------------------------------- |
| bot     |  [Bot](./bot.md)         | 来源 Bot 对象                             |
| contact | [Contact](./contact.md) | 来源 Contact 对象(可能是好友也可能是群聊) |
| message | [Message](../guide/message.md) | 发送的消息                                |

### GroupMessagePreSendEvent 
##### 说明: 在发送群消息前广播的事件
##### Event 对象成员：


| 成员名  | 成员类型                                          | 描述            |
| ------- | ------------------------------------------------- | --------------- |
| bot     |  [Bot](./bot.md)                | 来源 Bot 对象   |
| target  | [Group](./contact.md#群-group) | 来源 Group 对象 |
| message | [Message](../guide/message.md)        | 发送的消息      |


### UserMessagePreSendEvent 
##### 说明: 在发送好友或群临时会话消息前广播的事件
##### Event 对象成员：


| 成员名 | 成员类型                      | 描述                                    |
| ------ | ----------------------------- | --------------------------------------- |
| bot    |  [Bot](./bot.md)         | 来源 Bot 对象                             |
| reciver  | [Friend](./contact.md#好友-friend)| 来源 Friend 对象(可能是好友也可能是临时) |
| message | [Message](../guide/message.md) | 发送的消息                              |


### TempMessagePreSendEvent 
##### 说明: 在发送群临时会话消息前广播的事件
##### Event 对象成员：


| 成员名  | 成员类型                                                | 描述             |
| ------- | ------------------------------------------------------- | ---------------- |
| bot     |  [Bot](./bot.md)                      | 来源 Bot 对象    |
| member  | [Member](./contact.md#群成员-member) | 来源 Member 对象 |
| message | [Message](../guide/message.md)              | 发送的消息       |


### FriendMessagePreSendEvent 
##### 说明: 在发送好友消息前广播的事件
##### Event 对象成员：

| 成员名  | 成员类型                                           | 描述             |
| ------- | -------------------------------------------------- | ---------------- |
| bot     |  [Bot](./bot.md)                 | 来源 Bot 对象    |
| friend  | [Friend](./contact.md#群-group) | 来源 Friend 对象 |
| message | [Message](../guide/message.md)         | 发送的消息       |

### MessagePostSendEvent*
##### 说明: 在发送消息后广播的事件
##### Event 对象成员：


### GroupMessagePostSendEvent 
##### 说明: 在群消息发送后广播的事件
##### Event 对象成员：

| 成员名  | 成员类型                                          | 描述            |
| ------- | ------------------------------------------------- | --------------- |
| bot     |  [Bot](./bot.md)                | 来源 Bot 对象   |
| target  | [Group](./contact.md#群-group) | 来源 Group 对象 |
| message | [Message](../guide/message.md)        | 发送的消息      |


### UserMessagePostSendEvent 
##### 说明: 在好友或群临时会话消息发送后广播的事件
##### Event 对象成员：


### TempMessagePostSendEvent 
##### 说明: 在群临时会话消息发送后广播的事件
##### Event 对象成员：


### MessageRecallEvent*
##### 说明: 消息撤回事件
##### Event 对象成员：


| 成员名 | 成员类型               | 描述                          |
| ------ | ---------------------- | ----------------------------- |
| bot    |  [Bot](./bot.md)  | 来源 Bot 对象                   |
| author/operator | [Contact](./contact.md) | 来源 Contact 对象（来自任何人） |
| source | [Message](../guide/message.md) | 原消息                        |

### FriendRecall 
##### 说明: 好友消息撤回事件
##### Event 对象成员：


| 成员名 | 成员类型               | 描述             |
| ------ | ---------------------- | ---------------- |
| bot    |  [Bot](./bot.md)  | 来源 Bot 对象      |
| author/operator | [Friend](./contact.md#好友-friend)  | 来源 Friend 对象 |


### GroupRecall 
##### 说明: 群消息撤回事件
##### Event 对象成员：


| 成员名 | 成员类型                    | 描述             |
| ------ | --------------------------- | ---------------- |
| bot    |  [Bot](./bot.md)       | 来源 Bot 对象      |
| author/operator | [Member](./contact.md#群成员-member) | 来源 Member 对象 |


### BeforeImageUploadEvent 
##### 说明: 图片上传前. 可以阻止上传
##### Event 对象成员：


| 成员名 | 成员类型                    | 描述             |
| ------ | --------------------------- | ---------------- |
| bot    |  [Bot](./bot.md)       | 来源 Bot 对象      |
| member | [Member](./contact.md#群成员-member) | 来源 Member 对象 |
| image  |              | 图片             |


### ImageUploadEvent 
##### 说明: 图片上传完成
##### Event 对象成员：


| 成员名 | 成员类型                    | 描述             |
| ------ | --------------------------- | ---------------- |
| bot    |  [Bot](./bot.md)       | 来源 Bot 对象      |
| member | [Member](./contact.md#群成员-member) | 来源 Member 对象 |
| image  |              | 图片             |

### ImageUploadEvent.Succeed 
##### 说明: 图片上传成功
##### Event 对象成员：


| 成员名 | 成员类型                    | 描述             |
| ------ | --------------------------- | ---------------- |
| bot    |  [Bot](./bot.md)       | 来源 Bot 对象      |
| member | [Member](./contact.md#群成员-member) | 来源 Member 对象 |
| image  |              | 图片             |

### ImageUploadEvent.Failed 
##### 说明: 图片上传失败
##### Event 对象成员：


| 成员名 | 成员类型                    | 描述             |
| ------ | --------------------------- | ---------------- |
| bot    |  [Bot](./bot.md)       | 来源 Bot 对象      |
| member | [Member](./contact.md#群成员-member) | 来源 Member 对象 |
| image  |              | 图片             |

## 部分未知事件类型

### GetMsgSuccess

### ConfigPushSvc.PushReq.PushReqResponse.ConfigPush

### ConfigPushSvc.PushReq.PushReqResponse.ServerListPush





