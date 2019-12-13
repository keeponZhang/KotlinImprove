package com.bennyhuo.协程理解.理解suspend方法3.kt

/**
 *createBy keepon
 */
//如果上述功能改用协程, 将会是:

fun main(args: Array<String>) {
    val data = async2Sync()  // 数据是同步返回了, 但是线程也阻塞了
    println("data is $data")
    // Thead.sleep(10000L)  // 这一句在这里毫无意义了, 注释掉
}

var mData = ""
val obj = Object()
private fun async2Sync(): String {
    // 随便创建一个对象当成锁使用
    synchronized(obj) {
        requestDataAsync { data ->
            mData = data;
            obj.notifyAll() // 通知所有的等待者
        }
        obj.wait()
    }


    return mData
}

fun requestDataAsync(callback: (String) -> Unit) {
    Thread {
        run {
            println("获取数据")
            Thread.sleep(1000)
            callback("返回的数据3")
        }
    }.start()
}


