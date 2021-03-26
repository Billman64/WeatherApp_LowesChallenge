package com.github.billman64.weatherapploweschallenge.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.github.billman64.weatherapploweschallenge.R
import com.github.billman64.weatherapploweschallenge.model.WeatherAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.awaitResponse
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class ForecastList : AppCompatActivity() {

    val TAG:String = this.javaClass.simpleName + "--demo"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forecast_list)

        Log.d(TAG, "onCreate()")

        val bundle:Bundle? = intent.extras

        bundle?.let {
            bundle.apply {
                val city:String = getString("city")?:""

                // Retrofit builder
                val weatherDataAPI = Retrofit.Builder()
                        .baseUrl(" https://api.openweathermap.org/data/2.5/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(WeatherAPI::class.java)


                // https://api.openweathermap.org/data/2.5/forecast?q={city}&appid={api key}



                // Coroutine for network call
                GlobalScope.launch(Dispatchers.IO){

                    // Get data
                    try{
                        Log.d(TAG, " city: $city")
                        val response = weatherDataAPI.getWeather(city,"insertAPIKeyHere").awaitResponse()
                        Log.d(TAG, " reponse code: ${response.code()} body: ${response.body()} errorBody: ${response.errorBody()}")
                        Log.d(TAG, " ${response.message()}")

                        // Parse data







                        // Update UI

                        withContext(Dispatchers.Main){

                        }


                    } catch(e: Exception){
                        Log.d(TAG," network error: ${e.message}")
                    }

                }









            }
        }





    }
}