package com.bennyhuo.coroutines.sample.launchblock

import com.bennyhuo.coroutines.lite.delay
import com.bennyhuo.coroutines.lite.launch
import com.bennyhuo.coroutines.lite.runBlocking
import com.bennyhuo.coroutines.utils.log

//其实runBlocking主要是创造一个协程环境，runBlocking可以调用挂起函数
fun main(args: Array<String>) = runBlocking {
    launch {
        log(-1)
        delay(1000)
        log(-3)
    }
    log(2)
    true
}
//runBlocking不会一直阻塞住，这个例子中log(-3)没有执行到

