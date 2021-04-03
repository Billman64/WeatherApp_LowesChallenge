package com.github.billman64.weatherapploweschallenge.view

import android.app.Dialog
import android.content.Intent
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
                supportActionBar?.setTitle(city)

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
                        val response = weatherDataAPI.getWeather(city).awaitResponse()
                        Log.d(TAG, " reponse code: ${response.code()} body: ${response.body().toString().substring(0..25)} errorBody: ${response.errorBody()}")
                        Log.d(TAG, " ${response.body().toString().substring(0..100)}")

                        // Parse data

                        val list = response.body()?.getAsJsonArray("list")
                        Log.d(TAG, "list: ${list.toString().substring(0..25)}... size of list: ${list?.size()}")

                        var weatherCollection = ArrayList<WeatherObject>()
                        var weatherCollectionForListView = ArrayList<WeatherLVItem>()

                        for(i in 0..list!!.size()-1){
                            var weatherItem = list?.get(i)?.asJsonObject
                            Log.v(TAG, "weather item: ${weatherItem.toString().substring(0..50)}")

                            var weatherItemMain = weatherItem?.getAsJsonObject("main")?.asJsonObject
                            Log.v(TAG, "weather main: ${weatherItemMain.toString().substring(0..50)}")

                            var weatherItemWeather = weatherItem?.getAsJsonArray("weather")?.get(0)?.asJsonObject
                            Log.v(TAG, "weather: ${weatherItemWeather.toString().substring(0..50)}")

                            // trim quotes
                            var main = weatherItemWeather?.get("main").toString()
                            main = main.substring(1..main.length-2)
                            var description = weatherItemWeather?.get("description").toString()
                            description = description.substring(1..description.length-2)

                            var weatherObject = WeatherObject(weatherItemMain?.get("temp").toString(),
                                weatherItemMain?.get("feels_like").toString(),
                                main,
                                description
                            )
                            Log.v(TAG, "weatherObject: ${weatherObject}")

                            weatherCollectionForListView.add(WeatherLVItem(weatherObject.weatherMain, weatherObject.temperature))
                            weatherCollection.add(weatherObject)

                        }
                        Log.d(TAG, "weather collection size: ${weatherCollection.size}")

                        // Update UI

                        withContext(Dispatchers.Main){
                            val lv = findViewById<ListView>(R.id.listView)
                            val weatherAdapter = WeatherAdapter(baseContext, weatherCollectionForListView)
                            lv.adapter = weatherAdapter

                            lv.setOnItemClickListener { parent, view, position, id ->
                                //TODO: implement fragment for detail view

                                val dialog: Dialog = Dialog(this@ForecastList)
                                dialog.setContentView(R.layout.detail)
                                var dialogTemp = dialog.findViewById<TextView>(R.id.temp)
                                dialogTemp.text = weatherCollection[position].temperature + "\u2109"

                                var dialogFeelsLike = dialog.findViewById<TextView>(R.id.feels_like)
                                dialogFeelsLike.text = "Feels like: " + weatherCollection[position].feelsLike + "\u2109"

                                var dialogWeather = dialog.findViewById<TextView>(R.id.weather)
                                dialogWeather.text = weatherCollection[position].weatherMain
                                Log.d(TAG, " dialog weather: ${weatherCollection[position].weatherMain}")

                                var dialogDescription = dialog.findViewById<TextView>(R.id.description)
                                dialogDescription.text = weatherCollection[position].weatherDescription

//                                dialog.show()

                                val i = Intent(applicationContext, Detail::class.java)
                                i.putExtra("temperature", weatherCollection[position].temperature)
                                i.putExtra("feelsLike", weatherCollection[position].feelsLike)
                                i.putExtra("weather", weatherCollection[position].weatherMain)
                                i.putExtra("description", weatherCollection[position].weatherDescription)
                                startActivity(i)





                            }
                        }

                    } catch(e: Exception){
                        Log.e(TAG," Error. message: ${e.message} stacktrace: ${e.stackTrace}")
                    }
                }
            }
        }
    }
}