package com.bennyhuo.协程理解.理解suspend方法2.kt

import android.os.SystemClock
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Unconfined
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch

/**
 *createBy keepon
 */
//如果上述功能改用协程, 将会是:

fun main(args: Array<String>) {
    println("开始时间：" + System.currentTimeMillis())
    launch(Unconfined) {
        // 请重点关注协程里是如何获取异步数据的
        val data = requestDataAsync()  // 异步回来的数据, 像同步一样return了
        println("data is $data 返回时间" + System.currentTimeMillis())
    }

    Thread.sleep(10000L) // 请不要关注这个sleep
}

suspend fun requestDataAsync(): String { // 请注意方法前多了一个suspend关键字
    return async(CommonPool) {
        // 先不要管这个async方法, 后面解释
        Thread.sleep(1000L)
        "使用协程方法返回的数据"
        // return data, lambda里的return要省略
    }.await()
}


//这里, 我们首先将requestDataAsync转成了一个suspend方法, 其原型的变化是:
//
//在前加了个suspend关键字.
//去除了原来的callback参数.
//这里先不去深究这个方法的新实现, 后面会专门解释.
//这里需要关注的点是: 在协程里面, 调用suspend方法, 异步的数据像同步一样般return了.
//这是怎么做到的呢?
//当程序执行到requestDataAsync内部时, 通过async启动了另外一个新的子协程去拉取数据, 启动这个新的子协程后, 当前的父协程就挂起了, 此时requestDataAsync还没有返回.
//子协程一直在后台跑, 过了一段时间, 子协程把数据拉回来之后, 会恢复它的父协程, 父协程继续执行, requestDataAsync就把数据返回了.
//



