package com.github.billman64.weatherapploweschallenge.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherObject(var temperature:String, var feelsLike:String, var weatherMain:String, var weatherDescription:String ){

//    @SerializedName
//    @Expose
//    var temperature:String
//        internal set

}
