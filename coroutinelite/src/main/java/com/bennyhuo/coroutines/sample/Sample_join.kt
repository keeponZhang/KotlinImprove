package com.bennyhuo.coroutines.sample2_0

import com.bennyhuo.coroutines.lite.delay
import com.bennyhuo.coroutines.lite.launch
import com.bennyhuo.coroutines.lite.launch2
import com.bennyhuo.coroutines.lite.runBlocking
import com.bennyhuo.coroutines.utils.log
import java.lang.RuntimeException
import kotlin.coroutines.experimental.EmptyCoroutineContext

fun main(args: Array<String>) {
    log(1)
    launch2(EmptyCoroutineContext) {
        val job = launch {
            log(1)
            throw RuntimeException("keepon2")
            log(2)
        }
          log(2)
        job.join()
        log(3)
    }
}

//DispatcherContext cont Function1<kotlin.coroutines.experimental.Continuation<? super kotlin.Unit>, java.lang.Object>  element:com.bennyhuo.coroutines.lite.CommonPool@448139f0 continuation.context =com.bennyhuo.coroutines.lite.CommonPool@448139f0  element != this false
//DispatchedContinuation delegate.javaClass class com.bennyhuo.coroutines.sample2_0.Sample2_1Kt$main$1$job$1  0  value=kotlin.Unit
//10:42:06:145 [CommonPool] 1
//AbstractCoroutine join currentState:   State.InComplete
//AbstractCoroutine resumeWithException java.lang.RuntimeException: keepon2       我是com.bennyhuo
// .coroutines.lite.StandaloneCoroutine@62e526a8    State.InComplete(这里依然时候 State
// .InComplete，因为launch里面没有delay操作)
//AbstractCoroutine doOnCompleted before state:   State.Complete
//AbstractCoroutine doOnCompleted 没有设置成功 after state:   State.Complete
//joinSuspend doOnCompleted  continuation kotlin.coroutines.experimental.SafeContinuation@378bf509
//10:42:06:148 [main] 3
//AbstractCoroutine 结束调用 resume kotlin.Unit       我是com.bennyhuo.coroutines.lite.StandaloneCoroutine2@2d98a335    State.InComplete
//（使用join后）第一个launch每次都是最后结束