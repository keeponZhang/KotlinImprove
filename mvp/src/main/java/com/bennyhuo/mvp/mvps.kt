package com.bennyhuo.mvp

interface IPresenter<out View : IMvpView<IPresenter<View>>> : ILifecycle {
    //这里val 其实不能被重新赋值，BasePresenter 覆写了view
    val view: View
}

interface IMvpView<out Presenter : IPresenter<IMvpView<Presenter>>> : ILifecycle {
    val presenter: Presenter
}

//out在这里同java的（V extends IMvpView2)
//
interface IPresenter2<out V : IMvpView2<IPresenter2<V>>> {
    //    fun setView(view:V)
    val view: V
}

interface IMvpView2<out P : IPresenter2<IMvpView2<P>>> {
//    fun setPresenter(presenter:P)
//    val presenter: P
}