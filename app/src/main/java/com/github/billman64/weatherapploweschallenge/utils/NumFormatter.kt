package com.github.billman64.weatherapploweschallenge.utils

import kotlin.math.roundToInt

class NumFormatter {
    fun roundNum(num:String):Int{
        return num.toFloat().roundToInt()
    }

    fun roundNum(num:Float):Int{
        return roundNum(num.toString())
    }

    fun roundNum(num:Double):Int{
        return roundNum(num.toString())
    }
}