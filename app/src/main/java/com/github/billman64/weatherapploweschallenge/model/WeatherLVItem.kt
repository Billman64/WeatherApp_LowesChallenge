package com.github.billman64.weatherapploweschallenge.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherLVItem( var weatherMain:String, var temperature:String){
}
