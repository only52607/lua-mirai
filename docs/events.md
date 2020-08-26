# 事件

lua-mirai基于事件驱动机制构建，lua-mirai中的事件包括好友、群消息事件等，当事件发生时，lua-mirai将会通过回调函数通知监听者。



## 事件监听

一个事件监听是由[`Bot`](/docs/bot.md)对象创建的，下面是一个订阅示例：

``` lua
function listener(event)
    print(event)
end

bot:subscribe("FriendMessageEvent",listener)
```

其中"FriendMessageEvent"字符串标识了一个指定事件，当事件发生时，会通过第二个参数传入的函数闭包进行回调，这个函数闭包接受一个Event对象，其中包含了机器人对象，发送者，消息等信息。



### 如何手动查看Event对象内的成员

``` lua
function listener(event)
    print(event)
end

bot:subscribe("FriendMessageEvent",listener)
```

以上是一个好友消息的订阅，当收到好友消息时，控制台会打印以下内容。

> FriendMessageEvent(sender=123456, message=xxxxx)

其中sender和message代表了Event对象内的成员。

关于所有的事件类型和对应的Event对象成员，已在下文中列出。

## 支持的所有事件类型

### Event 
##### 说明:事件
##### Event对象成员：
<br />

### BotEvent 
##### 说明:有关一个 [Bot] 的事件
##### Event对象成员：
<br />
### BotPassiveEvent 
##### 说明:被动接收的事件. 这些事件可能与机器人有关
##### Event对象成员：
<br />
### 
##### 说明:略
##### Event对象成员：
<br />
### BotActiveEvent 
##### 说明:主动发起的动作的事件
##### Event对象成员：
<br />
### BotOnlineEvent 
##### 说明:登录完成, 好友列表, 群组列表初始化完成
##### Event对象成员：
<br />
### BotOfflineEvent 
##### 说明:离线
##### Event对象成员：
<br />
### BotOfflineEvent.Active 
##### 说明:主动离线
##### Event对象成员：
<br />
### BotOfflineEvent.Force 
##### 说明:被挤下线
##### Event对象成员：
<br />
### BotOfflineEvent.MsfOffline 
##### 说明:被服务器断开
##### Event对象成员：
<br />
### BotOfflineEvent.Dropped 
##### 说明:因网络问题而掉线
##### Event对象成员：
<br />
### BotOfflineEvent.RequireReconnect 
##### 说明:服务器主动要求更换另一个服务器
##### Event对象成员：
<br />
### BotReloginEvent 
##### 说明:主动或被动重新登录. 在此事件广播前就已经登录完毕.
##### Event对象成员：
<br />
### BotAvatarChangedEvent 
##### 说明:头像被修改（通过其他客户端修改了头像）
##### Event对象成员：
<br />
### GroupEvent 
##### 说明:有关群的事件
##### Event对象成员：
<br />
### GroupMemberEvent 
##### 说明:有关群成员的事件
##### Event对象成员：
<br />
### MemberJoinEvent 
##### 说明:成员已经加入群的事件
##### Event对象成员：
<br />
### MemberJoinEvent.Invite 
##### 说明:被邀请加入群
##### Event对象成员：
<br />
### MemberJoinEvent.Active 
##### 说明:成员主动加入群
##### Event对象成员：
<br />
### MemberLeaveEvent 
##### 说明:成员已经离开群的事件
##### Event对象成员：
<br />
### MemberLeaveEvent.Kick 
##### 说明:成员被踢出群. 成员不可能是机器人自己.
##### Event对象成员：
<br />
### MemberLeaveEvent.Quit 
##### 说明:成员主动离开
##### Event对象成员：
<br />
### MemberCardChangeEvent 
##### 说明:成员群名片改动. 此事件广播前修改就已经完成.
##### Event对象成员：
<br />
### MemberSpecialTitleChangeEvent 
##### 说明:成员群头衔改动. 一定为群主操作
##### Event对象成员：
<br />
### MemberPermissionChangeEvent 
##### 说明:成员权限改变的事件. 成员不可能是机器人自己.
##### Event对象成员：
<br />
### MemberMuteEvent 
##### 说明:群成员被禁言事件. 被禁言的成员都不可能是机器人本人
##### Event对象成员：
<br />
### MemberUnmuteEvent 
##### 说明:群成员被取消禁言事件. 被禁言的成员都不可能是机器人本人
##### Event对象成员：
<br />
### GroupOperableEvent 
##### 说明:可由 [Member] 或 [Bot] 操作的事件
##### Event对象成员：
<br />
### MemberSpecialTitleChangeEvent 
##### 说明:成员群头衔改动. 一定为群主操作
##### Event对象成员：
<br />
### GroupSettingChangeEvent<T> 
##### 说明:群设置改变. 此事件广播前修改就已经完成.
##### Event对象成员：
<br />
### GroupNameChangeEvent 
##### 说明:群名改变. 此事件广播前修改就已经完成.
##### Event对象成员：
<br />
### GroupEntranceAnnouncementChangeEvent 
##### 说明:入群公告改变. 此事件广播前修改就已经完成.
##### Event对象成员：
<br />
### GroupMuteAllEvent 
##### 说明:群 "全员禁言" 功能状态改变. 此事件广播前修改就已经完成.
##### Event对象成员：
<br />
### GroupAllowAnonymousChatEvent 
##### 说明:群 "匿名聊天" 功能状态改变. 此事件广播前修改就已经完成.
##### Event对象成员：
<br />
### GroupAllowConfessTalkEvent 
##### 说明:群 "坦白说" 功能状态改变. 此事件广播前修改就已经完成.
##### Event对象成员：
<br />
### GroupAllowMemberInviteEvent 
##### 说明:群 "允许群员邀请好友加群" 功能状态改变. 此事件广播前修改就已经完成.
##### Event对象成员：
<br />
### BotGroupPermissionChangeEvent 
##### 说明:Bot 在群里的权限被改变. 操作人一定是群主
##### Event对象成员：
<br />
### BotMuteEvent 
##### 说明:Bot 被禁言
##### Event对象成员：
<br />
### BotUnmuteEvent 
##### 说明:Bot 被取消禁言
##### Event对象成员：
<br />
### BotJoinGroupEvent 
##### 说明:Bot 成功加入了一个新群
##### Event对象成员：
<br />
### BotJoinGroupEvent.Active 
##### 说明:不确定. 可能是主动加入
##### Event对象成员：
<br />
### BotJoinGroupEvent.Invite 
##### 说明:Bot 被一个群内的成员直接邀请加入了群
##### Event对象成员：
<br />
### GroupSettingChangeEvent 
##### 说明:群设置改变. 此事件广播前修改就已经完成.        
##### Event对象成员：
<br />
### BotInvitedJoinGroupRequestEvent 
##### 说明:[Bot] 被邀请加入一个群
##### Event对象成员：
<br />
### MemberJoinRequestEvent 
##### 说明:一个账号请求加入群事件, [Bot] 在此群中是管理员或群主.
##### Event对象成员：
<br />
### BotLeaveEvent 
##### 说明:机器人被踢出群或在其他客户端主动退出一个群
##### Event对象成员：
<br />
### BotLeaveEvent.Active 
##### 说明:机器人主动退出一个群
##### Event对象成员：
<br />
### BotLeaveEvent.Kick 
##### 说明:机器人被管理员或群主踢出群
##### Event对象成员：
<br />
### FriendEvent 
##### 说明:有关好友的事件
##### Event对象成员：
<br />
### FriendRemarkChangeEvent 
##### 说明:好友昵称改变事件
##### Event对象成员：
<br />
### FriendAddEvent 
##### 说明:成功添加了一个新好友的事件
##### Event对象成员：
<br />
### FriendDeleteEvent 
##### 说明:好友已被删除的事件
##### Event对象成员：
<br />
### FriendAvatarChangedEvent 
##### 说明:头像被修改. 在此事件广播前就已经修改完毕.
##### Event对象成员：
<br />
### NewFriendRequestEvent 
##### 说明:一个账号请求添加机器人为好友的事件
##### Event对象成员：
<br />
### MessagePreSendEvent 
##### 说明:在发送消息前广播的事件
##### Event对象成员：
<br />
### GroupMessagePreSendEvent 
##### 说明:在发送群消息前广播的事件
##### Event对象成员：
<br />
### UserMessagePreSendEvent 
##### 说明:在发送好友或群临时会话消息前广播的事件
##### Event对象成员：
<br />

