package com.bennyhuo.coroutines.sample1_0

import com.bennyhuo.coroutines.lite.async
import com.bennyhuo.coroutines.lite.delay
import com.bennyhuo.coroutines.lite.launch
import com.bennyhuo.coroutines.lite.runBlocking
import com.bennyhuo.coroutines.utils.log

fun main(args: Array<String>) = runBlocking {
    launch {
        log(-1)
    }.join()
    true
}

//AbstractCoroutine join currentState:   State.InComplete
//AbstractCoroutine doOnCompleted before state:   State.InComplete
//16:57:05:987 [CommonPool] -1
//AbstractCoroutine resume kotlin.Unit
//AbstractCoroutine resume kotlin.Unit




