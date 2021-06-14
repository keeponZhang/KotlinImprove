package com.bennyhuo.github

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.support.multidex.MultiDex
import android.support.v7.app.AppCompatDelegate
import android.util.Log
import com.bennyhuo.swipefinishable.SwipeFinishable
import com.bennyhuo.tieguanyin.runtime.core.ActivityBuilder
import java.security.NoSuchAlgorithmException
import javax.net.ssl.SSLContext

//延迟初始化
private lateinit var INSTANCE: Application

class App: Application() {
    override fun onCreate() {
        super.onCreate()
        //这里调用会奔溃，Settings2间接依赖INSTANCE
//        Settings2.email
        INSTANCE = this
        Log.e("TAG", "App onCreate:" );
        ActivityBuilder.INSTANCE.init(this)
        SwipeFinishable.INSTANCE.init(this)
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun attachBaseContext(base: Context?) {
        MultiDex.install(base)
        super.attachBaseContext(base)
    }
}

//自成一类又只有一个实例对象
object AppContext: ContextWrapper(INSTANCE){

    init {
        Log.e("TAG","init")
    }
}

//fun initializeSSLContext(mContext: Context) {
//    try {
//        SSLContext.getInstance("TLSv1.2")
//    } catch (e: NoSuchAlgorithmException) {
//        e.printStackTrace()
//    }
//    try {
//        ProviderInstaller.installIfNeeded(mContext.applicationContext)
//    } catch (e: GooglePlayServicesRepairableException) {
//        e.printStackTrace()
//    } catch (e: GooglePlayServicesNotAvailableException) {
//        e.printStackTrace()
//    }
//}