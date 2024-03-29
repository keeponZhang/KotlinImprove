package com.bennyhuo.coroutines.sample

import com.bennyhuo.coroutines.utils.log
import kotlinx.coroutines.experimental.CompletableDeferred
import kotlinx.coroutines.experimental.launch
import kotlinx.coroutines.experimental.runBlocking
import kotlin.concurrent.thread

interface Callback {
    fun onSuccess(result: String)
    fun onError(e: Throwable)
}

fun loadAsync(callback: Callback) {
    thread {
        try {
            Thread.sleep(1000)
            if(Math.random() > 0.5f){
                callback.onSuccess("HelloWorld")
            } else {
                throw IllegalStateException("This is a Demonstration Error.")
            }
        } catch (e: Throwable) {
            callback.onError(e)
        }
    }
}

//异步操作变成了同步挂起操作
suspend fun load(): String {
    val completableDeferred = CompletableDeferred<String>()
    loadAsync(object : Callback {
        override fun onSuccess(result: String) {
            completableDeferred.complete(result)
        }

        override fun onError(e: Throwable) {
            completableDeferred.completeExceptionally(e)
        }
    })
    //注意这里
    return completableDeferred.await()
}

fun main(args: Array<String>) = runBlocking {
    log(-1)
    launch {
        log(1)
        try {
            val result = load()
            log(2.1)
            log(result)
        } catch (e: Exception) {
            log(2.2)
            log(e)
        }
        log(3)
    }.join()
    log(-2)
}