package com.github.billman64.weatherapploweschallenge.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.TextView
import com.github.billman64.weatherapploweschallenge.R
import com.github.billman64.weatherapploweschallenge.model.WeatherAPI
import com.github.billman64.weatherapploweschallenge.model.WeatherAdapter
import com.github.billman64.weatherapploweschallenge.model.WeatherLVItem
import com.github.billman64.weatherapploweschallenge.model.WeatherObject
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
        setContentView(R.layout.forecast_list)

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
                        val response = weatherDataAPI.getWeather(city,"65d00499677e59496ca2f318eb68c049").awaitResponse()
                        Log.d(TAG, " reponse code: ${response.code()} body: ${response.body()} errorBody: ${response.errorBody()}")
                        Log.d(TAG, " ${response.body().toString().substring(0..100)}")

                        // Parse data

                        val list = response.body()?.getAsJsonArray("list")
                        Log.d(TAG, "list: ${list.toString().substring(0..25)}... size of list: ${list?.size()}")

                        var weatherCollection = ArrayList<WeatherObject>()
                        var weatherCollectionForListView = ArrayList<WeatherLVItem>()

                        for(i in 1..list!!.size()){
                            var weatherItem = list?.get(0)?.asJsonObject
                            Log.d(TAG, "weather item: ${weatherItem.toString().substring(0..50)}")

                            var weatherItemMain = weatherItem?.getAsJsonObject("main")?.asJsonObject
                            Log.d(TAG, "weather main: ${weatherItemMain.toString().substring(0..50)}")

                            var weatherItemWeather = weatherItem?.getAsJsonArray("weather")?.get(0)?.asJsonObject
                            Log.d(TAG, "weather: ${weatherItemWeather.toString().substring(0..50)}")

                            var weatherObject = WeatherObject(weatherItemMain?.get("temp").toString(),
                                weatherItemMain?.get("feels_like").toString(),
                                weatherItemWeather?.get("main").toString(),
                                weatherItemWeather?.get("description").toString()
                            )
                            Log.d(TAG, "weatherObject: ${weatherObject}")

                            weatherCollectionForListView.add(WeatherLVItem(weatherObject.weatherMain, weatherObject.temperature))
                            weatherCollection.add(weatherObject)

                        }
                        Log.d(TAG, "weather collection size: ${weatherCollection.size}")

                        // Update UI

                        withContext(Dispatchers.Main){
                            val lv = findViewById<ListView>(R.id.listView)
                            val weatherAdapter = WeatherAdapter(baseContext, weatherCollectionForListView)
                            lv.adapter = weatherAdapter
                        }


                    } catch(e: Exception){
                        Log.e(TAG," network error: ${e.message}")
                    }

                }

            }
        }

    }
}