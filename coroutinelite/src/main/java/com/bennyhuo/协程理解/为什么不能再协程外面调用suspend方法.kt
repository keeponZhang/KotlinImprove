package com.bennyhuo.协程理解

/**
 * createBy	 keepon
 */
//suspend方法只能在协程里面调用, 原因是只有在协程里, 才能遣散当前线程, 在协程外面, 不允许遣散, 反过来思考, 假如在协程外面也能遣散线程, 会怎么样, 写一个反例:


//fun main(args: Array<String>) {
//    requestDataSuspend();
//    doSomethingNormal();
//}
//suspend fun requestDataSuspend() {
//    // ...
//}
//fun doSomethingNormal() {
//    // ...
//}

//requestDataSuspend是suspend方法, doSomethingNormal是正常方法,
//doSomethingNormal必须等到requestDataSuspend执行完才会开始,
//后果main方法失去了并行的能力, 所有地方都失去了并行的能力, 这肯定不是我们要的,
//所以需要约定只能在协程里才可以遣散线程, 放弃执行权, 于是suspend方法只能在协程里面调用.




