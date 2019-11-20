package android.support.v7.widget

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.PorterDuff.Mode
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.support.v4.view.TintableBackgroundView
import android.util.AttributeSet
import android.widget.ToggleButton

class TintableToggleButton
@JvmOverloads
constructor(context: Context, attrs: AttributeSet?= null,  defStyleAttr: Int = 0)
    : ToggleButton(TintContextWrapper.wrap(context), attrs, defStyleAttr), TintableBackgroundView {

    //    private var backgroundTintHelper: AppCompatBackgroundHelper? = null
    private val backgroundTintHelper = AppCompatBackgroundHelper(this)

    //这里不能用lazy，应为成员变量初始化调用后，backgroundTintHelper还是为空的，然后走构造函数，构造函数用到了这个成员变量，因为这个成员变量是空的，导致空指针
//    private val backgroundTintHelper: AppCompatBackgroundHelper by lazy{
//    AppCompatBackgroundHelper(this).apply{
//        loadFromAttributes(attrs, defStyleAttr)
//    }
//}

    init {
//        backgroundTintHelper = AppCompatBackgroundHelper(this)
        backgroundTintHelper?.loadFromAttributes(attrs, defStyleAttr)
    }

    override fun setSupportBackgroundTintList(tint: ColorStateList?) {
        backgroundTintHelper?.supportBackgroundTintList = tint
    }

    override fun getSupportBackgroundTintMode() = backgroundTintHelper?.supportBackgroundTintMode

    override fun setSupportBackgroundTintMode(tintMode: Mode?) {
        backgroundTintHelper?.supportBackgroundTintMode = tintMode
    }

    override fun getSupportBackgroundTintList() = backgroundTintHelper?.supportBackgroundTintList

    override fun setBackgroundResource(@DrawableRes resId: Int) {
        super.setBackgroundResource(resId)
        backgroundTintHelper?.onSetBackgroundResource(resId)
    }

    override fun setBackgroundDrawable(background: Drawable?) {
        super.setBackgroundDrawable(background)
        backgroundTintHelper?.onSetBackgroundDrawable(background)
    }
}