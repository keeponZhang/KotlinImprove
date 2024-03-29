package com.bennyhuo.coroutines.sample.delay

import com.bennyhuo.coroutines.lite.async
import com.bennyhuo.coroutines.lite.delay
import com.bennyhuo.coroutines.lite.launch
import com.bennyhuo.coroutines.lite.runBlocking
import com.bennyhuo.coroutines.lite.runBlocking2
import com.bennyhuo.coroutines.utils.log

fun main(args: Array<String>) {
    launch {
        log(-1)
        delay(1000)
        log(-2)
    }
    log(2)
    Thread.sleep(2000)
    true
}

