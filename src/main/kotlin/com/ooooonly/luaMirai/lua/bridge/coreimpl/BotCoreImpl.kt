package com.ooooonly.luaMirai.lua.bridge.coreimpl

import com.ooooonly.luaMirai.lua.MiraiCoreGlobals
import com.ooooonly.luaMirai.lua.MiraiCoreGlobalsManger
import com.ooooonly.luaMirai.lua.bridge.base.BaseBot
import com.ooooonly.luakt.*
import kotlinx.coroutines.*
import net.mamoe.mirai.*
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.message.FriendMessageEvent
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.message.MessageEvent
import net.mamoe.mirai.utils.BotConfiguration
import org.luaj.vm2.*

class BotCoreImpl(val host: Bot) : BaseBot() {
    override var id: Long
        get() = host.id
        set(value) {}
    override var isActive: Boolean
        get() = host.isActive
        set(value) {}
    override var isOnline: Boolean
        get() = host.isOnline
        set(value) {}

    override fun login() = also {
        runBlocking {
            host.login()
        }
    }

    companion object {
        fun setBotFactory(table: LuaTable) {
            table.set("Bot", luaFunctionOf { varargs: Varargs ->
                val user = varargs[0].asKValue<Long>()
                val pwd = varargs[1].asKValue<String>()
                val config =
                    if (varargs.narg() >= 3) BotConfiguration.Default.apply { fileBasedDeviceInfo(varargs[2].asKValue()) }
                    else BotConfiguration.Default
                return@luaFunctionOf BotCoreImpl(Bot(user, pwd, config))
            })
        }

        private val instances = mutableMapOf<Bot, BotCoreImpl>()
        fun getInstance(host: Bot): BotCoreImpl {
            if (!instances.contains(host)) instances[host] = BotCoreImpl(host)
            return instances[host]!!
        }
    }

    override fun getFriend(id: Long): FriendCoreImpl = FriendCoreImpl(host.getFriend(id))

    override fun getGroup(id: Long): GroupCoreImpl = GroupCoreImpl(host.getGroup(id))

    override fun getFriends(): Array<FriendCoreImpl> = host.friends.map { FriendCoreImpl(it) }.toTypedArray()

    override fun getGroups(): Array<GroupCoreImpl> = host.groups.map { GroupCoreImpl(it) }.toTypedArray()

    override fun launch(block: LuaClosure): Job = host.launch { block.invoke() }

    override fun containsFriend(id: Long): Boolean = host.containsFriend(id)

    override fun containsGroup(id: Long): Boolean = host.containsGroup(id)

    override fun subscribe(eventId: Int, block: LuaClosure): CompletableJob {
        //println("subscribe:$eventId scriptId:${MiraiCoreGlobals.checkScriptId(block)}")
        val job = host.subscribeAlways(eventId.toEvent()) {
            block.invoke(this.toVarargs())
        }
        MiraiCoreGlobalsManger.checkGlobals(block)?.addListener(job)
        return job
    }

    override fun join() = runBlocking {
        host.join()
    }

    override fun closeAndJoin() = runBlocking {
        host.closeAndJoin()
    }

    fun subscribeFriendMsg(block: LuaClosure) = subscribe(Events.EVENT_MSG_FRIEND, block)
    fun subscribeGroupMsg(block: LuaClosure) = subscribe(Events.EVENT_MSG_GROUP, block)
    fun subscribeFriendMsgSend(block: LuaClosure) = subscribe(Events.EVENT_MSG_SEND_FRIEND, block)
    fun subscribeGroupMsgSend(block: LuaClosure) = subscribe(Events.EVENT_MSG_SEND_GROUP, block)

    fun subscribeBotOnlineEvent(block: LuaClosure) = subscribe(Events.EVENT_BOT_ONLINE, block)
    fun subscribeBotOfflineEvent(block: LuaClosure) = subscribe(Events.EVENT_BOT_OFFLINE, block)
    fun subscribeBotReloginEvent(block: LuaClosure) = subscribe(Events.EVENT_BOT_RE_LOGIN, block)

    fun subscribeBotGroupPermissionChangeEvent(block: LuaClosure) =
        subscribe(Events.EVENT_BOT_CHANGE_GROUP_PERMISSION, block)

