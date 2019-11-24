package com.keepon.coroutines;

import android.content.ClipData
import com.keepon.coroutines.bean.Post
import com.keepon.coroutines.bean.Token
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.experimental.launch


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
   launch { // 创建一个新协程
        val token = requestToken5()
        val post = createPost5(token, item)
        processPost(post)
        // 需要异常处理，直接加上 try/catch 语句即可
    }
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

























































