package com.github.billman64.weatherapploweschallenge.utils

import android.util.Log
import kotlin.math.roundToInt

class NumFormatter {
    private val TAG = this.javaClass.simpleName + "--demo"
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