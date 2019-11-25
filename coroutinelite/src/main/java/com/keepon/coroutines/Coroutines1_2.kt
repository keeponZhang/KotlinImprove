package com.keepon.coroutines;

import android.content.ClipData
import com.keepon.coroutines.bean.Post
import com.keepon.coroutines.bean.Token


//先看官方文档的描述
//协程通过将复杂性放入库来简化异步编程。程序的逻辑可以在协程中顺序地表达，而底层库会为我们解决其异步性。该库可以将用户代码的相关部分包装为回调、
//订阅相关事件、在不同线程（甚至不同机器）上调度执行，而代码则保持如同顺序执行一样简单。
//
//协程的开发人员 Roman Elizarov 是这样描述协程的：协程就像非常轻量级的线程。
//线程是由系统调度的，线程切换或线程阻塞的开销都比较大。而协程依赖于线程，但是协程挂起时不需要阻塞线程，
//几乎是无代价的，协程是由开发者控制的。所以协程也像用户态的线程，非常轻量级，一个线程中可以创建任意个协程。
//
//总而言之：协程可以简化异步编程，可以顺序地表达程序，协程也提供了一种避免阻塞线程并用更廉价、更可控的操作替代线程阻塞的方法 -- 协程挂起。
//
//3.1 挂起函数
//
suspend fun requestToken5(): Token { return  Token() }   // 挂起函数
suspend fun createPost5(token: Token, item: ClipData.Item): Post {
    return Post()
}  // 挂起函数
fun processPost5(post: Post) { }
//
//requestToken和createPost函数前面有suspend修饰符标记，这表示两个函数都是挂起函数。
//挂起函数能够以与普通函数相同的方式获取参数和返回值，但是调用函数可能挂起协程
//（如果相关调用的结果已经可用，库可以决定继续进行而不挂起），挂起函数挂起协程时，不会阻塞协程所在的线程。
//挂起函数执行完成后会恢复协程，后面的代码才会继续执行。但是挂起函数只能在协程中或其他挂起函数中调用。
//事实上，要启动协程，至少要有一个挂起函数，它通常是一个挂起 lambda 表达式。所以suspend修饰符可以标记普通函数、扩展函数和 lambda 表达式。
//
//挂起函数只能在协程中或其他挂起函数中调用，上面例子中launch函数就创建了一个协程。


fun postItem5(item: ClipData.Item) {
//   launch { // 创建一个新协程
//        val token = requestToken5()
//        val post = createPost5(token, item)
//        processPost(post)
//        // 需要异常处理，直接加上 try/catch 语句即可
//    }

//    GlobalScope.launch(Dispatchers.Main) { // 在 UI 线程创建一个新协程
//        val token = requestToken5()
//        val post = createPost5(token, item)
//        processPost5(post)
//    }
}

//public actual fun launch(
//        context: CoroutineContext = DefaultDispatcher,
//        start: CoroutineStart = CoroutineStart.DEFAULT,
//        parent: Job? = null,
//        block: suspend CoroutineScope.() -> Unit
//): Job

//public fun CoroutineScope.launch(
//        context: CoroutineContext = EmptyCoroutineContext,
//        start: CoroutineStart = CoroutineStart.DEFAULT,
//        block: suspend CoroutineScope.() -> Unit
//): Job

//CoroutineScope
//从上面函数定义中可以看到协程的一些重要的概念：CoroutineContext、CoroutineDispatcher、Job，下面来一一介绍这些概念。

//3.1 CoroutineScope 和 CoroutineContext
//CoroutineScope，可以理解为协程本身，包含了 CoroutineContext。
//CoroutineContext，协程上下文，是一些元素的集合，主要包括 Job 和 CoroutineDispatcher 元素，可以代表一个协程的场景。
//EmptyCoroutineContext 表示一个空的协程上下文。
//
//3.2 CoroutineDispatcher
//CoroutineDispatcher，协程调度器，决定协程所在的线程或线程池。
//它可以指定协程运行于特定的一个线程、一个线程池或者不指定任何线程
//（这样协程就会运行于当前线程）。coroutines-core中 CoroutineDispatcher
//有三种标准实现Dispatchers.Default、Dispatchers.IO，Dispatchers.Main和Dispatchers.Unconfined，Unconfined 就是不指定线程。
//
//launch函数定义如果不指定CoroutineDispatcher或者没有其他的ContinuationInterceptor，
//默认的协程调度器就是Dispatchers.Default，Default是一个协程调度器，其指定的线程为共有的线程池，线程数量至少为 2 最大与 CPU 数相同。




//3.3 Job & Deferred
//Job，任务，封装了协程中需要执行的代码逻辑。Job 可以取消并且有简单生命周期，它有三种状态：
//
//State	                                [isActive]	[isCompleted]	[isCancelled]
//New (optional initial state)	           false	    false	          false
//Active (default initial state)	           true	        false	          false
//Completing (optional transient state)	   true	        false	          false
//Cancelling (optional transient state)	   false	    false	          true
//Cancelled (final state)	                   false	    true	          true
//Completed (final state)	                   false	    true	          false
//Job 完成时是没有返回值的，如果需要返回值的话，应该使用 Deferred，它是 Job 的子类public interface Deferred<out T> : Job。

//3.4 Coroutine builders
//CoroutineScope.launch函数属于协程构建器 Coroutine builders，Kotlin 中还有其他几种 Builders，负责创建协程。

//3.4.1 CoroutineScope.launch {}
//CoroutineScope.launch {} 是最常用的 Coroutine builders，不阻塞当前线程，在后台创建一个新协程，也可以指定协程调度器，例如在 Android 中常用的GlobalScope.launch(Dispatchers.Main) {}。
//
//3.4.2 runBlocking {}
//runBlocking {}是创建一个新的协程同时阻塞当前线程，直到协程结束。这个不应该在协程中使用，主要是为main函数和测试设计的。



// fun main(args: Array<String>) = runBlocking { // start main coroutine
//    launch { // launch new coroutine in background and continue
//        delay(1000L)
//        println("World!")
//    }
//    println("Hello,") // main coroutine continues here immediately
//    delay(2000L)      // delaying for 2 seconds to keep JVM alive


//    val time = measureTimeMillis {
//        val one = async { doSomethingUsefulOne() }  // start async one coroutine without suspend main coroutine
//        val two = async { doSomethingUsefulTwo() }  // start async two coroutine without suspend main coroutine
//        println("The answer is ${one.await()}"+ " and ${ two.await()}") // suspend main coroutine for waiting two async coroutines to finish
//    }
//    println("Completed in $time ms")
//}

fun doSomethingUsefulTwo(): Any {
    return "1"
}

fun doSomethingUsefulOne(): Any {
    return "2"
}
//3.4.3 withContext {}
//withContext {}不会创建新的协程，在指定协程上运行挂起代码块，并挂起该协程直至代码块运行完成。

//3.4.4 async {}
//CoroutineScope.async {}可以实现与 launch builder 一样的效果，在后台创建一个新协程，唯一的区别是它有返回值，因为CoroutineScope.async {}返回的是 Deferred 类型。
//获取CoroutineScope.async {}的返回值需要通过await()函数，它也是是个挂起函数，调用时会挂起当前协程直到 async 中代码执行完并返回某个值。


















































