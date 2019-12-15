package com.bennyhuo.协程理解.初识协程.kt

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking

/**
 *createBy keepon
 */
//https://blog.csdn.net/BeyondWorlds/article/details/79866611
fun main(args: Array<String>)  = runBlocking{
    launch(CommonPool) {
        delay(1000L)
        println("World!")
    }
    println("Hello,")
    Thread.sleep(2000L)
}