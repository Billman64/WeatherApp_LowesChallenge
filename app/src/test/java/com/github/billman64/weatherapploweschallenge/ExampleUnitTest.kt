package com.github.billman64.weatherapploweschallenge

import com.github.billman64.weatherapploweschallenge.utils.NumFormatter
import org.junit.Test

import org.junit.Assert.*



/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun numFormatter_rounds_down(){
        val n = NumFormatter()
        assertEquals(n.roundNum("60.4"),60)
    }

    @Test
    fun numFormatter_rounds_up(){
        val n = NumFormatter()
        assertEquals(n.roundNum("60.5"),61)
    }

    @Test
    fun numFormatter_float_parameter(){
        val n = NumFormatter()
        assertEquals(n.roundNum(60.5f),61)
    }

    @Test
    fun numFormatter_double_parameter(){
        val n = NumFormatter()
        assertEquals(n.roundNum(60.5f),61)
    }
}