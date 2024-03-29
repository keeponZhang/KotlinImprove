package com.bennyhuo.coroutines.sample1_0

import com.bennyhuo.coroutines.lite.delay
import com.bennyhuo.coroutines.lite.launch2
import com.bennyhuo.coroutines.lite.runBlocking
import com.bennyhuo.coroutines.utils.log

fun main(args: Array<String>) = runBlocking {
    log(0)
    var job = launch2 {
        log(-1)
       delay(100)
//   注释掉这个DispatchedContinuation resume delegate.javaClass class com.bennyhuo
//        .coroutines.sample1_0.Samplerunblock_joinKt$main$1$job$1  0  value=kotlin.Unit
//        ---------------------------会少调用两次（） 每开启一个协程调用一次
        log(-2)
//       delay(100)  //加上这个一共5次，可知，每次开启协程会调用一次，每次恢复协程会调用一次
//        log(-3)
    }
    log(1)
    job.join()
    log(3)
//    Thread.sleep(2000)
    true
}






