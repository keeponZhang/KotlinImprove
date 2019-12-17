package com.bennyhuo.coroutines.lite

import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.coroutines.experimental.suspendCoroutine

/**
 * Created by benny on 2018/5/20.
 */
private val executor = Executors.newScheduledThreadPool(1) { runnable ->
    Thread(runnable, "Scheduler").apply { isDaemon = true }
}

suspend fun delay(time: Long, unit: TimeUnit = TimeUnit.MILLISECONDS) = suspendCoroutine<Unit> {
    continuation ->
    //这里挂起下面的方法还会继续走，任务完成，挂起恢复，任务继续下上走 continuation(SafeContinuation)
    println("准备要执行delay方法了 "+System.currentTimeMillis())
    executor.schedule({
        println("正在执行runnbale任务 continuation "+continuation)
        continuation.resume(Unit) }, time, unit)
}