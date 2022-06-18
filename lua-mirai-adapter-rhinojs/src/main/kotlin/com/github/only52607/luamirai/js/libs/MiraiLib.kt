@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.github.only52607.luamirai.js.libs

import com.github.only52607.luamirai.js.utils.KtLambdaInterfaceBridge
import kotlinx.coroutines.*
import me.him188.kotlin.jvm.blocking.bridge.JvmBlockingBridge
import net.mamoe.mirai.console.util.cast
import net.mamoe.mirai.contact.*
import net.mamoe.mirai.event.*
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.message.data.*
import com.github.only52607.luamirai.js.JSLib
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable
import org.mozilla.javascript.ScriptableObject
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class MiraiLib : JSLib() {
    @JvmSynthetic
    override val nameInJs: String = "mirai"

    @JvmSynthetic
    override fun importTo(scope: Scriptable, context: Context) {
        ScriptableObject.putProperty(scope, "${nameInJs}Kt", Context.javaToJS(this, scope))
    }

    fun <E : Event> wrapEventChannel(eventChannel: EventChannel<E>) =
        EventChannelKtWrapper(eventChannel)

    class EventChannelKtWrapper<E : Event>(val self: EventChannel<E>) {
        //EventChannel.filter is currently not available for Java
        fun filter(samCallback: KtLambdaInterfaceBridge.SingleArgument<E, Boolean>) =
            EventChannelKtWrapper(self.filter { samCallback.call(it) })

        //EventChannel.subscribeMessages is kotlin-only function.
        @JvmOverloads
        fun <R> subscribeMessages(
            coroutineContext: CoroutineContext = EmptyCoroutineContext,
            concurrencyKind: ConcurrencyKind = ConcurrencyKind.CONCURRENT,
            priority: EventPriority = EventPriority.MONITOR,
            samCallBack: KtLambdaInterfaceBridge.SingleArgument<MessageEventSubscriberBuilderJsImpl, R>
        ): EventChannelKtWrapper<E> {
            self.subscribeMessages(coroutineContext, concurrencyKind, priority) {
                samCallBack.call(MessageEventSubscriberBuilderJsImpl(this))
            }
            return this
        }

        fun unwrap() = self

        class MessageEventSubscriberBuilderJsImpl(val self: MessageEventSubscribersBuilder) {
            //always subscribe
            fun always(
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<MessageEvent, Unit, Unit>
            ): Listener<MessageEvent> = self.always { samCallback.call(this, Unit) }

            //filter from message
            @JvmOverloads
            fun case(
                equals: String, ignoreCase: Boolean = false, trim: Boolean = true,
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<MessageEvent, String, Unit>
            ): Listener<MessageEvent> = self.case(equals, ignoreCase, trim) { samCallback.call(this, it) }

            fun match(
                regex: org.mozilla.javascript.regexp.NativeRegExp,
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<MessageEvent, MatchResult, Unit>
            ) = self.matching(Regex(regex.toString())) { samCallback.call(this, it) }

            @JvmOverloads
            fun contains(
                equals: String, ignoreCase: Boolean = false, trim: Boolean = true,
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<MessageEvent, String, Unit>
            ): Listener<MessageEvent> = self.contains(equals, ignoreCase, trim) {
                samCallback.call(
                    this,
                    this.message.filterIsInstance<PlainText>().first { p -> p.content.contains(equals) }.content
                )
            }

            @JvmOverloads
            fun startWith(
                equals: String, trim: Boolean = true,
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<MessageEvent, String, Unit>
            ): Listener<MessageEvent> = self.startsWith(equals, trim) {
                samCallback.call(
                    this,
                    this.message.filterIsInstance<PlainText>().first { p -> p.content.startsWith(equals) }.content
                )
            }

            @JvmOverloads
            fun endsWith(
                suffix: String, removeSuffix: Boolean = true, trim: Boolean = true,
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<MessageEvent, String, Unit>
            ): Listener<MessageEvent> = self.endsWith(suffix, removeSuffix, trim) {
                samCallback.call(
                    this,
                    this.message.filterIsInstance<PlainText>()
                        .first { p -> p.content.endsWith(suffix, ignoreCase = false) }.content
                )
            }

            //filter from subject
            fun sentBy(
                qq: Long,
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<FriendMessageEvent, Friend, Unit>
            ) = self.sentBy(qq) { samCallback.call(this as FriendMessageEvent, subject) }

            fun sentByFriend(
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<FriendMessageEvent, Friend, Unit>
            ) = self.sentByFriend { samCallback.call(this, subject) }

            fun sentByStranger(
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<StrangerMessageEvent, Stranger, Unit>
            ) = self.sentByStranger { samCallback.call(this, subject) }

            fun sentByGroupAdmin(
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<GroupMessageEvent, Member, Unit>
            ) = self.sentByAdministrator().invoke { samCallback.call(this.cast(), this.cast()) }

            fun sentByGroupOwner(
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<GroupMessageEvent, Member, Unit>
            ) = self.sentByOwner().invoke { samCallback.call(this.cast(), this.cast()) }

            fun sentByGroupTemp(
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<GroupTempMessageEvent, NormalMember, Unit>
            ) = self.sentByGroupTemp().invoke { samCallback.call(this.cast(), this.cast()) }

            fun sentFrom(
                group: Long,
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<GroupMessageEvent, Group, Unit>
            ) = self.sentFrom(group).invoke { samCallback.call(this.cast(), this.cast()) }

            fun atBot(samCallback: KtLambdaInterfaceBridge.DoubleArgument<MessageEvent, Unit, Unit>) =
                self.atBot().invoke { samCallback.call(this.cast(), Unit) }

            fun atAll(samCallback: KtLambdaInterfaceBridge.DoubleArgument<GroupMessageEvent, Group, Unit>) =
                self.atAll().invoke { samCallback.call(this.cast(), this.cast()) }

            fun at(qq: Long, samCallback: KtLambdaInterfaceBridge.DoubleArgument<MessageEvent, At, Unit>) =
                self.at(qq).invoke {
                    samCallback.call(this.cast(),
                        this.message.filterIsInstance<At>().first { at -> at.target == qq }
                    )
                }

            fun <T : SingleMessage> has(
                type: Class<T>,
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<MessageEvent, SingleMessage, Unit>
            ) = self.content { message.any { it.javaClass == type } }.invoke {
                samCallback.call(this.cast(),
                    this.message.first { m -> m.javaClass == type }
                )
            }

            fun content(
                samCallbackJudge: KtLambdaInterfaceBridge.DoubleArgument<MessageEvent, Unit, Boolean>,
                samCallbackExecute: KtLambdaInterfaceBridge.DoubleArgument<MessageEvent, Unit, Unit>
            ) = self
                .content { samCallbackJudge.call(this, Unit) }
                .invoke { samCallbackExecute.call(this.cast(), Unit) }
        }
    }

    fun <T : MessageEvent> wrapMessageEvent(messageEvent: T) =
        MessageEventKtWrapper(messageEvent)

    class MessageEventKtWrapper(val self: MessageEvent) {
        @JvmOverloads
        @JvmBlockingBridge
        suspend fun <R> selectMessages(
            timeMillis: Long = -1, filterContext: Boolean = true, priority: EventPriority = EventPriority.MONITOR,
            samCallback: KtLambdaInterfaceBridge.SingleArgument<MessageEventSelectBuilderJsImpl<R>, R>
        ) = self.selectMessages(timeMillis, filterContext, priority) {
            samCallback.call(MessageEventSelectBuilderJsImpl(this))
        }

        fun unwrap() = self

        class MessageEventSelectBuilderJsImpl<R>(val self: MessageSelectBuilder<MessageEvent, R>) {
            //selectMessages-only function
            fun default(
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<MessageEvent, Unit, R>
            ) = self.default { samCallback.call(this, Unit) }

            fun timeoutException(
                timeMillis: Long,
                samCallback: KtLambdaInterfaceBridge.NoArgument<Unit>
            ) = self.timeoutException(timeMillis) { MessageSelectionTimeoutException().also { samCallback.call() } }

            fun timeout(
                timeMillis: Long,
                samCallback: KtLambdaInterfaceBridge.NoArgument<R>
            ) = self.timeout(timeMillis) { samCallback.call() }

            //copy from [MessageEventSubscriberBuilderJsImpl]
            //filter from message
            @JvmOverloads
            fun case(
                equals: String, ignoreCase: Boolean = false, trim: Boolean = true,
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<MessageEvent, String, R>
            ) = self.case(equals, ignoreCase, trim) { samCallback.call(this, it) }

            fun match(
                regex: org.mozilla.javascript.regexp.NativeRegExp,
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<MessageEvent, MatchResult, R>
            ) = self.matching(Regex(regex.toString())) { samCallback.call(this, it) }

            @JvmOverloads
            fun contains(
                equals: String, ignoreCase: Boolean = true, trim: Boolean = true,
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<MessageEvent, String, R>
            ) = self.contains(equals, ignoreCase, trim) {
                samCallback.call(
                    this,
                    this.message.filterIsInstance<PlainText>().first { p -> p.content.contains(equals) }.content
                )
            }

            @JvmOverloads
            fun startWith(
                equals: String, trim: Boolean = true,
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<MessageEvent, String, R>
            ) = self.startsWith(equals, trim) {
                samCallback.call(
                    this,
                    this.message.filterIsInstance<PlainText>().first { p -> p.content.startsWith(equals) }.content
                )
            }

            @JvmOverloads
            fun endsWith(
                suffix: String, removeSuffix: Boolean = true, trim: Boolean = true,
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<MessageEvent, String, R>
            ) = self.endsWith(suffix, removeSuffix, trim) {
                samCallback.call(
                    this,
                    this.message.filterIsInstance<PlainText>()
                        .first { p -> p.content.endsWith(suffix, ignoreCase = false) }.content
                )
            }

            //filter from subject
            fun sentBy(
                qq: Long,
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<FriendMessageEvent, Unit, R>
            ) = self.sentBy(qq) { samCallback.call(this as FriendMessageEvent, Unit) }

            fun sentByFriend(
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<FriendMessageEvent, Unit, R>
            ) = self.sentByFriend { samCallback.call(this, Unit) }

            fun sentByStranger(
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<StrangerMessageEvent, Unit, R>
            ) = self.sentByStranger { samCallback.call(this, Unit) }

            fun sentByGroupAdmin(
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<GroupMessageEvent, Unit, R>
            ) = self.sentByAdministrator().invoke { samCallback.call(this.cast(), Unit) }

            fun sentByGroupOwner(
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<GroupMessageEvent, Unit, R>
            ) = self.sentByOwner().invoke { samCallback.call(this.cast(), Unit) }

            fun sentByGroupTemp(
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<GroupTempMessageEvent, Unit, R>
            ) = self.sentByGroupTemp().invoke { samCallback.call(this.cast(), Unit) }

            fun sentFrom(
                group: Long,
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<GroupMessageEvent, Unit, R>
            ) = self.sentFrom(group).invoke { samCallback.call(this.cast(), Unit) }

            fun atBot(samCallback: KtLambdaInterfaceBridge.DoubleArgument<MessageEvent, Unit, R>) =
                self.atBot().invoke { samCallback.call(this.cast(), Unit) }

            fun atAll(samCallback: KtLambdaInterfaceBridge.DoubleArgument<GroupMessageEvent, Unit, R>) =
                self.atAll().invoke { samCallback.call(this.cast(), Unit) }

            fun at(qq: Long, samCallback: KtLambdaInterfaceBridge.DoubleArgument<MessageEvent, At, R>) =
                self.at(qq).invoke {
                    samCallback.call(this.cast(),
                        this.message.filterIsInstance<At>().first { at -> at.target == qq }
                    )
                }

            fun <T : SingleMessage> has(
                type: Class<T>,
                samCallback: KtLambdaInterfaceBridge.DoubleArgument<MessageEvent, SingleMessage, R>
            ) = self.content { message.any { it.javaClass == type } }.invoke {
                samCallback.call(this.cast(),
                    this.message.first { m -> m.javaClass == type }
                )
            }

            fun content(
                samCallbackJudge: KtLambdaInterfaceBridge.DoubleArgument<MessageEvent, Unit, Boolean>,
                samCallbackExecute: KtLambdaInterfaceBridge.DoubleArgument<MessageEvent, Unit, R>
            ) = self
                .content { samCallbackJudge.call(this, Unit) }
                .invoke { samCallbackExecute.call(this.cast(), Unit) }
        }
    }

    @JvmField
    val utils = LinearSyncKtWrapper

    object LinearSyncKtWrapper {
        @JvmOverloads
        @JvmBlockingBridge
        suspend fun <E : Event, R : Any> syncFromEvent(
            clazz: Class<E>,
            timeoutMillis: Long = -1,
            priority: EventPriority = EventPriority.MONITOR,
            samCallback: KtLambdaInterfaceBridge.SingleArgument<E, R>
        ): R? {
            require(timeoutMillis == -1L || timeoutMillis > 0) { "timeoutMillis must be -1 or > 0" }
            return withTimeoutOrNullOrCoroutineScope(timeoutMillis) {
                syncFromEventImpl(clazz, this, priority) {
                    samCallback.call(it)
                }
            }
        }

        @JvmOverloads
        fun <E : Event, R : Any> asyncFromEvent(
            coroutineScope: CoroutineScope,
            clazz: Class<E>,
            timeoutMillis: Long = -1,
            priority: EventPriority = EventPriority.MONITOR,
            samCallback: KtLambdaInterfaceBridge.SingleArgument<E, R>
        ): KotlinCoroutineLib.DeferredJsImpl<R?> {
            require(timeoutMillis == -1L || timeoutMillis > 0) { "timeoutMillis must be -1 or > 0" }
            return KotlinCoroutineLib.DeferredJsImpl(
                coroutineScope.async(context = coroutineScope.coroutineContext) {
                    syncFromEvent(clazz, timeoutMillis, priority, samCallback)
                }
            )
        }

        @JvmOverloads
        @JvmBlockingBridge
        suspend fun <E : Event> nextEvent(
            clazz: Class<E>,
            timeoutMillis: Long = -1,
            priority: EventPriority = EventPriority.MONITOR,
            samCallback: KtLambdaInterfaceBridge.SingleArgument<E, Boolean> =
                object : KtLambdaInterfaceBridge.SingleArgument<E, Boolean> {
                    override fun call(arg: E): Boolean {
                        return true
                    }
                }
        ): E? = withTimeoutOrNullOrCoroutineScope(timeoutMillis) {
            nextEventImpl(clazz, this, priority) { samCallback.call(it) }
        }

        private suspend fun <E : Event, R> syncFromEventImpl(
            clazz: Class<E>,
            coroutineScope: CoroutineScope,
            priority: EventPriority,
            mapper: (E) -> R?
        ): R = suspendCancellableCoroutine { continuation ->
            coroutineScope.globalEventChannel().subscribe(clazz.kotlin, priority = priority) {
                runCatching {
                    continuation.resumeWith(
                        Result.success(
                            mapper.invoke(this) ?: return@subscribe ListeningStatus.LISTENING
                        )
                    )
                }
                return@subscribe ListeningStatus.STOPPED
            }
        }

        @OptIn(ExperimentalCoroutinesApi::class)
        private suspend fun <E : Event> nextEventImpl(
            clazz: Class<E>,
            coroutineScope: CoroutineScope,
            priority: EventPriority,
            filter: (E) -> Boolean
        ): E = suspendCancellableCoroutine { continuation ->
            coroutineScope.globalEventChannel().subscribe(clazz.kotlin, priority = priority) {
                if (!filter(this)) return@subscribe ListeningStatus.LISTENING
                runCatching {
                    continuation.resumeWith(Result.success(this))
                }
                return@subscribe ListeningStatus.STOPPED
            }
        }

        private suspend inline fun <R> withTimeoutOrNullOrCoroutineScope(
            timeoutMillis: Long,
            noinline block: suspend CoroutineScope.() -> R
        ): R? {
            require(timeoutMillis == -1L || timeoutMillis > 0) { "timeoutMillis must be -1 or > 0 " }
            return if (timeoutMillis == -1L) {
                coroutineScope(block)
            } else {
                withTimeoutOrNull(timeoutMillis, block)
            }
        }
    }
}