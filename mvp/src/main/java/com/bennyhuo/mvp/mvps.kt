package com.bennyhuo.mvp

interface IPresenter<out View: IMvpView<IPresenter<View>>>: ILifecycle{
    val view: View
}

interface IMvpView<out Presenter: IPresenter<IMvpView<Presenter>>>: ILifecycle{
    val presenter: Presenter
}

interface IPresenter2<out V: IMvpView2<IPresenter2<V>>>{
    val view: V
}

interface IMvpView2< out P: IPresenter2<IMvpView2<P>>>{
    val presenter: P
}