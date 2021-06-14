package com.bennyhuo.github.common.unused

import java.io.File
import java.io.FileInputStream
import java.net.URL
import java.util.Properties
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.isSuperclassOf

/**
 * Created by benny on 8/12/17.
 */
class PropertiesDelegate(private val path: String) {

    //lateinit就不用初始化了
    private lateinit var url: URL

    private val properties: Properties by lazy {
        val prop = Properties()
        url = try {
            javaClass.getResourceAsStream(path).use {
                prop.load(it)
            }
            javaClass.getResource(path)
        } catch (e: Exception) {
            try {
                ClassLoader.getSystemClassLoader().getResourceAsStream(path).use {
                    prop.load(it)
                }
                ClassLoader.getSystemClassLoader().getResource(path)
            } catch (e: Exception) {
                FileInputStream(path).use {
                    prop.load(it)
                }
                URL("file://${File(path).canonicalPath}")
            }
        }

        prop
    }

    //    https://blog.csdn.net/u013448469/article/details/79403284
    //T表示属性的类型，thisRef是属性所在类的类型
    operator fun <T> getValue(thisRef: Any, property: KProperty<*>): T {
        //读出来其实都是字符串，这里需要转化
        val value = properties[property.name]
        println("\"TAG\", \"PropertiesDelegate getValue:\" + $property")
//        Log.e("TAG", "PropertiesDelegate getValue:" + property);
        val classOfT = property.returnType.classifier as KClass<*>

//    thisRef::class
//        val kotlin = thisRef.javaClass.kotlin
        return when {
            Boolean::class == classOfT -> value.toString().toBoolean()  //value是Any?类型
            Number::class.isSuperclassOf(classOfT) -> {
                //用的是java反射 Long.parseLong
                classOfT.javaObjectType.getDeclaredMethod("parse${classOfT.simpleName}",
                        String::class.java).invoke(null, value)
            }
            String::class == classOfT -> value
            else -> throw IllegalArgumentException("Unsupported type.")
        } as T
    }

    operator fun <T> setValue(thisRef: Any, property: KProperty<*>, value: T) {
        properties[property.name] = value.toString()
        File(url.toURI()).outputStream().use {
            properties.store(it, "")
        }
    }
}

abstract class AbsProperties(path: String) {
    //一次性读写出来
    protected val prop = PropertiesDelegate(path)
}