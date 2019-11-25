package com.keepon.coroutines

import kotlinx.coroutines.experimental.*


/**
 * createBy	 keepon
 */
fun main(args: Array<String>) {
    test4()
//    test3()

//    test2()

//   test1()
}

fun test4() {
    launch { // 默认继承 parent coroutine 的 CoroutineDispatcher，运行commonPool
        println("main runBlocking: I'm working in thread ${Thread.currentThread().name}")
        delay(100)
        println("main runBlocking: After delay in thread ${Thread.currentThread().name}")
    }
    launch(Unconfined) {
        println("Unconfined      : I'm working in thread ${Thread.currentThread().name}")
        delay(100)
        println("Unconfined      : After delay in thread ${Thread.currentThread().name}")
    }
    Thread.sleep(1000)
}

fun test3() {
    val coroutineDispatcher = newSingleThreadContext("ctx")
    // 启动协程 1
    launch(coroutineDispatcher) {
        println("the first coroutine "+Thread.currentThread().name)
        delay(200)
        println("the first coroutine "+Thread.currentThread().name)
    }
    // 启动协程 2
    launch(coroutineDispatcher) {
        println("the second coroutine "+Thread.currentThread().name)
        delay(100)
        println("the second coroutine "+Thread.currentThread().name)
    }
    // 保证 main 线程存活，确保上面两个协程运行完成
    Thread.sleep(500)
}


fun test2() {
    launch {
        //async { requestToken0() } //新建一个协程，可能在另一个线程运行
        // 但是 await() 是挂起函数，当前协程执行逻辑卡在第一个分支，第一种状态，当 async 的协程执行完后恢复当前协程，才会切换到下一个分支
        //在这里调用await没什么优势，但是await()可以在下面调用
        val token = async { requestToken0() }.await()
        // 在第二个分支状态中，又新建一个协程，使用 await 挂起函数将之后代码作为 Continuation 放倒下一个分支状态，直到 async 协程执行完
        val post = async { createPost0(token, "keepon") }.await()
        // 最后一个分支状态，直接在当前协程处理
        processPost0(post)
    }

    Thread.sleep(3000)
}

private fun test1() {
    launch {
        val token = requestToken0()
        val post = createPost0(token, "keepn")
        processPost0(post)
    }
    Thread.sleep(3000)
}