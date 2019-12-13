package com.bennyhuo.协程理解.理解suspend方法.kt

import android.os.SystemClock

/**
 *createBy keepon
 */
//suspend方法是协程的核心, 理解suspend方法是使用和理解协程的关键.
//(suspend lambda和suspend方法差不多, 只是没有名字, 不再单独介绍了)
//
//suspend方法的语法很简单, 只是比普通方法只是多了个suspend关键字:

//suspend fun foo(): ReturnType {
//    // ...
//}


//suspend方法只能在协程里面调用, 不能在协程外面调用.
//suspend方法本质上, 与普通方法有较大的区别, suspend方法的本质是异步返回(注意: 不是异步回调). 后面我们会解释这句话的含义.

//现在, 我们先来看一个异步回调的例子:
fun main(args: Array<String>) {
    println("开始时间：" + System.currentTimeMillis())
    requestDataAsync {
        println("data is $it  返回时间" + System.currentTimeMillis())
    }
    println("2")
    Thread.sleep(10000L)  // 这个sleep只是为了保活进程
}

fun requestDataAsync(callback: (String) -> Unit) {
    Thread() {
        Thread.sleep(1000L)
        callback("使用异步回调返回了数据")
    }.start()
}

//逻辑很简单, 就是通过异步的方法拉一个数据, 然后使用这个数据, 按照以往的编程方式, 若要接受异步回来的数据, 唯有使用callback.
//但是假如使用协程, 可以不使用callback, 而是直接把这个数据”return”回来, 调用者不使用callback接受数据, 而是像调用同步方法一样接受返回值









































