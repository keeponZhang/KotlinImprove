package com.bennyhuo.github.network

import com.bennyhuo.github.AppContext
import com.bennyhuo.github.common.ext.ensureDir
import com.bennyhuo.github.network.compat.enableTls12OnPreLollipop
import com.bennyhuo.github.network.interceptors.AcceptInterceptor
import com.bennyhuo.github.network.interceptors.AuthInterceptor
import com.bennyhuo.github.network.interceptors.CacheInterceptor
import com.bennyhuo.github.network.services.TLSSocketFactory
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.experimental.CoroutineCallAdapterFactory
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory2
import retrofit2.converter.gson.GsonConverterFactory
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.io.File
import java.util.concurrent.TimeUnit

//加了private，不会生成get方法
private const val BASE_URL = "http://api.github.com"

//通过一个 QueryParameter 让 CacheInterceptor 添加 no-cache
//const会将private变成public，top-lelve属性没加const时，直接是在编译后的java类名下调用getter
const val FORCE_NETWORK = "forceNetwork"
val FORCE_NETWORK2 = "forceNetwork"

private val cacheFile by lazy {
    File(AppContext.cacheDir, "webServiceApi").apply { ensureDir() }
}

//object,顶层函数，伴生对象都是会生成静态的，加了const，就变成真正静态的，否则是通过get方法获取的
object test1 {
    const val name: String = "liuliqianxiao"
    val name1: String = "liuliqianxiao"
}

class Person {
    companion object {
        const val sex: Int = 1
        val sex1: Int = 1
    }
}

fun main(args: Array<String>) {
    //
    System.out.println(test1.name);
    System.out.println(test1.name1);
    System.out.println(Person.Companion.sex);
    System.out.println(Person.Companion.sex1);
}

val retrofit by lazy {
    Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .addCallAdapterFactory(
            RxJavaCallAdapterFactory2.createWithSchedulers(
                Schedulers.io(),
                AndroidSchedulers.mainThread()
            )
        )
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(
            OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .cache(Cache(cacheFile, 1024 * 1024 * 1024))
                .addInterceptor(AcceptInterceptor())
                .addInterceptor(AuthInterceptor())
                .addInterceptor(CacheInterceptor())
                .addInterceptor(HttpLoggingInterceptor().setLevel(Level.BODY))
                .enableTls12OnPreLollipop()
                .sslSocketFactory(TLSSocketFactory())
                .build()
        )
        .baseUrl(BASE_URL)
        .build()
}