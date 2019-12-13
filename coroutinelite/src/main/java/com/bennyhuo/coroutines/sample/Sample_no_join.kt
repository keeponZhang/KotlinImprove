package com.bennyhuo.coroutines.sample

import com.bennyhuo.coroutines.lite.delay
import com.bennyhuo.coroutines.lite.launch
import com.bennyhuo.coroutines.lite.launch2
import com.bennyhuo.coroutines.lite.runBlocking
import com.bennyhuo.coroutines.utils.log
import java.lang.RuntimeException
import kotlin.coroutines.experimental.EmptyCoroutineContext

//这里不调用join
fun main(args: Array<String>) {
    log(1)
    launch2(EmptyCoroutineContext) {
        val job = launch {
            log(1)
            throw RuntimeException("keepon")
            log(2)
        }
        log(3)
    }
}

