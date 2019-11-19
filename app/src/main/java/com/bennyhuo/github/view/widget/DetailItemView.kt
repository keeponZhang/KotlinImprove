package com.bennyhuo.github.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.bennyhuo.github.R
import com.bennyhuo.github.utils.subscribeIgnoreError
import com.bennyhuo.kotlin.opd.delegateOf
import kotlinx.android.synthetic.main.detail_item.view.*
import org.jetbrains.anko.sdk15.listeners.onClick
import rx.Observable
import kotlin.reflect.KProperty

typealias CheckEvent = (Boolean) -> Observable<Boolean>

//其实也可以不用R，getter函数一般没有方法参数
class ObjectPropertyDelegate<T, R>(val receiver: R, val getter: ((R) -> T)? = null, val setter: ((R, T) -> Unit)? = null, defaultValue: T? = null) {
    private var value: T? = defaultValue

    //ref 代理的属性 ，如 title
    operator fun getValue(ref: Any, property: KProperty<*>): T {
        //receiver是方法调用者
        return getter?.invoke(receiver) ?: value!!
    }

    operator fun setValue(ref: Any, property: KProperty<*>, value: T) {
        setter?.invoke(receiver, value)
        this.value = value
    }

}

class DetailItemView
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : RelativeLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.detail_item, this)
    }
//    var title:String
//        set(value){
//            titleView.text = value
//        }
//        get() = textView().text.toString()

    var title by ObjectPropertyDelegate(titleView,TextView::getText,TextView::setText)
//    var title by delegateOf(titleView::getText, titleView::setText)

    var content by delegateOf(contentView::getText, contentView::setText, "")

    var icon by delegateOf(iconView::setImageResource, 0)

    var operatorIcon by delegateOf(operatorIconView::setBackgroundResource, 0)

    var isChecked by delegateOf(operatorIconView::isChecked, operatorIconView::setChecked)

    var checkEvent: CheckEvent? = null

    init {
        attrs?.let {
            val a = context.obtainStyledAttributes(it, R.styleable.DetailItemView)
            title = a.getString(R.styleable.DetailItemView_item_title) ?: ""
            content = a.getString(R.styleable.DetailItemView_item_content) ?: ""
            icon = a.getResourceId(R.styleable.DetailItemView_item_icon, 0)
            operatorIcon = a.getResourceId(R.styleable.DetailItemView_item_op_icon, 0)
            a.recycle()
        }

        onClick {
            //把isChecked作为checkEventlambda表达式的输入参数
            checkEvent?.invoke(isChecked)
                    ?.subscribeIgnoreError {
                        isChecked = it
                    }
        }
    }
}