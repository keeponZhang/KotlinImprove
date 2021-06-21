package com.bennyhuo.github.common.sharedpreferences

import android.content.Context
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

//T表示类型，Stirng,Long等
class Preference<T>(
        val context: Context, val name: String, val default: T, val prefName: String = "default"
)
//ReadWriteProperty 就一个getValue和一个setValue方法
    : ReadWriteProperty<Any?, T> {

    //lazy代理只覆写了getValue方法，所以这里不能用var
    private val prefs by lazy {
        context.getSharedPreferences(prefName, Context.MODE_PRIVATE)
    }

    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return findPreference(findProperName(property))
    }
    //这里可知，name可以为空
    private fun findProperName(property: KProperty<*>) = if (name.isEmpty()) property.name else name

    private fun findPreference(key: String): T {
        return when (default) {
            is Long -> prefs.getLong(key, default)
            is Int -> prefs.getInt(key, default)
            is Boolean -> prefs.getBoolean(key, default)
            is String -> prefs.getString(key, default)
            else -> throw IllegalArgumentException("Unsupported type.")
        } as T
    }

    //T表示属性的类型，thisRef是属性所在类的类型
    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        putPreference(findProperName(property), value)
    }

    private fun putPreference(key: String, value: T) {
        //with是两个参数的 ,第一个参数不要传空,第二个参数是block，block输入参数是第一个参数
        with(prefs.edit()) {
            when (value) {
                //所以这里可以直接调用putLong
                is Long -> putLong(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is String -> putString(key, value)
                else -> throw IllegalArgumentException("Unsupported type.")
            }
            //.apply() apply放在这里也可以
        }.apply()

        //也可以用apply来实现
//        prefs.edit().apply(){
//            when(value){
//                is Long -> putLong(key, value)
//                is Int -> putInt(key, value)
//                is Boolean -> putBoolean(key, value)
//                is String -> putString(key, value)
//                else -> throw IllegalArgumentException("Unsupported type.")
//            }.apply()
//        }
    }
}