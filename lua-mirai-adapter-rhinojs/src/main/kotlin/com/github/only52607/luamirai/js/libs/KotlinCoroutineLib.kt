@file:Suppress("unused", "DeferredIsResult")

package com.github.only52607.luamirai.js.libs

import com.github.only52607.luamirai.js.utils.KtLambdaInterfaceBridge
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.sync.withPermit
import me.him188.kotlin.jvm.blocking.bridge.JvmBlockingBridge
import com.github.only52607.luamirai.js.JSLib
import org.mozilla.javascript.Context
import org.mozilla.javascript.Scriptable
import org.mozilla.javascript.ScriptableObject
import java.util.*
import java.util.concurrent.Executors
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume

@Suppress("FunctionName", "ClassName", "MemberVisibilityCanBePrivate", "PropertyName")
class KotlinCoroutineLib(
    private val scriptCoroutineScope: CoroutineScope
) : JSLib() {
    @JvmSynthetic
    override fun load(scope: Scriptable, context: Context) {
        ScriptableObject.putProperty(scope, "coroutine", Context.javaToJS(this, scope))
    }

    @JvmField
    val Dispatchers = kotlinx.coroutines.Dispatchers

    @JvmField
    val currentScope = scriptCoroutineScope

    fun isCancelled() = !scriptCoroutineScope.isActive

    class CoroutineContextJsImpl(coroutineContext: CoroutineContext) :
        JsImplWrapperImpl<CoroutineContext>(coroutineContext) {
        fun plus(other: CoroutineContextJsImpl) = CoroutineContextJsImpl(self.plus(other.self))
        fun isActive() = self.isActive
        fun getJob() = JobJsImpl(self.job)
        fun cancel(cause: String) = self.cancel(CancellationException(cause))
        fun cancelChildren(cause: String) = self.cancelChildren(CancellationException(cause))
        fun ensureActive() = self.ensureActive()
    }

    @JvmField
    val continuation = object {
        fun <T> create(
            coroutineContext: CoroutineContext,
            samCallback: KtLambdaInterfaceBridge.SingleArgument<Result<T>, Unit>
        ): ContinuationJsImpl<T> = ContinuationJsImpl(Continuation(coroutineContext) {
            samCallback.call(it)
        })
    }

    class ContinuationJsImpl<T>(continuation: Continuation<T>) : JsImplWrapperImpl<Continuation<T>>(continuation) {
        fun resumeWithSuccess(value: T) = self.resumeWith(Result.success(value))
        fun resumeWithFailure(reason: String) = self.resumeWith(Result.failure(Throwable(reason)))
        fun resume(value: T) = self.resume(value)
        fun getContext() = CoroutineContextJsImpl(self.context)
    }

    @JvmOverloads
    fun createSupervisorJob(parent: Job? = scriptCoroutineScope.coroutineContext[Job]) =
        SupervisorJobJsImpl(SupervisorJob(parent))

    class SupervisorJobJsImpl(job: CompletableJob) : JsImplWrapperImpl<CompletableJob>(job) {
        @JvmBlockingBridge
        suspend fun join() = self.join()
        fun cancel(reason: String) = self.cancel(cause = kotlinx.coroutines.CancellationException(reason))

        @JvmBlockingBridge
        suspend fun cancelAndJoin() = self.cancelAndJoin()
        fun isActive() = self.isActive
        fun isCanceled() = self.isCancelled
        fun isCompleted() = self.isCompleted
        fun complete() = self.complete()
        fun completeExceptionally(reason: String) = self.completeExceptionally(Throwable(reason))
    }

    @JvmOverloads
    fun createJob(parent: Job? = scriptCoroutineScope.coroutineContext[Job]) = JobJsImpl(Job(parent))

    class JobJsImpl(job: Job) : JsImplWrapperImpl<Job>(job) {
        @JvmBlockingBridge
        suspend fun join() = self.join()
        fun cancel(reason: String) = self.cancel(cause = kotlinx.coroutines.CancellationException(reason))

        @JvmBlockingBridge
        suspend fun cancelAndJoin() = self.cancelAndJoin()
        fun isActive() = self.isActive
        fun isCancelled() = self.isCancelled
        fun isCompleted() = self.isCompleted
    }

    fun newSingleThreadContext(name: String) =
        Executors.newSingleThreadExecutor().asCoroutineDispatcher() + CoroutineName(name)

    fun newFixedThreadPoolContext(nThreads: Int, name: String) =
        Executors.newFixedThreadPool(nThreads).asCoroutineDispatcher() + CoroutineName(name)

    fun CoroutineScope(coroutineContext: CoroutineContext) =
        kotlinx.coroutines.CoroutineScope(coroutineContext)

    @JvmBlockingBridge
    suspend fun <T> coroutineScope(
        samCallback: KtLambdaInterfaceBridge.SingleArgument<CoroutineScope, T>
    ) = coroutineScope { samCallback.call(this) }

    @JvmBlockingBridge
    suspend fun <T> supervisorScope(
        samCallback: KtLambdaInterfaceBridge.SingleArgument<CoroutineScope, T>
    ) = supervisorScope { samCallback.call(this) }

    @JvmBlockingBridge
    suspend fun <T> suspendCoroutine(samCallback: KtLambdaInterfaceBridge.SingleArgument<ContinuationJsImpl<T>, Unit>): T =
        kotlin.coroutines.suspendCoroutine { samCallback.call(ContinuationJsImpl(it)) }

    @JvmBlockingBridge
    suspend fun delay(timeMills: Long) = kotlinx.coroutines.delay(timeMills)

    @JvmBlockingBridge
    suspend fun yield() = kotlinx.coroutines.yield()

//    fun launchFromGlobalScope(samCallback: KtLambdaInterfaceBridge.SingleArgument<CoroutineScope, Unit>) =
//        JobJsImpl(GlobalScope.launch { samCallback.call(this) })

    fun launchFromPluginScope(
        samCallback: KtLambdaInterfaceBridge.SingleArgument<CoroutineScope, Unit>
    ) = JobJsImpl(scriptCoroutineScope.launch { samCallback.call(this) })

    @InternalCoroutinesApi
    fun launch(
        coroutineScope: CoroutineScope,
        samCallback: KtLambdaInterfaceBridge.SingleArgument<CoroutineScope, Unit>
    ) = JobJsImpl(coroutineScope.launch { samCallback.call(this) })

    fun launch(
        samCallback: KtLambdaInterfaceBridge.SingleArgument<CoroutineScope, Unit>
    ) = launchFromPluginScope(samCallback)

//    fun <T> asyncFromGlobalScope(
//        samCallback: KtLambdaInterfaceBridge.SingleArgument<CoroutineScope, T>
//    ) = DeferredJsImpl(GlobalScope.async { samCallback.call(this) })

    fun <T> asyncFromPluginScope(
        samCallback: KtLambdaInterfaceBridge.SingleArgument<CoroutineScope, T>
    ) = DeferredJsImpl(scriptCoroutineScope.async { samCallback.call(this) })

    fun <T> async(
        coroutineScope: CoroutineScope,
        samCallback: KtLambdaInterfaceBridge.SingleArgument<CoroutineScope, T>
    ) = DeferredJsImpl(coroutineScope.async(coroutineScope.coroutineContext) { samCallback.call(this) })

    fun <T> async(
        samCallback: KtLambdaInterfaceBridge.SingleArgument<CoroutineScope, T>
    ) = asyncFromPluginScope(samCallback)

    class DeferredJsImpl<T>(deferred: Deferred<T>) : JsImplWrapperImpl<Deferred<T>>(deferred) {
        @JvmBlockingBridge
        suspend fun await(): T = self.await()

        @ExperimentalCoroutinesApi
        fun getCompleted() = self.getCompleted()
    }

    @JvmBlockingBridge
    suspend fun <T> withContext(
        coroutineContext: CoroutineContext,
        samCallback: KtLambdaInterfaceBridge.SingleArgument<CoroutineScope, T>
    ) = withContext(coroutineContext) { samCallback.call(this) }

    /* kotlin.coroutines.withTimeout doesn't work in js. */
    @ExperimentalCoroutinesApi
    @JvmBlockingBridge
    suspend fun <T> withTimeout(
        timeMills: Long,
        samCallback: KtLambdaInterfaceBridge.SingleArgument<CoroutineScope, T>
    ) = coroutineScope functionReturn@{
        val deferred = async { samCallback.call(this) }.also { this.launch { it.await() } }
        delay(timeMills)
        try {
            return@functionReturn deferred.getCompleted()
        } catch (ex: IllegalStateException) {
            deferred.cancel()
            throw Exception("Time out waiting for $timeMills ms.")
        }
    }

    @JvmField
    val channel = ChannelJsField

    object ChannelJsField {
        @JvmField
        val BufferOverflow = object {
            @JvmField
            val DROP_LATEST = kotlinx.coroutines.channels.BufferOverflow.DROP_LATEST

            @JvmField
            val DROP_OLDEST = kotlinx.coroutines.channels.BufferOverflow.DROP_OLDEST

            @JvmField
            val SUSPEND = kotlinx.coroutines.channels.BufferOverflow.SUSPEND
        }

        @JvmField
        val ChannelFactory = Channel.Factory

        @JvmOverloads
        fun <T> create(
            capacity: Int = ChannelFactory.RENDEZVOUS,
            onBufferOverflow: kotlinx.coroutines.channels.BufferOverflow =
                kotlinx.coroutines.channels.BufferOverflow.SUSPEND,
            samCallback: KtLambdaInterfaceBridge.SingleArgument<T, Unit> =
                object : KtLambdaInterfaceBridge.SingleArgument<T, Unit> {
                    override fun call(arg: T) {}
                }
        ): ChannelJsImpl<T> = ChannelJsImpl(Channel(capacity, onBufferOverflow, ({ samCallback.call(it) })))
    }

    class ChannelJsImpl<T>(channel: Channel<T>) : JsImplWrapperImpl<Channel<T>>(channel) {
        @OptIn(ExperimentalCoroutinesApi::class)
        fun isClosedForReceive() = self.isClosedForReceive

        @OptIn(ExperimentalCoroutinesApi::class)
        fun isClosedForSend() = self.isClosedForSend

        @JvmBlockingBridge
        suspend fun receive(samCallback: KtLambdaInterfaceBridge.SingleArgument<T, Unit>) {
            while (!isClosedForReceive()) {
                try {
                    samCallback.call(self.receive())
                } catch (ex: Exception) {
                    return
                }
            }
        }

        @JvmBlockingBridge
        suspend fun send(value: T) = self.send(value)
        fun close() = self.close()
    }

    @JvmField
    val flow = object {
        fun <T> create(samCallback: KtLambdaInterfaceBridge.SingleArgument<FlowCollectorJsImpl<T>, Unit>): FlowJsImpl<T> =
            FlowJsImpl(flow { samCallback.call(FlowCollectorJsImpl(this)) })

        fun <T> flowOf(elements: Array<T>) = FlowJsImpl(flow { repeat(elements.count()) { emit(elements[it]) } })
    }

    class FlowCollectorJsImpl<T>(flowCollector: FlowCollector<T>) : JsImplWrapperImpl<FlowCollector<T>>(flowCollector) {
        @JvmBlockingBridge
        suspend fun emit(value: T) = self.emit(value)
    }

    class FlowJsImpl<T>(flow: Flow<T>) : JsImplWrapperImpl<Flow<T>>(flow) {
        fun onEach(samCallback: KtLambdaInterfaceBridge.SingleArgument<T, Unit>) =
            FlowJsImpl(self.onEach { samCallback.call(it) })

        /* Every transform operation returns a new FlowJsImpl,
        * because internal transform creates a new Flow */
        fun <R> map(samCallback: KtLambdaInterfaceBridge.SingleArgument<T, R>) =
            FlowJsImpl(self.map { samCallback.call(it) })

        fun filter(samCallback: KtLambdaInterfaceBridge.SingleArgument<T, Boolean>) =
            FlowJsImpl(self.filter { samCallback.call(it) })

        fun filterNot(samCallback: KtLambdaInterfaceBridge.SingleArgument<T, Boolean>) =
            FlowJsImpl(self.filterNot { samCallback.call(it) })

        fun take(count: Int) = FlowJsImpl(self.take(count))
        fun takeWhile(samCallback: KtLambdaInterfaceBridge.SingleArgument<T, Boolean>) =
            FlowJsImpl(self.takeWhile { samCallback.call(it) })

        fun drop(count: Int) = FlowJsImpl(self.drop(count))
        fun dropWhile(samCallback: KtLambdaInterfaceBridge.SingleArgument<T, Boolean>) =
            FlowJsImpl(self.dropWhile { samCallback.call(it) })

        fun buffer(
            capacity: Int,
            onBufferOverflow: kotlinx.coroutines.channels.BufferOverflow
        ) = FlowJsImpl(self.buffer(capacity, onBufferOverflow))

        fun buffer() = FlowJsImpl(self.buffer())
        fun conflate() = FlowJsImpl(self.conflate())

        fun flowOn(context: CoroutineContextJsImpl) = FlowJsImpl(self.flowOn(context.unwrap()))
        fun launchIn(scope: CoroutineScope) = JobJsImpl(self.launchIn(scope))
        fun catch(samCallback: KtLambdaInterfaceBridge.DoubleArgument<FlowCollectorJsImpl<T>, Throwable, Unit>) =
            FlowJsImpl(self.catch { samCallback.call(FlowCollectorJsImpl(this), it) })

        @FlowPreview
        fun debounce(timeoutMills: Long) = FlowJsImpl(self.debounce(timeoutMills))

        @FlowPreview
        fun debounce(samCallback: KtLambdaInterfaceBridge.SingleArgument<T, Long>) =
            FlowJsImpl(self.debounce { samCallback.call(it) })

        fun cancellable() = FlowJsImpl(self.cancellable())

        fun <TT, R> combine(
            other: FlowJsImpl<TT>,
            samCallback: KtLambdaInterfaceBridge.DoubleArgument<T, TT, R>
        ) = FlowJsImpl(self.combine(other.self) { a: T, b: TT -> samCallback.call(a, b) })

        fun <TT, R> zip(
            other: FlowJsImpl<TT>,
            samCallback: KtLambdaInterfaceBridge.DoubleArgument<T, TT, R>
        ) = FlowJsImpl(self.zip(other.self) { a: T, b: TT -> samCallback.call(a, b) })

        @JvmBlockingBridge
        suspend fun collect(samCallback: KtLambdaInterfaceBridge.SingleArgument<T, Unit>) =
            self.collect { samCallback.call(it) }

        @JvmBlockingBridge
        suspend fun collectIndexed(samCallback: KtLambdaInterfaceBridge.DoubleArgument<Int, T, Unit>) =
            self.collectIndexed { index, value -> samCallback.call(index, value) }

        @JvmBlockingBridge
        suspend fun first() = self.first()

        @JvmBlockingBridge
        suspend fun first(samCallback: KtLambdaInterfaceBridge.SingleArgument<T, Boolean>) =
            self.first { samCallback.call(it) }

        @JvmBlockingBridge
        suspend fun count() = self.count()

        @JvmBlockingBridge
        suspend fun count(samCallback: KtLambdaInterfaceBridge.SingleArgument<T, Boolean>) =
            self.count { samCallback.call(it) }
    }

    @JvmField
    val mutex = MutexJsField

    object MutexJsField {
        @JvmOverloads
        fun create(boolean: Boolean = false) = MutexJsImpl(Mutex(boolean))
    }

    class MutexJsImpl(mutex: Mutex) : JsImplWrapperImpl<Mutex>(mutex) {
        fun isLocked() = self.isLocked
        fun holdsLock(objects: Objects) = self.holdsLock(objects)

        @JvmBlockingBridge
        suspend fun lock() = self.lock(null)

        @JvmBlockingBridge
        suspend fun lock(objects: Objects) = self.lock(objects)
        fun tryLock(objects: Objects) = self.tryLock(objects)
        fun unlock() = self.unlock(null)
        fun unlock(objects: Objects) = self.unlock(objects)

        @JvmBlockingBridge
        suspend fun <T> withLock(
            objects: Objects?,
            samCallback: KtLambdaInterfaceBridge.NoArgument<T>
        ): T = self.withLock(objects) { samCallback.call() }

        @JvmBlockingBridge
        suspend fun <T> withLock(samCallback: KtLambdaInterfaceBridge.NoArgument<T>): T =
            withLock(null, samCallback)
    }

    @JvmField
    val semaphore = SemaphoreJsField

    object SemaphoreJsField {
        @JvmOverloads
        fun create(permits: Int, acquiredPermits: Int = 0) =
            SemaphoreJsImpl(Semaphore(permits, acquiredPermits))
    }

    class SemaphoreJsImpl(semaphore: Semaphore) : JsImplWrapperImpl<Semaphore>(semaphore) {
        fun availablePermits() = self.availablePermits

        @JvmBlockingBridge
        suspend fun acquire() = self.acquire()
        fun release() = self.release()
        fun tryAcquire() = self.tryAcquire()

        @JvmBlockingBridge
        suspend fun <T> withPermits(samCallback: KtLambdaInterfaceBridge.NoArgument<T>): T =
            self.withPermit { samCallback.call() }
    }

    //wrapper
    fun wrapCoroutineContext(context: CoroutineContext) = CoroutineContextJsImpl(context)
    fun <T> wrapContinuation(continuation: Continuation<T>) = ContinuationJsImpl(continuation)
    fun wrapSupervisorJob(job: CompletableJob) = SupervisorJobJsImpl(job)
    fun wrapJob(job: Job) = JobJsImpl(job)
    fun <T> wrapDeferred(deferred: Deferred<T>) = DeferredJsImpl(deferred)
    fun <E> wrapChannel(channel: Channel<E>) = ChannelJsImpl(channel)
    fun <T> wrapFlow(flow: Flow<T>) = FlowJsImpl(flow)
    fun <T> wrapFlowCollector(flowCollector: FlowCollector<T>) =
        FlowCollectorJsImpl(flowCollector)
}

open class JsImplWrapperImpl<ST>(val self: ST) {
    fun unwrap() = self
}


