package com.bennyhuo.coroutines.lite

import kotlin.coroutines.experimental.CoroutineContext
import kotlin.coroutines.experimental.EmptyCoroutineContext

/**
 * Created by benny on 5/20/17.
 */
fun launch(
    context: CoroutineContext = CommonPool, block: suspend () -> Unit
): AbstractCoroutine<Unit> {
    return StandaloneCoroutine(context, block)
}
fun launch2(
    context: CoroutineContext = CommonPool, block: suspend () -> Unit
): AbstractCoroutine<Unit> {
    return StandaloneCoroutine2(context, block)
}

fun launch多个Interceptor(
    //
    context: CoroutineContext = MyContinuationInterceptor()+ CommonPool , block: suspend () -> Unit
):
    AbstractCoroutine<Unit> {
    return StandaloneCoroutine(context, block)
}

fun launch多个Interceptor2(
    //
    context: CoroutineContext = CommonPool+MyContinuationInterceptor() , block: suspend () -> Unit
):
    AbstractCoroutine<Unit> {
    return StandaloneCoroutine(context, block)
}

fun launchDefault(
    context: CoroutineContext = EmptyCoroutineContext, block: suspend () -> Unit
): AbstractCoroutine<Unit> {
    return StandaloneCoroutine(context, block)
}

fun <T> async(context: CoroutineContext = CommonPool, block: suspend () -> T): Deferred<T> {
    return Deferred(context, block)
}

fun runBlocking(block: suspend () -> Boolean) {
    val eventQueue = BlockingQueueDispatcher()
    val context = DispatcherContext(eventQueue)
    val blockingCoroutine = BlockingCoroutine(context, eventQueue, block)
    blockingCoroutine.joinBlocking()
}

fun runBlocking2(block: suspend () -> Unit) {
    val eventQueue = BlockingQueueDispatcher()
    val context = DispatcherContext(eventQueue)
    BlockingCoroutine(context, eventQueue, block).joinBlocking()
}

