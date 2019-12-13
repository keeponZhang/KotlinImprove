package com.bennyhuo.coroutines.lite

import kotlin.coroutines.experimental.CoroutineContext

class StandaloneCoroutine<T>(context: CoroutineContext, block: suspend () -> T): AbstractCoroutine<T>(context, block)
class StandaloneCoroutine2<T>(context: CoroutineContext, block: suspend () -> T):
    AbstractCoroutine<T>(context, block)