package com.ooooonly.luaMirai.lua

import com.ooooonly.luaMirai.utils.*
import kotlinx.coroutines.CompletableJob
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import net.mamoe.mirai.*
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.event.subscribeAlways
import net.mamoe.mirai.message.FriendMessageEvent
import net.mamoe.mirai.message.GroupMessageEvent
import net.mamoe.mirai.utils.BotConfiguration
import org.luaj.vm2.LuaError
import org.luaj.vm2.LuaFunction
import org.luaj.vm2.LuaTable
import org.luaj.vm2.LuaValue

class MiraiBot : LuaBot {
    var bot: Bot
    var scriptId: Int

    companion object {
        var listeners: HashMap<Int, HashMap<Int, CompletableJob>> = HashMap()
    }

    constructor(
        account: Long,
        password: String,
        scriptId: Int,
        config: BotConfiguration = BotConfiguration.Default
    ) : super(account, password) {
        this.scriptId = scriptId
        this.bot = try {
            Bot.getInstance(account)
        } catch (e: Exception) {
            Bot(account, password, config)
        }
    }

    constructor(bot: Bot, scriptId: Int) : super(bot.id, "") {
        this.scriptId = scriptId
        this.bot = bot
    }

    private inline fun <reified E : BotEvent> Bot.subscribeLuaFunction(
        luaFun: LuaFunction,
        crossinline callback: (LuaValue, Event) -> Unit, // （返回值，Event对象） 处理返回结果
        crossinline map: E.(E) -> Array<LuaValue> //转换器，将Event对象参数转换成LuaValue数组，以便传入LuaFunction
    ): CompletableJob = subscribeAlways<E> {
        val varargs = LuaValue.varargsOf(it.map(it))
        val exeResult = luaFun.invoke(varargs)
        callback(exeResult.arg1(), it)
    }

