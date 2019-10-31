package com.bennyhuo.github.utils

import com.bennyhuo.github.AppContext
import com.bennyhuo.github.common.sharedpreferences.Preference
import kotlin.reflect.jvm.jvmName

/**
 * Created by benny on 6/23/17.
 */
//对于泛型函数来说，它们的类型参数可以被实化。我们将方面的函数修改如下，声明为inline并且用reified标记类型参数，就能用该函数检查value是不是T
inline fun <reified R, T> R.pref(default: T) = Preference(AppContext, "", default, R::class.jvmName)