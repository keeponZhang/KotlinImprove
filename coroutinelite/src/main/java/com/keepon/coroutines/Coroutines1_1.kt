package com.keepon.coroutines;

import android.os.Build
import com.keepon.coroutines.bean.Post
import com.keepon.coroutines.bean.Token
import java.util.concurrent.Callable
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

/**
 * createBy	 keepon  https://www.jianshu.com/p/2659bbe0df16
 */
//1. 为什么需要协程？
//	异步编程中最为常见的场景是：在后台线程执行一个复杂任务，下一个任务依赖于上一个任务的执行结果，
//	所以必须等待上一个任务执行完成后才能开始执行。看下面代码中的三个函数，后两个函数都依赖于前一个函数的执行结果。
private val executor = Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors()) { runnable ->
    Thread(runnable, "CommonPool").apply { isDaemon = true }
}

fun requestToken0(): Token {
    // makes request for a token & waits
    var token:Token ;

    println(" requestToken0 "+Thread.currentThread().name)
    val submit = executor.submit(Callable<Token>(){
        Thread.sleep(1000)
        println("requestTokening")
         Token()
    })
    token =  submit.get()
    return token // returns result when received
}

fun createPost0(token: Token, item:String): Post {
    // sends item to the server & waits
    var post = Post()
    println("createPost0 "+Thread.currentThread().name)
    return post // returns resulting post
}

fun processPost0(post: Post) {
    println("processPost0 "+Thread.currentThread().name)
    // does some local processing of result
}
//三个函数中的操作都是耗时操作，因此不能直接在 UI 线程中运行，而且后两个函数都依赖于前一个函数的执行结果，三个任务不能并行运行，该如何解决这个问题呢？
//1.1 回调
//常见的做法是使用回调，把之后需要执行的任务封装为回调。
fun requestTokenAsync1():  Token {
    println("getToken")
    var token:Token
    val submit = executor.submit(Callable<Token>(){
        Thread.sleep(1000)
        println("requestTokenAsync1")
        Token()
    })
    return submit.get()
}
fun createPostAsync1(token: Token, item:String):Post {
    println("通过token拿post")
    return Post()
}

fun processPost1(post: Post) {
    println("处理post")

}

//fun main(args: Array<String>) {
//    postItem1("keepon")
//    postItem2("keepon")
//    postItem3("keepon")
//}
fun postItem1(item: String) {
    //这里的意思是，先处理requestTokenAsync，再在requestTokenAsync的回调调用createPostAsync，
//    再在createPostAsync的回调调用processPost2
    requestTokenAsync1().let { it->
        createPostAsync1(it,item).let {
            processPost1(it)
        }
    }
}


fun requestTokenAsync2(cb: (Token) -> Unit) {
    println("getToken2")
}
fun createPostAsync2(token: Token, item: String, cb: (Post) -> Unit) {
    println("通过token拿post2")
}
fun processPost2(post: Post) {
    println("处理post2")
}

fun postItem2(item: String) {
    val myToken = Token()
    requestTokenAsync2 { myToken ->
        println("-----")
        createPostAsync2(myToken, item) { post ->
            processPost2(post)
        }
    }
}


fun requestTokenAsync3(): CompletableFuture<Token>? {
    println("getToken2")
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        CompletableFuture<Token>()
    } else {
        return null;
    };
}
fun createPostAsync3(token: Token, item:String): CompletableFuture<Post>? {  println("通过token拿post3")
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        CompletableFuture<Post>()
    } else {
        return null;
    };}
fun processPost3(post: Post) {   println("处理post3") }
fun postItem3(item: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        requestTokenAsync3()!!
                .thenCompose { token -> createPostAsync3(token, item) }
                .thenAccept { post -> processPost3(post) }
                .exceptionally { e ->
                    e.printStackTrace()
                    null
                }
    }
}

//回调在只有两个任务的场景是非常简单实用的，很多网络请求框架的 onSuccess Listener 就是使用回调，但是在三个以上任务的场景中就会出现多层回调嵌套的问题，而且不方便处理异常。



