    override fun getSubscribeFunction(opcode: Int): SubscribeFunction = object : SubscribeFunction(opcode) {
        override fun onSubscribe(self: LuaValue, listener: LuaFunction): LuaValue = self.also {
            if (self !is MiraiBot) throw LuaError("The reference object must be MiraiBot")

            listeners[self.scriptId]?.get(opcode)?.complete() ?: run { listeners[self.scriptId] = HashMap() }


            val process = { lv: LuaValue, e: Event -> if (lv != LuaValue.NIL) e.intercept() }
            when (opcode) {
                EVENT_MSG_FRIEND -> self.bot.subscribeLuaFunction<FriendMessageEvent>(listener, process) {
                    arrayOf(self, MiraiMsg(it.message, it.bot), MiraiFriend(self, it.sender))
                }
                EVENT_MSG_GROUP -> self.bot.subscribeLuaFunction<GroupMessageEvent>(listener, process) {
                    var g = MiraiGroup(self, it.group)
                    arrayOf(self, MiraiMsg(it.message, self.bot), g, MiraiGroupMember(self, g, it.sender))
                }
                /*
                EVENT_MSG_SEND_FRIEND -> self.bot.subscribeLuaFunction<MessagePreSendEvent>(
                    listener,
                    process
                ) {
                    arrayOf(self, MiraiMsg(it.message, it.bot), MiraiFriend(self, it.target))
                }
                EVENT_MSG_SEND_GROUP -> self.bot.subscribeLuaFunction<MessageSendEvent.GroupMessageSendEvent>(
                    listener,
                    process
                ) {
                    arrayOf(self, MiraiMsg(it.message, self.bot), MiraiGroup(self, it.target))
                }
                */
                EVENT_BOT_ONLINE -> self.bot.subscribeLuaFunction<BotOnlineEvent>(listener, process) {
                    arrayOf(self)
                }
                EVENT_BOT_OFFLINE -> self.bot.subscribeLuaFunction<BotOfflineEvent>(listener, process) {
                    arrayOf(self)
                }
                EVENT_BOT_RE_LOGIN -> self.bot.subscribeLuaFunction<BotReloginEvent>(listener, process) {
                    arrayOf(self)
                }
                EVENT_BOT_CHANGE_GROUP_PERMISSION -> self.bot.subscribeLuaFunction<BotGroupPermissionChangeEvent>(
                    listener,
                    process
                ) {
                    arrayOf(
                        self,
                        MiraiGroup(self, it.group),
                        LuaValue.valueOf(it.new.name),
                        LuaValue.valueOf(it.origin.name)
                    )
                }
                EVENT_BOT_MUTED -> self.bot.subscribeLuaFunction<BotMuteEvent>(listener, process) {
                    arrayOf(self, MiraiGroup(self, it.group))
                }
                EVENT_BOT_JOIN_GROUP -> self.bot.subscribeLuaFunction<BotJoinGroupEvent>(listener, process) {
                    arrayOf(self, MiraiGroup(self, it.group))
                }
                /*EVENT_BOT_KICKED -> self.bot.subscribeLuaFunction<>(listener, process) {
                    arrayOf(self, MiraiGroup(self,it.group),it.)
                }*/
                EVENT_BOT_LEAVE -> self.bot.subscribeLuaFunction<BotLeaveEvent>(listener, process) {
                    arrayOf(self, MiraiGroup(self, it.group))
                }

                EVENT_GROUP_CHANGE_NAME -> self.bot.subscribeLuaFunction<GroupNameChangeEvent>(listener, process) {
                    var g = MiraiGroup(self, it.group)
                    arrayOf(
                        self,
                        g,
                        MiraiGroupMember(self, g, it.operatorOrBot),
                        LuaValue.valueOf(it.new),
                        LuaValue.valueOf(it.origin)
                    )
                }
                /*EVENT_GROUP_CHANGE_SETTING -> self.bot.subscribeLuaFunction<GroupSettingChangeEvent>(listener, process) {
                    arrayOf(self, MiraiGroup(self,it.group))
                }*/
                EVENT_GROUP_CHANGE_ENTRANCE_ANNOUNCEMENT -> self.bot.subscribeLuaFunction<GroupEntranceAnnouncementChangeEvent>(
                    listener,
                    process
                ) {
                    arrayOf(self, MiraiGroup(self, it.group))
                }
                EVENT_GROUP_CHANGE_ALLOW_CONFESS_TALK -> self.bot.subscribeLuaFunction<GroupAllowConfessTalkEvent>(
                    listener,
                    process
                ) {
                    arrayOf(self, MiraiGroup(self, it.group))
                }
                EVENT_GROUP_CHANGE_ALLOW_MEMBER_INVITE -> self.bot.subscribeLuaFunction<GroupAllowConfessTalkEvent>(
                    listener,
                    process
                ) {
                    arrayOf(self, MiraiGroup(self, it.group))
                }
                EVENT_GROUP_REQUEST -> self.bot.subscribeLuaFunction<BotInvitedJoinGroupRequestEvent>(
                    listener,
                    process
                ) {

                    arrayOf(self, LuaValue.valueOf(it.groupId.toInt()))
                }
                EVENT_GROUP_MEMBER_JOIN -> self.bot.subscribeLuaFunction<MemberJoinEvent>(listener, process) {
                    var g = MiraiGroup(self, it.group)
                    arrayOf(self, g, MiraiGroupMember(self, g, it.member))
                }
                EVENT_GROUP_MEMBER_JOIN_REQUEST -> self.bot.subscribeLuaFunction<MemberJoinRequestEvent>(
                    listener,
                    process
                ) {
                    var g = MiraiGroup(self, it.group)
                    arrayOf(
                        self,
                        g,
                        LuaValue.valueOf(it.eventId.toString()),
                        LuaValue.valueOf(it.fromId.toString()),
                        LuaValue.valueOf(it.message),
                        LuaTable().apply {
                            setFunction0Arg0Return("accept") {
                                kotlinx.coroutines.runBlocking { accept() }
                            }
                            setFunction1ArgNoReturn("ignore") { blackList ->
                                kotlinx.coroutines.runBlocking { ignore(blackList.optboolean(false)) }
                            }
                            setFunction2ArgNoReturn("reject") { blackList, msg ->
                                kotlinx.coroutines.runBlocking {
                                    reject(
                                        blackList.optboolean(false),
                                        msg.optjstring("")
                                    )
                                }
                            }
                        }
                    )
                }
                EVENT_GROUP_MEMBER_LEAVE -> self.bot.subscribeLuaFunction<MemberLeaveEvent>(listener, process) {
                    var g = MiraiGroup(self, it.group)

                    arrayOf(self, g, MiraiGroupMember(self, g, it.member))
                }

                EVENT_GROUP_MEMBER_CHANGE_CARD -> self.bot.subscribeLuaFunction<MemberCardChangeEvent>(
                    listener,
                    process
                ) {
                    var g = MiraiGroup(self, it.group)
                    arrayOf(
                        self,
                        g,
                        MiraiGroupMember(self, g, it.member),
                        LuaValue.valueOf(it.new),
                        LuaValue.valueOf(it.origin)
                    )
                }
                EVENT_GROUP_MEMBER_CHANGE_SPECIAL_TITLE -> self.bot.subscribeLuaFunction<MemberSpecialTitleChangeEvent>(
                    listener,
                    process
                ) {
                    var g = MiraiGroup(self, it.group)
                    arrayOf(
                        self,
                        g,
                        MiraiGroupMember(self, g, it.member),
                        LuaValue.valueOf(it.new),
                        LuaValue.valueOf(it.origin)
                    )
                }
                EVENT_GROUP_MEMBER_CHANGE_PERMISSION -> self.bot.subscribeLuaFunction<MemberPermissionChangeEvent>(
                    listener,
                    process
                ) {
                    var g = MiraiGroup(self, it.group)
                    arrayOf(
                        self,
                        g,
                        MiraiGroupMember(self, g, it.member),
                        LuaValue.valueOf(it.new.name),
                        LuaValue.valueOf(it.origin.name)
                    )
                }
                EVENT_GROUP_MEMBER_MUTED -> self.bot.subscribeLuaFunction<MemberMuteEvent>(listener, process) {
                    var g = MiraiGroup(self, it.group)
                    arrayOf(self, g, MiraiGroupMember(self, g, it.member), LuaValue.valueOf(it.durationSeconds))
                }
                EVENT_GROUP_MEMBER_UN_MUTED -> self.bot.subscribeLuaFunction<MemberUnmuteEvent>(listener, process) {
                    var g = MiraiGroup(self, it.group)
                    arrayOf(self, g, MiraiGroupMember(self, g, it.member))
                }

                EVENT_FRIEND_CHANGE_REMARK -> self.bot.subscribeLuaFunction<FriendRemarkChangeEvent>(
                    listener,
                    process
                ) {
                    arrayOf(self, MiraiFriend(self, it.friend), LuaValue.valueOf(it.newName))
                }
                EVENT_FRIEND_ADDED -> self.bot.subscribeLuaFunction<FriendAddEvent>(listener, process) {
                    arrayOf(self, MiraiFriend(self, it.friend))
                }
                EVENT_FRIEND_DELETE -> self.bot.subscribeLuaFunction<FriendDeleteEvent>(listener, process) {
                    arrayOf(self, MiraiFriend(self, it.friend))
                }
                EVENT_FRIEND_REQUEST -> self.bot.subscribeLuaFunction<NewFriendRequestEvent>(listener, process) {
                    it.fromGroup?.let { g ->
                        arrayOf(
                            self,
                            LuaValue.valueOf(it.eventId.toInt()),
                            MiraiGroup(self, g),
                            LuaValue.valueOf(it.message),
                            LuaValue.valueOf(it.fromId.toInt()),
                            LuaValue.valueOf(it.fromNick)
                        )
                    } ?: arrayOf(
                        self,
                        LuaValue.valueOf(it.eventId.toInt()),
                        LuaValue.valueOf(it.message),
                        LuaValue.valueOf(it.fromId.toString()),
                        LuaValue.valueOf(it.fromNick),
                        LuaTable().apply {
                            setFunction0Arg0Return("accept") {
                                kotlinx.coroutines.runBlocking { accept() }
                            }
                            setFunction2ArgNoReturn("reject") { blackList, msg ->
                                kotlinx.coroutines.runBlocking { reject(blackList.optboolean(false)) }
                            }
                        }
                    )
                }

                else -> null
            }?.let { listeners[self.scriptId]?.set(opcode, it) }

        }
    }

