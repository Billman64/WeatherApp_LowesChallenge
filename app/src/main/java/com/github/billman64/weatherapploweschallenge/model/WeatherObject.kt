package com.github.billman64.weatherapploweschallenge.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherObject(var temperature:String,
                         var feelsLike:String,
                         var weatherMain:String,
                         var weatherDescription:String ):Parcelable{

    @SerializedName("temperature")
    @Expose
    var mTemperature:String
        internal set

    @SerializedName("feelsLike")
    @Expose
    var mFeelsLike:String
        internal set

    @SerializedName("weather")
    @Expose
    var mWeatherMain:String
        internal set

    @SerializedName("description")
    @Expose
    var mDescription:String
        internal set

    constructor(parcel: Parcel) : this(
        parcel.readString()?:"no string found",
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readString().toString()
    )
    init {
        mTemperature = temperature
        mFeelsLike = feelsLike
        mWeatherMain = weatherMain
        mDescription = weatherDescription
    }

    override fun describeContents(): Int {
        TODO("Not yet implemented")
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(temperature)
        dest?.writeString(feelsLike)
        dest?.writeString(weatherMain)
        dest?.writeString(weatherDescription)
    }

    companion object CREATOR : Parcelable.Creator<WeatherObject> {
        override fun createFromParcel(parcel: Parcel): WeatherObject {
            return WeatherObject(parcel)
        }

        override fun newArray(size: Int): Array<WeatherObject?> {
            return arrayOfNulls(size)
        }
    }
}
