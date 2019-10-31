package com.bennyhuo.github.common.ext

 sealed class BooleanExt<out T> {

    object Otherwise : BooleanExt<Nothing>()
     //这里有个getData方法，返回T,所以是协变的，BooleanExt<out T>需要加out
    class WithData<T>( var data: T) : BooleanExt<T>(){
    }
}
inline fun <T> Boolean.yes2(block: () -> T):BooleanExt<T> {
        when {
            //this 其实是调用者
            this -> {
               return BooleanExt.WithData(block())
            }
            else -> {
                //如果BooleanExt<out T>变成BooleanExt<T>时会报错，因为BooleanExt.Otherwise不一定是BooleanExt的子类型
                return   BooleanExt.Otherwise
            }
        }
}
inline fun <T> Boolean.yes(block: () -> T) =
        when {
            //this 其实是调用者
            this -> {
                BooleanExt.WithData(block())
            }
            else -> {
                BooleanExt.Otherwise
            }
        }


inline fun <T> Boolean.no(block: () -> T) = when {
    this -> BooleanExt.Otherwise
    else -> {
        BooleanExt.WithData(block())
    }
}

//如果是BooleanExt.Otherwise，表明要执行后面这个block
//否则执行第一个block

inline fun <T> BooleanExt<T>.otherwise(block: () -> T): T =
        when (this) {
            is BooleanExt.Otherwise -> block()
            //这里不用强转，因为首先是BooleanExt<T>（拓展函数），然后是BooleanExt.WithData
            is BooleanExt.WithData -> this.data
        }

fun main(args: Array<String>) {
    false.yes{
        //yes 前面是true 走这里
        println(" false.yes"+1)
        1
    }.otherwise{
        //yes 前面是false 走这里
        println(" false.yes"+2)
        2
    }
}