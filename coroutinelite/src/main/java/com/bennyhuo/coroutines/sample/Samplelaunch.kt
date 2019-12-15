package com.bennyhuo.coroutines.sample.launch

import com.bennyhuo.coroutines.lite.launch
import com.bennyhuo.coroutines.utils.log

fun main(args: Array<String>) {
    launch {
        log(-1)
        log(-2)
        log(-3)
    }
    log(2)
    Thread.sleep(50000)
    true
}