### TempMessagePreSendEvent 
##### 说明:在发送群临时会话消息前广播的事件
##### Event对象成员：
<br />
### FriendMessagePreSendEvent 
##### 说明:在发送好友消息前广播的事件
##### Event对象成员：
<br />
### MessagePostSendEvent 
##### 说明:在发送消息后广播的事件
##### Event对象成员：
<br />
### GroupMessagePostSendEvent 
##### 说明:在群消息发送后广播的事件
##### Event对象成员：
<br />
### UserMessagePostSendEvent 
##### 说明:在好友或群临时会话消息发送后广播的事件
##### Event对象成员：
<br />
### TempMessagePostSendEvent 
##### 说明:在群临时会话消息发送后广播的事件
##### Event对象成员：
<br />
### MessageRecallEvent 
##### 说明:消息撤回事件
##### Event对象成员：
<br />
### MessageRecallEvent.FriendRecall 
##### 说明:好友消息撤回事件
##### Event对象成员：
<br />
### MessageRecallEvent.GroupRecall 
##### 说明:群消息撤回事件
##### Event对象成员：
<br />
### BeforeImageUploadEvent 
##### 说明:图片上传前. 可以阻止上传
##### Event对象成员：
<br />
### ImageUploadEvent 
##### 说明:图片上传完成
##### Event对象成员：
<br />
### ImageUploadEvent.Succeed 
##### 说明:图片上传成功
##### Event对象成员：
<br />
### ImageUploadEvent.Failed 
##### 说明:图片上传失败
##### Event对象成员：
<br />
### FriendMessageEvent 
##### 说明:机器人收到的好友消息的事件
##### Event对象成员：
<br />
### GroupMessageEvent 
##### 说明:机器人收到的群消息的事件
##### Event对象成员：



