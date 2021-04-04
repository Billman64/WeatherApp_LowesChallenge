package com.github.billman64.weatherapploweschallenge.model

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("forecast?units=imperial&appid=InsertApiKey")
    fun getWeather(@Query("q") city:String):retrofit2.Call<JsonObject>
}