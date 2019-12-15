package com.bennyhuo.coroutines.sample2_0

import com.bennyhuo.coroutines.lite.launch
import com.bennyhuo.coroutines.lite.launch2
import com.bennyhuo.coroutines.utils.log
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

