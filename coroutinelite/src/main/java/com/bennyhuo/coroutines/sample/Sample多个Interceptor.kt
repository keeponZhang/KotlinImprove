package com.bennyhuo.coroutines.sample0

import com.bennyhuo.coroutines.lite.async
import com.bennyhuo.coroutines.lite.delay
import com.bennyhuo.coroutines.lite.launch
import com.bennyhuo.coroutines.lite.launch多个Interceptor
import com.bennyhuo.coroutines.lite.launch多个Interceptor2
import com.bennyhuo.coroutines.lite.runBlocking
import com.bennyhuo.coroutines.lite.runBlocking2
import com.bennyhuo.coroutines.utils.log

fun main(args: Array<String>) {
    launch多个Interceptor2 {
        log(-1)
        log(-2)
    }
    log(2)
    true
}
//launch 里面没有调用挂起函数
//18:21:45:259 [main] 2
//18:21:45:259 [CommonPool] -1
//AbstractCoroutine resume kotlin.Unit       我是com.bennyhuo.coroutines.lite.StandaloneCoroutine@55f9807a
//AbstractCoroutine resume true       我是com.bennyhuo.coroutines.lite.BlockingCoroutine@33c7353a