    fun subscribeBotMutedEvent(block: LuaClosure) = subscribe(Events.EVENT_BOT_MUTED, block)
    fun subscribeBotJoinGroupEvent(block: LuaClosure) = subscribe(Events.EVENT_BOT_JOIN_GROUP, block)
    fun subscribeBotKickedEvent(block: LuaClosure) = subscribe(Events.EVENT_BOT_KICKED, block)
    fun subscribeBotLeaveEvent(block: LuaClosure) = subscribe(Events.EVENT_BOT_LEAVE, block)

    fun subscribeGroupNameChangedEvent(block: LuaClosure) = subscribe(Events.EVENT_GROUP_CHANGE_NAME, block)
    fun subscribeGroupSettingChangedEvent(block: LuaClosure) = subscribe(Events.EVENT_GROUP_CHANGE_SETTING, block)
    fun subscribeGroupEntranceAnnouncementChangedEvent(block: LuaClosure) =
        subscribe(Events.EVENT_GROUP_CHANGE_ENTRANCE_ANNOUNCEMENT, block)

    fun subscribeAllowAnonymousChangedEvent(block: LuaClosure) =
        subscribe(Events.EVENT_GROUP_CHANGE_ALLOW_ANONYMOUS, block)

    fun subscribeAllowConfessTalkChangedEvent(block: LuaClosure) =
        subscribe(Events.EVENT_GROUP_CHANGE_ALLOW_CONFESS_TALK, block)

    fun subscribeAllowMemberInviteChangedEvent(block: LuaClosure) =
        subscribe(Events.EVENT_GROUP_CHANGE_ALLOW_MEMBER_INVITE, block)

    fun subscribeMemberJoinEvent(block: LuaClosure) = subscribe(Events.EVENT_GROUP_MEMBER_JOIN, block)
    fun subscribeMemberJoinRequestEvent(block: LuaClosure) = subscribe(Events.EVENT_GROUP_MEMBER_JOIN_REQUEST, block)
    fun subscribeMemberLeaveEvent(block: LuaClosure) = subscribe(Events.EVENT_GROUP_MEMBER_LEAVE, block)
    fun subscribeMemberCardChangedEvent(block: LuaClosure) = subscribe(Events.EVENT_GROUP_MEMBER_CHANGE_CARD, block)
    fun subscribeMemberSpecialTitleChangeEvent(block: LuaClosure) =
        subscribe(Events.EVENT_GROUP_MEMBER_CHANGE_SPECIAL_TITLE, block)

    fun subscribeMemberPermissionChangedEvent(block: LuaClosure) =
        subscribe(Events.EVENT_GROUP_MEMBER_CHANGE_PERMISSION, block)

    fun subscribeMemberMutedEvent(block: LuaClosure) = subscribe(Events.EVENT_GROUP_MEMBER_MUTED, block)
    fun subscribeMemberUnmutedEvent(block: LuaClosure) = subscribe(Events.EVENT_GROUP_MEMBER_UN_MUTED, block)

    fun subscribeFriendRemarkChangedEvent(block: LuaClosure) = subscribe(Events.EVENT_FRIEND_CHANGE_REMARK, block)
    fun subscribeFriendAddEvent(block: LuaClosure) = subscribe(Events.EVENT_FRIEND_ADDED, block)
    fun subscribeFriendDeleteEvent(block: LuaClosure) = subscribe(Events.EVENT_FRIEND_DELETE, block)
    fun subscribeFriendRequestEvent(block: LuaClosure) = subscribe(Events.EVENT_FRIEND_REQUEST, block)

