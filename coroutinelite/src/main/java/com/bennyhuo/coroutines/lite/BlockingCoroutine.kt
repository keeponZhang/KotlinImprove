package com.bennyhuo.coroutines.lite

import java.util.concurrent.LinkedBlockingDeque
import kotlin.coroutines.experimental.CoroutineContext

typealias EventTask = () -> Unit

class BlockingQueueDispatcher : LinkedBlockingDeque<EventTask>(), Dispatcher {

//    fun dispatch(block: () -> Unit)
    //DispatchedContinuation 调用到这里
    override fun dispatch(block: EventTask) {
    println("BlockingQueueDispatcher block " + this)
        offer(block)
    }
}

class BlockingCoroutine<T>(context: CoroutineContext, private val eventQueue: LinkedBlockingDeque<EventTask>, block: suspend () -> T) : AbstractCoroutine<T>(context, block) {

    fun joinBlocking() {
        println("!isCompleted "+!isCompleted)
        while (!isCompleted) {
            //如果eventQueue是空的，take会阻塞
            println("joinBlocking while eventQueue.take() ")
            eventQueue.take().invoke()
            println(" eventQueue.take().invoke() 完成")
        }
    }
}