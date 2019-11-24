package com.bennyhuo.coroutines.lite

import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import kotlin.coroutines.experimental.AbstractCoroutineContextElement
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.ContinuationInterceptor

interface Dispatcher {
    fun dispatch(block: ()->Unit)
}

private object CommonPoolDispatcher: Dispatcher {
    private val executor = Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors()) { runnable ->
        Thread(runnable, "CommonPool").apply { isDaemon = true }
    }
    //上面之所以这样写，是因为从2可以看出，这时一个输入类型如runnable，返回为Thread的类型
    private val executor2 = Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors(),object :ThreadFactory{
        override fun newThread(r: Runnable): Thread {
            return Thread()
        }
    })


    override fun dispatch(block: () -> Unit) {
        executor.submit(block)
    }
}

object CommonPool: DispatcherContext(CommonPoolDispatcher)

open class DispatcherContext(private val dispatcher: Dispatcher) : AbstractCoroutineContextElement(ContinuationInterceptor), ContinuationInterceptor {
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T>{
       return DispatchedContinuation(//下面这段代码是要查找其他拦截器，并保证能调用它们的拦截方法
                continuation.context.fold(continuation, { cont, element ->
                    if (element != this && element is ContinuationInterceptor)
                        element.interceptContinuation(cont) else cont
                }), dispatcher)
    }

}

private class DispatchedContinuation<T>(val delegate: Continuation<T>, val dispatcher: Dispatcher) : Continuation<T>{
    override val context = delegate.context

    override fun resume(value: T) {
        dispatcher.dispatch {
            delegate.resume(value)
        }
    }

    override fun resumeWithException(exception: Throwable) {
        dispatcher.dispatch {
            delegate.resumeWithException(exception)
        }
    }
}