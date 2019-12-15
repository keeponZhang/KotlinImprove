package com.bennyhuo.coroutines.lite

import java.util.concurrent.atomic.AtomicReference
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.CoroutineContext

import kotlin.coroutines.experimental.startCoroutine
import kotlin.coroutines.experimental.suspendCoroutine

typealias OnComplete<T> = (T?, Throwable?) -> Unit

sealed class State {
    object InComplete : State()
    class Complete<T>(val value: T? = null, val exception: Throwable? = null) : State()
    //线程还没执行完，如果外面调用了类似await的信息，可以回调信息出去
    class CompleteHandler<T>(val handler: OnComplete<T>) : State()
}

abstract class AbstractCoroutine<T>(
    override val context: CoroutineContext, block: suspend () -> T
) : Continuation<T> {

    //状态需要是原子的
    protected val state = AtomicReference<State>()

    init {
        state.set(State.InComplete)
        //这里传进去this，最后线程池执行完会调用resume方法
        //launch 举例
        // (CoroutinesLibrary)createCoroutineUnchecked(completion).resume(Unit) - >
        // (IntrinsicsJvm)(this.create(completion) as kotlin.coroutines.experimental.jvm.internal.CoroutineImpl).facade
        //->(CoroutineImpl)facade = interceptContinuationIfNeeded(_context!!, this)->(CoroutineIntrinsics)interceptContinuationIfNeeded
        //->context[ContinuationInterceptor]?.interceptContinuation(continuation) ?: continuation
        // (这里会通过ContinuationInterceptor为key查找下，有没有拦截器（其实就是使用代理，对原来的continuation进行一次包装）) ->
        //(DispatcherContext).interceptContinuation
        println("开启协程咯")
        block.startCoroutine(this) //this:Continuation
    }

    val isCompleted
        get() = state.get() is State.Complete<*>

    //调用resume表示执行完
    override fun resume(value: T) {
//   以原子方式设置为给定值，并返回以前的值。
        //程序正常终止，需要设置状态为State.Complete
        val currentState = state.getAndSet(State.Complete(value))
        println("我是" + this + " 结束调用 <<resume>> " + value + " 当前状态=" + getState(
            currentState) + "  最新状态 =" + getState(state.get()))
        when (currentState) {
            // State.CompleteHandler<T> 运行时泛型参数已经被擦除了
            is State.CompleteHandler<*> -> {
                //这里方便编译期编译器做强转
                println("resume State.CompleteHandler")
                (currentState as State.CompleteHandler<T>).handler(value, null)
            }
        }
    }

    //调用resumeWithException表示执行完,这里没有抛出异常，所以sample2可以launch方面抛出异常，程序继续执行
    override fun resumeWithException(exception: Throwable) {

        //抛异常后说明程序到此终止，需要设置状态为State.Complete
        val currentState = state.getAndSet(State.Complete<T>(null, exception))
        println("我是" + this + " 结束调用 resumeWithException " + exception + " 当前状态" + getState(
            currentState) + " 最新状态 =" + getState(state.get()))
        //说明有回调传进来
        when (currentState) {
            is State.CompleteHandler<*> -> {
                (currentState as State.CompleteHandler<T>).handler(null, exception)
            }
        }
    }

    fun getState(state: State): String {
        when (state) {
            is State.CompleteHandler<*> -> return " State.CompleteHandler"
            is State.Complete<*> -> return " State.Complete"
            is State.InComplete -> return " State.InComplete"
        }
    }

    //协程的等待(协程有可能执行完了，有可能没有执行完)
    suspend fun join() {
        val currentState = state.get()
        when (currentState) {
            is State.InComplete -> {
                println("AbstractCoroutine join 此时还没完成，所以执行挂起操作 InComplete:")
                return joinSuspend()
            }  //如果没有执行完，执行挂起操作
            is State.Complete<*> -> {
                println("AbstractCoroutine join 此时已经是 Complete 状态")
                return
            } //如果已经执行完，直接return，代码继续执行就好
            else -> {
                println("AbstractCoroutine join 此时为else状态")
                throw IllegalStateException("Invalid State: $currentState")
            }
        }
    }


//    public suspend inline fun <T> suspendCoroutine(crossinline block: (Continuation<T>) -> Unit): T =
//        suspendCoroutineOrReturn { c: Continuation<T> ->
//            val safe = SafeContinuation(c)
//            block(safe)
//            safe.getResult()
//        }
    //从上面可知，执行下面bolock之前，传进来c（CoroutineImpl的子类）返回一个SafeContinuation，传入block,
// 然后调用SafeContinuation的resume方法
    //协程真正的等待
    //join  suspendCoroutine<T>泛型参数表示返回类型
    private suspend fun joinSuspend() = suspendCoroutine<Unit> { continuation ->
        //这表明block是有参数的，下面真正用到block时会把参数传进来，->箭头后面，表示传进来之后怎么处理
        //这里面调用抛异常的话会走到resumeWithException
        // 其实这里就是处理回调
        doOnCompleted { t, throwable ->
            println("joinSuspend doOnCompleted continuation "+continuation)
            //continuation:SafeContinuation
            continuation.resume(Unit)
        }
    }

    //await  有返回值
//    private suspend fun awaitSuspend() = suspendCoroutine<T> { continuation ->
//        doOnCompleted { t, throwable ->
//            when {
//                t != null -> continuation.resume(t)
//                throwable != null -> continuation.resumeWithException(throwable)
//                else -> throw IllegalStateException("Won't happen.")
//            }
//        }
//    }

    protected fun doOnCompleted(block: (T?, Throwable?) -> Unit) {
        println("AbstractCoroutine  doOnCompleted 调到这里说明挂起的那个job已经执行完了 state:" + getState(state
            .get()))
        //如果当前值 == 预期值，则以原子方式将该值设置为给定的更新值。这里需要注意的是这个方法的返回值实际上是是否成功修改，而与之前的值无关。
        //compareAndSet(V expect, V update)
        if (!state.compareAndSet(State.InComplete, State.CompleteHandler<T>(block))) {
            //如果没有设置成功(如果调用!state.compareAndSet前已经是Complete状态，可能会设置失败)
            val currentState = state.get()
            println("AbstractCoroutine doOnCompleted 没有设置成功 此时状态是:" + getState(currentState))
            when (currentState) {
                is State.Complete<*> -> {
                    (currentState as State.Complete<T>).let {
                        //这个不能注释掉,因为一定要通过调用这个回调出去，然后外面调用continuation.resume(Unit)是程序执行下去
                        block(currentState.value, currentState.exception)
//                        return
//                        throw IllegalStateException("Invalid State: $currentState")
                    }
                }
                else -> {
                    println("currentState else")
                    throw IllegalStateException("Invalid State: $currentState")
                }
            }
        }
    }
}