    override fun getOpFunction(opcode: Int): OpFunction = generateOpFunction(opcode) { op, varargs ->
        varargs.checkArg<MiraiBot>(1).let {
            when (opcode) {
                LOGIN -> it.also { it.bot.alsoLogin() }
                JOIN -> it.also { it.bot.join() }
                CLOSE_AND_JOIN -> it.also { it.bot.closeAndJoin() }
                GET_FRIEND -> MiraiFriend(it, varargs.optlong(2, 0))
                GET_GROUP -> MiraiGroup(it, varargs.optlong(2, 0))
                GET_FRIENDS -> LuaTable().apply {
                    var i: Int = 0
                    it.bot.friends.forEach { friend ->
                        this.insert(i++, MiraiFriend(it, friend))
                    }
                }
                GET_GROUPS -> LuaTable().apply {
                    var i: Int = 0
                    it.bot.groups.forEach { group ->
                        this.insert(i++, MiraiGroup(it, group))
                    }
                }
                GET_SELF_QQ -> MiraiFriend(it, it.bot.selfQQ)
                GET_ID -> it.bot.id.toLuaValue()
                CONTAINS_FRIEND -> it.bot.containsFriend(varargs.optlong(2, 0)).toLuaValue()
                CONTAINS_GROUP -> it.bot.containsGroup(varargs.optlong(2, 0)).toLuaValue()
                IS_ACTIVE -> it.bot.isActive.toLuaValue()
                //ADD_FRIEND ->it.bot.
                LAUNCH -> LuaCoroutineJob(it.bot.launch { varargs.arg(2).checkIfType<LuaFunction>().call() })
                else -> LuaValue.NIL
            }
        }
    }

    fun unSubsribeAll() = listeners[scriptId]?.forEach { it.value.complete() }
}