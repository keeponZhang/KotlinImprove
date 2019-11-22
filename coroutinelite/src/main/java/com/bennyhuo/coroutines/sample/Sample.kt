package com.bennyhuo.coroutines.sample

import com.bennyhuo.coroutines.lite.async
import com.bennyhuo.coroutines.lite.delay
import com.bennyhuo.coroutines.lite.launch
import com.bennyhuo.coroutines.lite.runBlocking
import com.bennyhuo.coroutines.utils.log

fun main(args: Array<String>) = runBlocking {
    test1()


//    test2()
}

private suspend fun test2() {
    launch {
        log(-1)
        val result = async {
            log(1)
            delay(100)
            log(2)
            loadForResult().also {
                log(3)
            }
        }
        log(-2)
        delay(200)
        log(-3)
        log(result.await())
        log(-4)
    }.join()
}

fun test1() {
    log(1)
    launch {
        log(-1)
        log("keepon")
        log(-2)
    }
    log(2)
}

suspend fun loadForResult(): String {
    delay(1000L)
    return "HelloWorld"
}