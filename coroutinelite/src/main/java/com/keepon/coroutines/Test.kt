package com.keepon.coroutines

import com.bennyhuo.coroutines.lite.launch

/**
 * createBy	 keepon
 */
fun main(args: Array<String>) {
    launch {
        val token = requestToken0()
        val post = createPost0(token, "keepn")
        processPost0(post)
    }
    Thread.sleep(1000)
}