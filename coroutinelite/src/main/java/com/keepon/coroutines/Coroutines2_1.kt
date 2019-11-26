package com.keepon.coroutines

import com.keepon.coroutines.bean.Token

/**
 * createBy	 keepon
 */
//1. 挂起函数的工作原理
//协程的内部实现使用了 Kotlin 编译器的一些编译技术，当挂起函数调用时，背后大致细节如下：
//
//挂起函数或挂起 lambda 表达式调用时，都有一个隐式的参数额外传入，这个参数是Continuation类型，封装了协程恢复后的执行的代码逻辑。
//
//用前文中的一个挂起函数为例：

suspend fun requestToken6(): Token {
    return Token()
}

//实际上在 JVM 中更像下面这样：
//Object requestToken(Continuation<Token> cont) { ... }
//Continuation的定义如下，类似于一个通用的回调接口：
///**
// * Interface representing a continuation after a suspension point that returns value of type `T`.
// */
//public interface Continuation<in T> {
//    /**
//     * Context of the coroutine that corresponds to this continuation.
//     */
//    public val context: CoroutineContext
//
//    /**
//     * Resumes the execution of the corresponding coroutine passing successful or failed [result] as the
//     * return value of the last suspension point.
//     */
//    public fun resumeWith(result: Result<T>)
//}
//现在再看之前postItem2函数：
fun postItem4(item: String) {
    //1.3
//    GlobalScope.launch {
//        val token = requestToken0()
//        val post = createPost0(token, item)
//        processPost0(post)
//    }
}
//然而，协程内部实现不是使用普通回调的形式，而是使用状态机来处理不同的挂起点，大致的 CPS(Continuation Passing Style) 代码为：
// 编译后生成的内部类大致如下
//final class postItem$1 extends SuspendLambda  {
//    public final Object invokeSuspend(Object result) {
//        ...
//        switch (this.label) {
//            case 0:
//            this.label = 1;
//            token = requestToken(this)
//            break;
//            case 1:
//            this.label = 2;
//            Token token = result;
//            post = createPost(token, this.item, this)
//            break;
//            case 2:
//            Post post = result;
//            processPost(post)
//            break;
//        }
//    }
//}

//上面代码中每一个挂起点和初始挂起点对应的 Continuation 都会转化为一种状态，协程恢复只是跳转到下一种状态中。挂起函数将执行过程分为多个 Continuation 片段，
//并且利用状态机的方式保证各个片段是顺序执行的。


//1.1 挂起函数可能会挂起协程
//挂起函数使用 CPS style 的代码来挂起协程，保证挂起点后面的代码只能在挂起函数执行完后才能执行，所以挂起函数保证了协程内的顺序执行顺序。
//
//在多个协程的情况下，挂起函数的作用更加明显：
//launch {
//    //async { requestToken0() } //新建一个协程，可能在另一个线程运行
//    // 但是 await() 是挂起函数，当前协程执行逻辑卡在第一个分支，第一种状态，当 async 的协程执行完后恢复当前协程，才会切换到下一个分支
//    //在这里调用await没什么优势，但是await()可以在下面调用
//    val token = async { requestToken0() }.await()
//    // 在第二个分支状态中，又新建一个协程，使用 await 挂起函数将之后代码作为 Continuation 放倒下一个分支状态，直到 async 协程执行完
//    val post = async { createPost0(token, "keepon") }.await()
//    // 最后一个分支状态，直接在当前协程处理
//    processPost0(post)
//}


//上面的例子中，await()挂起函数挂起当前协程，直到异步协程完成执行，但是这里并没有阻塞线程，是使用状态机的控制逻辑来实现。
//而且挂起函数可以保证挂起点之后的代码一定在挂起点前代码执行完成后才会执行，挂起函数保证顺序执行，所以异步逻辑也可以用顺序的代码顺序来编写。
//
//注意挂起函数不一定会挂起协程，如果相关调用的结果已经可用，库可以决定继续进行而不挂起，
//例如async { requestToken() }的返回值Deferred的结果已经可用时，await()挂起函数可以直接返回结果，不用再挂起协程。


//1.2 挂起函数不会阻塞线程
//挂起函数挂起协程，并不会阻塞协程所在的线程，例如协程的delay()挂起函数会暂停协程一定时间，并不会阻塞协程所在线程，但是Thread.sleep()函数会阻塞线程。
//看下面一个例子，两个协程运行在同一线程上：
//fun main(args: Array<String>) {
//    // 创建一个单线程的协程调度器，下面两个协程都运行在这同一线程上
//    val coroutineDispatcher = newSingleThreadContext("ctx")
//    // 启动协程 1
//    GlobalScope.launch(coroutineDispatcher) {
//        println("the first coroutine")
//        delay(200)
//        println("the first coroutine")
//    }
//    // 启动协程 2
//    GlobalScope.launch(coroutineDispatcher) {
//        println("the second coroutine")
//        delay(100)
//        println("the second coroutine")
//    }
//    // 保证 main 线程存活，确保上面两个协程运行完成
//    Thread.sleep(500)
//}
//
//运行结果为：
//
//the first coroutine
//the second coroutine
//the second coroutine
//the first coroutine

//从上面结果可以看出，当协程 1 暂停 200 ms 时，线程并没有阻塞，而是执行协程 2 的代码，然后在 200 ms 时间到后，继续执行协程 1 的逻辑。
//所以挂起函数并不会阻塞线程，这样可以节省线程资源，协程挂起时，线程可以继续执行其他逻辑。

//1.3 挂起函数恢复协程后运行在哪个线程
//协程的所属的线程调度在前一篇文章《协程简介》中有提到过，主要是由协程的CoroutineDispatcher控制，CoroutineDispatcher可以指定协程运行在某一特定线程上
//、运作在线程池中或者不指定所运行的线程。所以协程调度器可以分为Confined dispatcher和Unconfined dispatcher，Dispatchers.Default、Dispatchers.IO
//和Dispatchers.Main属于Confined dispatcher，都指定了协程所运行的线程或线程池，挂起函数恢复后协程也是运行在指定的线程或线程池上的，而Dispatchers.Unconfined属于Unconfined dispatcher，协程启动并运行在 Caller Thread 上，但是只是在第一个挂起点之前是这样的，挂起恢复后运行在哪个线程完全由所调用的挂起函数决定。

//test4()
//main runBlocking: I'm working in thread ForkJoinPool.commonPool-worker-1
//Unconfined      : I'm working in thread main
//Unconfined      : After delay in thread kotlinx.coroutines.DefaultExecutor
//main runBlocking: After delay in thread ForkJoinPool.commonPool-worker-1
//上面第三行输出，经过delay挂起函数后，使用Dispatchers.Unconfined的协程挂起恢复后依然在delay函数使用的DefaultExecutor上。




























































































