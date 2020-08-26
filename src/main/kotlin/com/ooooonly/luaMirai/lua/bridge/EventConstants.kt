package com.ooooonly.luaMirai.lua.bridge

import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.message.FriendMessageEvent
import net.mamoe.mirai.message.GroupMessageEvent
import kotlin.reflect.KClass

object EventConstants {
    val events by lazy {
        val map = mutableMapOf<String, KClass<out Event>>()
        map.apply {
            set("Event", Event::class)  //事件
            set("BotEvent", BotEvent::class)  //有关一个 [Bot] 的事件
            set("BotPassiveEvent", BotPassiveEvent::class)  //被动接收的事件. 这些事件可能与机器人有关
            set("BotActiveEvent", BotActiveEvent::class)  //主动发起的动作的事件
            set("BotOnlineEvent", BotOnlineEvent::class)  //登录完成, 好友列表, 群组列表初始化完成
            set("BotOfflineEvent", BotOfflineEvent::class)  //离线
            set("BotOfflineEvent.Active", BotOfflineEvent.Active::class)  //主动离线
            set("BotOfflineEvent.Force", BotOfflineEvent.Force::class)  //被挤下线
            set("BotOfflineEvent.MsfOffline", BotOfflineEvent.MsfOffline::class)  //被服务器断开
            set("BotOfflineEvent.Dropped", BotOfflineEvent.Dropped::class)  //因网络问题而掉线
            set("BotOfflineEvent.RequireReconnect", BotOfflineEvent.RequireReconnect::class)  //服务器主动要求更换另一个服务器
            set("BotReloginEvent", BotReloginEvent::class)  //主动或被动重新登录. 在此事件广播前就已经登录完毕.
            set("BotAvatarChangedEvent", BotAvatarChangedEvent::class)  //头像被修改（通过其他客户端修改了头像）
            set("GroupEvent", GroupEvent::class)  //有关群的事件
            set("GroupMemberEvent", GroupMemberEvent::class)  //有关群成员的事件
            set("MemberJoinEvent", MemberJoinEvent::class)  //成员已经加入群的事件
            set("MemberJoinEvent.Invite", MemberJoinEvent.Invite::class)  //被邀请加入群
            set("MemberJoinEvent.Active", MemberJoinEvent.Active::class)  //成员主动加入群
            set("MemberLeaveEvent", MemberLeaveEvent::class)  //成员已经离开群的事件
            set("MemberLeaveEvent.Kick", MemberLeaveEvent.Kick::class)  //成员被踢出群. 成员不可能是机器人自己.
            set("MemberLeaveEvent.Quit", MemberLeaveEvent.Quit::class)  //成员主动离开
            set("MemberCardChangeEvent", MemberCardChangeEvent::class)  //成员群名片改动. 此事件广播前修改就已经完成.
            set("MemberSpecialTitleChangeEvent", MemberSpecialTitleChangeEvent::class)  //成员群头衔改动. 一定为群主操作
            set("MemberPermissionChangeEvent", MemberPermissionChangeEvent::class)  //成员权限改变的事件. 成员不可能是机器人自己.
            set("MemberMuteEvent", MemberMuteEvent::class)  //群成员被禁言事件. 被禁言的成员都不可能是机器人本人
            set("MemberUnmuteEvent", MemberUnmuteEvent::class)  //群成员被取消禁言事件. 被禁言的成员都不可能是机器人本人
            set("GroupOperableEvent", GroupOperableEvent::class)  //可由 [Member] 或 [Bot] 操作的事件
            set("MemberSpecialTitleChangeEvent", MemberSpecialTitleChangeEvent::class)  //成员群头衔改动. 一定为群主操作
            //set("GroupSettingChangeEvent<T>",GroupSettingChangeEvent<*>::class)  //群设置改变. 此事件广播前修改就已经完成.
            set("GroupNameChangeEvent", GroupNameChangeEvent::class)  //群名改变. 此事件广播前修改就已经完成.
            set(
                "GroupEntranceAnnouncementChangeEvent",
                GroupEntranceAnnouncementChangeEvent::class
            )  //入群公告改变. 此事件广播前修改就已经完成.
            set("GroupMuteAllEvent", GroupMuteAllEvent::class)  //群 "全员禁言" 功能状态改变. 此事件广播前修改就已经完成.
            set("GroupAllowAnonymousChatEvent", GroupAllowAnonymousChatEvent::class)  //群 "匿名聊天" 功能状态改变. 此事件广播前修改就已经完成.
            set("GroupAllowConfessTalkEvent", GroupAllowConfessTalkEvent::class)  //群 "坦白说" 功能状态改变. 此事件广播前修改就已经完成.
            set(
                "GroupAllowMemberInviteEvent",
                GroupAllowMemberInviteEvent::class
            )  //群 "允许群员邀请好友加群" 功能状态改变. 此事件广播前修改就已经完成.
            set("BotGroupPermissionChangeEvent", BotGroupPermissionChangeEvent::class)  //Bot 在群里的权限被改变. 操作人一定是群主
            set("BotMuteEvent", BotMuteEvent::class)  //Bot 被禁言
            set("BotUnmuteEvent", BotUnmuteEvent::class)  //Bot 被取消禁言
            set("BotJoinGroupEvent", BotJoinGroupEvent::class)  //Bot 成功加入了一个新群
            set("BotJoinGroupEvent.Active", BotJoinGroupEvent.Active::class)  //不确定. 可能是主动加入
            set("BotJoinGroupEvent.Invite", BotJoinGroupEvent.Invite::class)  //Bot 被一个群内的成员直接邀请加入了群
            set("GroupSettingChangeEvent", GroupSettingChangeEvent::class)  //群设置改变. 此事件广播前修改就已经完成.
            set("BotInvitedJoinGroupRequestEvent", BotInvitedJoinGroupRequestEvent::class)  //[Bot] 被邀请加入一个群
            set("MemberJoinRequestEvent", MemberJoinRequestEvent::class)  //一个账号请求加入群事件, [Bot] 在此群中是管理员或群主.
            set("BotLeaveEvent", BotLeaveEvent::class)  //机器人被踢出群或在其他客户端主动退出一个群
            set("BotLeaveEvent.Active", BotLeaveEvent.Active::class)  //机器人主动退出一个群
            set("BotLeaveEvent.Kick", BotLeaveEvent.Kick::class)  //机器人被管理员或群主踢出群
            set("FriendEvent", FriendEvent::class)  //有关好友的事件
            set("FriendRemarkChangeEvent", FriendRemarkChangeEvent::class)  //好友昵称改变事件
            set("FriendAddEvent", FriendAddEvent::class)  //成功添加了一个新好友的事件
            set("FriendDeleteEvent", FriendDeleteEvent::class)  //好友已被删除的事件
            set("FriendAvatarChangedEvent", FriendAvatarChangedEvent::class)  //头像被修改. 在此事件广播前就已经修改完毕.
            set("NewFriendRequestEvent", NewFriendRequestEvent::class)  //一个账号请求添加机器人为好友的事件
            set("MessagePreSendEvent", MessagePreSendEvent::class)  //在发送消息前广播的事件
            set("GroupMessagePreSendEvent", GroupMessagePreSendEvent::class)  //在发送群消息前广播的事件
            set("UserMessagePreSendEvent", UserMessagePreSendEvent::class)  //在发送好友或群临时会话消息前广播的事件
            set("TempMessagePreSendEvent", TempMessagePreSendEvent::class)  //在发送群临时会话消息前广播的事件
            set("FriendMessagePreSendEvent", FriendMessagePreSendEvent::class)  //在发送好友消息前广播的事件
            set("MessagePostSendEvent", MessagePostSendEvent::class)  //在发送消息后广播的事件
            set("GroupMessagePostSendEvent", GroupMessagePostSendEvent::class)  //在群消息发送后广播的事件
            set("UserMessagePostSendEvent", UserMessagePostSendEvent::class)  //在好友或群临时会话消息发送后广播的事件
            set("TempMessagePostSendEvent", TempMessagePostSendEvent::class)  //在群临时会话消息发送后广播的事件
            set("MessageRecallEvent", MessageRecallEvent::class)  //消息撤回事件
            set("MessageRecallEvent.FriendRecall", MessageRecallEvent.FriendRecall::class)  //好友消息撤回事件
            set("MessageRecallEvent.GroupRecall", MessageRecallEvent.GroupRecall::class)  //群消息撤回事件
            set("BeforeImageUploadEvent", BeforeImageUploadEvent::class)  //图片上传前. 可以阻止上传
            set("ImageUploadEvent", ImageUploadEvent::class)  //图片上传完成
            set("ImageUploadEvent.Succeed", ImageUploadEvent.Succeed::class)  //图片上传成功
            set("ImageUploadEvent.Failed", ImageUploadEvent.Failed::class)  //图片上传失败
            set("FriendMessageEvent", FriendMessageEvent::class)  //机器人收到的好友消息的事件
            set("GroupMessageEvent", GroupMessageEvent::class)  //机器人收到的群消息的事件
        }
    }
}