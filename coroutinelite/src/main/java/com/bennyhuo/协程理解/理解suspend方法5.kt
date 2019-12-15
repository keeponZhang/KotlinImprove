package com.bennyhuo.协程理解

import kotlinx.coroutines.experimental.Unconfined
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

/**
 * createBy	 keepon
 */
fun main(args: Array<String>) {
    // 1. 程序开始
    println("${Thread.currentThread().name}: 1");

    // 2. 启动一个协程, 并立即启动
    launch(Unconfined) { // Unconfined意思是在当前线程(主线程)运行协程
        // 3. 本协程在主线程上直接开始执行了第一步
        println("${Thread.currentThread().name}: 2");

        /* 4. 本协程的第二步调用了一个suspend方法, 调用之后,
         * 本协程就放弃执行权, 遣散运行我的线程(主线程)请干别的去.
         *
         * delay被调用的时候, 在内部创建了一个计时器, 并设了个callback.
         * 1秒后计时器到期, 就会调用刚设置的callback.
         * 在callback里面, 会调用系统的接口来恢复协程.
         * 协程在计时器线程上恢复执行了. (不是主线程, 跟Unconfined有关)
         */
        delay(1000L)  // 过1秒后, 计时器线程会resume协程

        // 7. 计时器线程恢复了协程,
        println("${Thread.currentThread().name}: 4")
    }

    // 5. 刚那个的协程不要我(主线程)干活了, 所以我继续之前的执行
    println("${Thread.currentThread().name}: 3");

    // 6. 我(主线程)睡2秒钟
    Thread.sleep(2000L)

    // 8. 我(主线程)睡完后继续执行
    println("${Thread.currentThread().name}: 5");
}
//main: 1
//main: 2
//main: 3
//kotlinx.coroutines.ScheduledExecutor: 4
//main: 5

//上述代码的注释详细的列出了程序运行流程, 看完之后, 应该就能明白 “遣散” 和 “放弃执行权” 的含义了.
//Unconfined的含义是不给协程指定运行的线程, 逮到谁就使用谁, 启动它的线程直接执行它, 但被挂起后,
//会由恢复它的线程继续执行, 如果一个协程会被挂起多次, 那么每次被恢复后, 都可能被不同线程继续执行.


//现在再来回顾刚刚那句: suspend方法的本质就是异步返回.
//含义就是将其拆成 “异步” + “返回”:
//
//首先, 数据不是同步回来的(同步指的是立即返回), 而是异步回来的.
//其次, 接受数据不需要通过callback, 而是直接接收返回值.


//调用suspend方法的详细流程是:
//在协程里, 如果调用了一个suspend方法, 协程就会挂起, 释放自己的执行权, 但在协程挂起之前, suspend方法内部一般会启动了另一个线程或协程, 我们暂且称之为”分支执行流”吧, 它的目的是运算得到一个数据.
//当suspend方法里的*分支执行流”完成后, 就会调用系统API重新恢复协程的执行, 同时会数据返回给协程(如果有的话).
