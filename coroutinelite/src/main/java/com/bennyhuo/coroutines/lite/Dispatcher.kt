package com.bennyhuo.coroutines.lite

import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import kotlin.coroutines.experimental.AbstractCoroutineContextElement
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.ContinuationInterceptor
import kotlin.coroutines.experimental.CoroutineContext

interface Dispatcher {
    fun dispatch(block: () -> Unit)
}

private object CommonPoolDispatcher : Dispatcher {
    private val executor =
        Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors()) { runnable ->
            Thread(runnable, "CommonPool").apply { isDaemon = true }
        }
    //上面之所以这样写，是因为从2可以看出，这时一个输入类型如runnable，返回为Thread的类型
    private val executor2 =
        Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors(),
            object : ThreadFactory {
                override fun newThread(r: Runnable): Thread {
                    return Thread()
                }
            })

    override fun dispatch(block: () -> Unit) {
        executor.submit(block)
    }
}

object CommonPool : DispatcherContext(CommonPoolDispatcher)

//DispatcherContext 是带dispatcher的，ContinuationInterceptor这里使用ContinuationInterceptor作为key
// 是因为系统默认会去调用这个
open class DispatcherContext(private val dispatcher: Dispatcher) :
    AbstractCoroutineContextElement(ContinuationInterceptor), ContinuationInterceptor {
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {
        //以ContinuationInterceptor的拦截器的context会在最后才调用
        val fold = continuation.context.fold(continuation, { cont, element ->
            println(
                "cont " + cont + "  element:" + element + " continuation.context =" + continuation.context)
            if (element != this && element is ContinuationInterceptor) {
                //ContinuationInterceptor 注意，这个不是key
                println("element != this && element is ContinuationInterceptor")
                //如果是拦截器， element.interceptContinuation(cont)会返回一个Continuation(cont)，此时已经调用了拦截方法
                element.interceptContinuation(cont)
            } else {
                //这个const就是编译器创建的继承CoroutineImpl的实例  element：CommonPool
                println(
                    " else ")
                cont
            }
        })
        println("fold "+fold)
        return DispatchedContinuation(fold, dispatcher)//下面这段代码是要查找其他拦截器，并保证能调用它们的拦截方法
    }
}

class MyContinuationInterceptor :
    AbstractCoroutineContextElement(MyContinuationInterceptor), ContinuationInterceptor {
    companion object Key : CoroutineContext.Key<MyContinuationInterceptor>

    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {
        println("经过MyContinuationInterceptor啦")
        return continuation;
    }
}

//可以用来切换线程
private class DispatchedContinuation<T>(
    val delegate: Continuation<T>, val dispatcher: Dispatcher
) :
    Continuation<T> {
    override val context = delegate.context

    //这个delgegate是编译器生成的继承CoroutineImpl的子类
    //CoroutineImpl的completion是StandaloneCoroutine
    override fun resume(value: T) {
        //block.startCoroutine(this)  开启协程的时候回调用到
        val javaClass = delegate.javaClass
        val declaredFields = javaClass.declaredFields.size
        println(
            "DispatchedContinuation delegate.javaClass " + delegate.javaClass + "  " + declaredFields)

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