    private fun Int.toEvent() = getEvent(this)
    private fun getEvent(code: Int) = when (code) {
        EVENT_MSG_FRIEND -> FriendMessageEvent::class
        EVENT_MSG_GROUP -> GroupMessageEvent::class
        EVENT_MSG_SEND_FRIEND -> FriendMessagePreSendEvent::class
        EVENT_MSG_SEND_GROUP -> GroupMessagePreSendEvent::class

        EVENT_BOT_ONLINE -> BotOnlineEvent::class
        EVENT_BOT_OFFLINE -> BotOfflineEvent::class
        EVENT_BOT_RE_LOGIN -> BotReloginEvent::class
        EVENT_BOT_CHANGE_GROUP_PERMISSION -> BotGroupPermissionChangeEvent::class
        EVENT_BOT_MUTED -> BotMuteEvent::class
        EVENT_BOT_JOIN_GROUP -> BotJoinGroupEvent::class
        EVENT_BOT_LEAVE -> BotLeaveEvent::class

        EVENT_GROUP_CHANGE_NAME -> GroupNameChangeEvent::class
        EVENT_GROUP_CHANGE_SETTING -> GroupSettingChangeEvent::class
        EVENT_GROUP_CHANGE_ENTRANCE_ANNOUNCEMENT -> GroupEntranceAnnouncementChangeEvent::class
        EVENT_GROUP_CHANGE_ALLOW_ANONYMOUS -> GroupAllowAnonymousChatEvent::class
        EVENT_GROUP_CHANGE_ALLOW_CONFESS_TALK -> GroupAllowConfessTalkEvent::class
        EVENT_GROUP_CHANGE_ALLOW_MEMBER_INVITE -> GroupAllowMemberInviteEvent::class

        EVENT_GROUP_MEMBER_JOIN -> MemberJoinEvent::class
        EVENT_GROUP_MEMBER_JOIN_REQUEST -> MemberJoinRequestEvent::class
        EVENT_GROUP_MEMBER_LEAVE -> MemberLeaveEvent::class
        EVENT_GROUP_MEMBER_CHANGE_CARD -> MemberCardChangeEvent::class
        EVENT_GROUP_MEMBER_CHANGE_SPECIAL_TITLE -> MemberSpecialTitleChangeEvent::class
        EVENT_GROUP_MEMBER_CHANGE_PERMISSION -> MemberPermissionChangeEvent::class
        EVENT_GROUP_MEMBER_MUTED -> MemberMuteEvent::class
        EVENT_GROUP_MEMBER_UN_MUTED -> MemberUnmuteEvent::class

        EVENT_FRIEND_CHANGE_REMARK -> FriendRemarkChangeEvent::class
        EVENT_FRIEND_ADDED -> FriendAddEvent::class
        EVENT_FRIEND_DELETE -> FriendDeleteEvent::class
        EVENT_FRIEND_REQUEST -> NewFriendRequestEvent::class

        else -> throw Exception("No such event class")
    }

    private fun Event.toVarargs(): Varargs {
        val args = mutableListOf<LuaValue>()
        if (this is BotEvent) args.add(getInstance(this.bot).asLuaValue())
        if (this is MessageEvent) {
            args.add(MsgCoreImpl(this.message))
            when (this) {
                is FriendMessageEvent -> args.add(FriendCoreImpl(this.sender).asLuaValue())
                is FriendMessagePostSendEvent -> args.add(FriendCoreImpl(this.target).asLuaValue())
                is GroupMessageEvent -> {
                    args.add(GroupCoreImpl(this.group).asLuaValue())
                    args.add(MemberCoreImpl(this.sender).asLuaValue())
                }
                is GroupMessagePostSendEvent -> args.add(GroupCoreImpl(this.target).asLuaValue())

            }
        }
        if (this is FriendEvent) args.add(FriendCoreImpl(this.friend).asLuaValue())
        if (this is GroupEvent) args.add(GroupCoreImpl(this.group).asLuaValue())
        if (this is GroupMemberEvent) args.add(MemberCoreImpl(this.member).asLuaValue())
        if (this is GroupOperableEvent) args.add(MemberCoreImpl(this.operatorOrBot).asLuaValue())
        when (this) {
            is GroupNameChangeEvent -> {
                args.add(this.origin.asLuaValue())
                args.add(this.new.asLuaValue())
            }
            is GroupEntranceAnnouncementChangeEvent -> {
                args.add(this.origin.asLuaValue())
                args.add(this.new.asLuaValue())
            }
            is MemberJoinRequestEvent -> {
                args.add(eventId.asLuaValue())
                args.add(fromId.asLuaValue())
                args.add(message.asLuaValue())
                luaTableOf {
                    "accept" to {
                        runBlocking { accept() }
                    }
                    "ignore" to { blackList: Boolean ->
                        runBlocking { ignore(blackList) }
                    }
                    "reject" to { blackList: Boolean, msg: String ->
                        runBlocking {
                            reject(
                                blackList,
                                msg
                            )
                        }
                    }
                }
            }
        }
        return LuaValue.varargsOf(args.toTypedArray())
    }
}