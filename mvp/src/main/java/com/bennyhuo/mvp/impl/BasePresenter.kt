package com.bennyhuo.mvp.impl

import android.content.res.Configuration
import android.os.Bundle
import com.bennyhuo.mvp.IMvpView
import com.bennyhuo.mvp.IPresenter

//interface IPresenter<out View: IMvpView<IPresenter<View>>>: ILifecycle {
abstract class BasePresenter<out V: IMvpView<BasePresenter<V>>>: IPresenter<V> {

    //var有get 和set方法，违反了协变和逆变，但是这里我们确实要用，所以加@UnsafeVariance
    override lateinit var view: @UnsafeVariance V

    override fun onCreate(savedInstanceState: Bundle?) = Unit
    override fun onSaveInstanceState(outState: Bundle) = Unit
    override fun onViewStateRestored(savedInstanceState: Bundle?) = Unit
    override fun onConfigurationChanged(newConfig: Configuration) = Unit
    override fun onDestroy() = Unit
    override fun onStart() = Unit
    override fun onStop() = Unit
    override fun onResume() = Unit
    override fun onPause() = Unit
}