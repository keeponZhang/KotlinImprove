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
        block.startCoroutine(this) //this:Continuation
    }

    val isCompleted
        get() = state.get() is State.Complete<*>

    //调用resume表示执行完
    override fun resume(value: T) {
//   以原子方式设置为给定值，并返回以前的值。
        val currentState = state.getAndSet(State.Complete(value))
        println("AbstractCoroutine 结束调用 resume " + value + "       我是" + this + " " + getState(
            currentState))
        when (currentState) {
            // State.CompleteHandler<T> 运行时泛型参数已经被擦除了
            is State.CompleteHandler<*> -> {
                //这里方便编译期编译器做强转
                println("resume State.CompleteHandler")
                (currentState as State.CompleteHandler<T>).handler(value, null)
            }
        }
    }

    //调用resumeWithException表示执行完
    override fun resumeWithException(exception: Throwable) {
        println("AbstractCoroutine resumeWithException" + exception)
        val currentState = state.getAndSet(State.Complete<T>(null, exception))
        //说明有回调传进来
        when (currentState) {
            is State.CompleteHandler<*> -> {
                (currentState as State.CompleteHandler<T>).handler(null, exception)
            }
        }
    }

    fun getState(state: State): String {
        when (state) {
            is State.CompleteHandler<*> -> return "   State.CompleteHandler"
            is State.Complete<*> -> return "   State.Complete"
            is State.InComplete -> return "   State.InComplete"
        }
    }

    //协程的等待(协程有可能执行完了，有可能没有执行完)
    suspend fun join() {
        val currentState = state.get()
        println("AbstractCoroutine join currentState:" + getState(currentState))
        when (currentState) {
            is State.InComplete -> return joinSuspend()  //如果没有执行完，执行挂起操作
            is State.Complete<*> -> return  //如果已经执行完，直接return，代码继续执行就好
            else -> {
                throw IllegalStateException("Invalid State: $currentState")
            }
        }
    }

    //协程真正的等待
    //join  suspendCoroutine<T>泛型参数表示返回类型
    private suspend fun joinSuspend() = suspendCoroutine<Unit> { continuation ->
        //这表明block是有参数的，下面真正用到block时会把参数传进来，->箭头后面，表示传进来之后怎么处理
        //这里面调用抛异常的话会走到resumeWithException
        // 其实这里就是处理回调
        doOnCompleted { t, throwable ->
            println("joinSuspend doOnCompleted")
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
        println("AbstractCoroutine doOnCompleted before state:" + getState(state.get()))
        //如果当前值 == 预期值，则以原子方式将该值设置为给定的更新值。这里需要注意的是这个方法的返回值实际上是是否成功修改，而与之前的值无关。
        //compareAndSet(V expect, V update)
        if (!state.compareAndSet(State.InComplete, State.CompleteHandler<T>(block))) {
            //如果没有设置成功(如果调用!state.compareAndSet前已经是Complete状态，可能会设置失败)
            val currentState = state.get()
            println("AbstractCoroutine doOnCompleted 没有设置成功 after state:" + getState(currentState))
            when (currentState) {
                is State.Complete<*> -> {
                    (currentState as State.Complete<T>).let {
                        //这个不能注释掉,
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