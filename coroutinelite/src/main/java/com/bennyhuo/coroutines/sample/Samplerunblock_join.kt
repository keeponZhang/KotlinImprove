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
        log(-2)
    }
    log(1)
    job.join()
    log(3)
    true
}






