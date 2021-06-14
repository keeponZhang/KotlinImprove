package com.bennyhuo.mvp.impl

import android.os.Bundle
import android.support.v4.app.Fragment
import com.bennyhuo.mvp.IMvpView
import com.bennyhuo.mvp.IPresenter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.coroutines.experimental.buildSequence
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.primaryConstructor
import kotlin.reflect.jvm.jvmErasure

//不要自定以构造函数
abstract class BaseFragment<out P : BasePresenter<BaseFragment<P>>> : IMvpView<P>, Fragment() {
    //负责实例化presenter
    override val presenter: P

    init {
        //fragment实例化的时候就要实例化presenter，这里就实现了view持有presenter的引用，presenter持有view的引用
        presenter = createPresenterKt()
        presenter.view = this
    }

    private fun createPresenterKt(): P {
        if (true) {
            return createPresenter()
        }
        buildSequence {
            var thisClass: KClass<*> = this@BaseFragment::class
            //这里是个死循环
            while (true) {
                //拿到所有父类
                println("-------------" + thisClass.simpleName + "  ${thisClass.supertypes}")
                //包括接口的
                yield(thisClass.supertypes)
                thisClass = thisClass.supertypes.firstOrNull()?.jvmErasure ?: break
            }
        }.flatMap {
            //泛型参数的List
            it.flatMap {
                it.arguments
            }.asSequence()
        }.first {
            //这里要筛选出IPresenter
            it.type?.jvmErasure?.isSubclassOf(IPresenter::class) ?: false
        }.let {
            return it.type!!.jvmErasure.primaryConstructor!!.call() as P
        }
    }

    //下面是java实现
    //Type类型,Type是一个接口(Java所有类型都会继承这个接口，Class，ParameterizedType，实现了这个接口)
    //	Type只有五种类型：
//	Class:所代表的是一个确定的类，比如Integer,String,Double等
//	ParameterizedType:ParameterizedType代表完整的泛型表达式
//	TypeVariable:TypeVariable代表泛型变量的符号即T,U等
//	WildcardType:WildcardType代表通配符,<? extends Integer>,<? super String>,或者<?>等
//	GenericArrayType:GenericArrayType代表数组类型


    //type:com.zhang.fanshe.bean.Point<java.lang.Integer>
    //val type = clazz.getGenericSuperclass()

    //其实type上面也是一个ParameterizedType,代表一个泛型类型
    //parameterArgClass:class java.lang.Integer
    //val parameterArgClass vparameterizedType.getActualTypeArguments()
    //parameterArgClass:class java.lang.Integer
    private fun createPresenter(): P {
        buildSequence<Type> {
            var thisClass: Class<*> = this@BaseFragment.javaClass
            while (true) {
                yield(thisClass.genericSuperclass)
                thisClass = thisClass.superclass ?: break
            }
        }.filter {
            it is ParameterizedType
        }.flatMap {
            println("!!type $it")
            (it as ParameterizedType).actualTypeArguments.asSequence()
        }.first {
            println("!!isAssignableFromtype $it")
            //是不是IPresenter的子类
            it is Class<*> && IPresenter::class.java.isAssignableFrom(it)
        }.let {
            return (it as Class<P>).newInstance()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        presenter.onStart()
    }

    override fun onResume() {
        super.onResume()
        presenter.onResume()
    }

    override fun onPause() {
        super.onPause()
        presenter.onPause()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onDestroy() {
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.onSaveInstanceState(outState)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        presenter.onViewStateRestored(savedInstanceState)
    }
}