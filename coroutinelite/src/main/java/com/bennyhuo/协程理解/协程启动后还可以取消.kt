package com.bennyhuo.协程理解.协程启动后还可以取消.kt

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

/**
 *createBy keepon
 */
fun main(args: Array<String>) {
    val job = launch(CommonPool) {
        var i = 1
        while (true) {
            println("$i little sheep")
            ++i
            delay(500L)  // 每半秒数一只, 一秒可以输两只
        }
    }

    Thread.sleep(1000L)  // 在主线程睡眠期间, 协程里已经数了两只羊
    job.cancel()  // 协程才数了两只羊, 就被取消了
    Thread.sleep(1000L)
    println("main process finished.")
}