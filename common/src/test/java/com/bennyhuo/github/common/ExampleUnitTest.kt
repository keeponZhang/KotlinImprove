package com.bennyhuo.github.common

import com.bennyhuo.github.common.ext.no
import com.bennyhuo.github.common.ext.otherwise
import com.bennyhuo.github.common.ext.yes
import com.bennyhuo.github.common.ext.yes2
import org.junit.Assert
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
class ExampleUnitTest {
    @Test
    fun testBoolean() {
       val resultOtherwise = false.yes{
           //yes 前面是true 走这里
           println(" false.yes"+1)
           1
        }.otherwise{
           //yes 前面是false 走这里
           println(" false.yes"+2)
           2
        }

        val resultOtherwise2 = false.yes2{
            //yes 前面是true 走这里
            println(" false.yes"+1)
            1
        }.otherwise{
            //yes 前面是false 走这里
            println(" false.yes"+2)
            2
        }


        Assert.assertEquals(resultOtherwise, 2)
        val result = true.no{
            //no 前面是false 走这里
            println(" true.no"+1)
            1
        }.otherwise{
            //no 前面是true 走这里
            println(" true.no"+2)
            2
        }
        Assert.assertEquals(result, 2)
    }

    fun getABoolean() = false
}