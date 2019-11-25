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








































































































