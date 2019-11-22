package com.bennyhuo.experimental.coroutines.sample;

import com.bennyhuo.coroutines.sample.CoroutineInterop;
import com.bennyhuo.coroutines.utils.LogKt;

public class Ex10_JavaInterop {
    public static void main(String... args) {
        try {
            LogKt.log(CoroutineInterop.loadString());
            LogKt.log(CoroutineInterop.loadFuture().get());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
