package com.bennyhuo.github.common

import com.bennyhuo.github.common.unused.AbsProperties
import org.junit.Test

class InfoProps: AbsProperties("info.properties"){
    var name: String by prop
    var email: String by prop
    var age: Int by prop
    var student: Boolean by prop
    var point: Float by prop
}

class TestProperties{

    @Test
    fun testProperties(){
        InfoProps().let {
            println("before name="+it.name)
            println("before email="+it.email)
            println("before age="+it.age)
            println("before student="+it.student)
            println("before point="+it.point)
            it.name = "kotlin"
            it.email = "admin@kotliner.cn"
            it.age = 8
            it.point = 3.0f
            println("after name="+it.name)
            println("after email="+it.email)
            println("after age="+it.age)
            println("after student="+it.student)
            println("after point="+it.point)
            //修改后的路径E:\keeponProject\mycode\KotlinImprove\common\build\intermediates\sourceFolderJavaResources\test\debug\info.properties
        }
    }

}
