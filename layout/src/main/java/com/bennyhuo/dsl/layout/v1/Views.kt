package com.bennyhuo.dsl.layout.v1

import android.app.Activity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout



//region ViewGroup
inline fun <reified T : ViewGroup> T.relativeLayout(init: _RelativeLayout.() -> Unit) {
    _RelativeLayout(context).also(this::addView).also(init)
}

inline fun <reified T : ViewGroup> T.linearLayout(init: _LinearLayout.() -> Unit) {
    _LinearLayout(context).also(this::addView).also(init)
}

inline fun <reified T : ViewGroup> T.frameLayout(init: _FrameLayout.() -> Unit) {
    _FrameLayout(context).also(this::addView).also(init)
}


 fun  LinearLayout.layoutWeight2(): Unit{

}
//这个表示lambda表达式的必须是_LinearLayout的的实例来调用，输入参数为空
//also把调用对象传入作为lambda表达式的输入参数
//apply的lambda表达式里面可以访问到调用者的私有属性和私有函数
inline fun <reified T : ViewGroup> T.verticalLayout(init: _LinearLayout.() -> Unit) {
    _LinearLayout(context)
            .also(this::addView)
            .apply {
                orientation = LinearLayout.VERTICAL
                init()
            }
}

inline fun <reified T : ViewGroup> T.button(init: Button.() -> Unit) {
    Button(context).also(this::addView).also(init)
}

inline fun <reified T : ViewGroup> T.textView(init: Button.() -> Unit) {
    Button(context).also(this::addView).also(init)
}

inline fun <reified T : ViewGroup> T.imageView(init: Button.() -> Unit) {
    Button(context).also(this::addView).also(init)
}
//endregion

//region Activity
inline fun <reified T : Activity> T.relativeLayout(init: _RelativeLayout.() -> Unit) {
    _RelativeLayout(this).also(this::setContentView).also(init)
}

inline fun <reified T : Activity> T.linearLayout(init: _LinearLayout.() -> Unit) {
    _LinearLayout(this).also(this::setContentView).also(init)
}

inline fun <reified T : Activity> T.frameLayout(init: _FrameLayout.() -> Unit) {
    _FrameLayout(this).also(this::setContentView).also(init)
}

//apply相当于调用者内部调用这个lambda表达式，所以可以直接访问属性
inline fun <reified T : Activity> T.verticalLayout(init: _LinearLayout.() -> Unit) {
    _LinearLayout(this)
            .also(this::setContentView)
            .apply {
                orientation = LinearLayout.VERTICAL
                //init(this) 这里加个this也可以
                init()
            }
}

//lambda表达式调用，lambda表达式名（输入参数）
inline fun  verticalLayout2(init:(Int) -> Unit) {
    return init(2)
}

inline fun <reified T : Activity> T.button(init: Button.() -> Unit) {
    Button(this).also(this::setContentView).also(init)
}

inline fun <reified T : Activity> T.textView(init: Button.() -> Unit) {
    Button(this).also(this::setContentView).also(init)
}

inline fun <reified T : Activity> T.imageView(init: Button.() -> Unit) {
    Button(this).also(this::setContentView).also(init)
}
//endregion