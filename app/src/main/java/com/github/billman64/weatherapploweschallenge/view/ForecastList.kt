package com.github.billman64.weatherapploweschallenge.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import com.github.billman64.weatherapploweschallenge.R
import com.github.billman64.weatherapploweschallenge.model.WeatherAPI
import com.github.billman64.weatherapploweschallenge.model.WeatherAdapter
import com.github.billman64.weatherapploweschallenge.model.WeatherLVItem
import com.github.billman64.weatherapploweschallenge.model.WeatherObject
import com.github.billman64.weatherapploweschallenge.utils.NumFormatter
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
    var city:String = ""
    var weatherCollection = ArrayList<WeatherObject>()
    var responseCode = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forecast_list)

        savedInstanceState?.let{Log.d(TAG, " onCreate() with a savedInstanceState") }?: getWeatherData()
    }

    fun getWeatherData(){

        // if there is a bundle found containing a city (normal case), pull weather data for it
        //TODO: handling for expired API
        //TODO: handling for bad input

        val bundle:Bundle? = intent.extras
        bundle?.let {
            bundle.apply {
                city = getString("city")?:""
                supportActionBar?.setTitle(city)

                // progressBar
                val progressBar = findViewById<ProgressBar>(R.id.progressBar)
                progressBar.visibility = View.VISIBLE

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
                        responseCode = weatherDataAPI.getWeather(city).awaitResponse().code().toString()
                        val response = weatherDataAPI.getWeather(city).awaitResponse()
                        Log.d(TAG, " reponse code: ${response.code()} body: ${response.body().toString().substring(0..25)} errorBody: ${response.errorBody()}")
                        Log.d(TAG, " ${response.body().toString().substring(0..100)}")

                        // Parse data

                        val list = response.body()?.getAsJsonArray("list")
                        Log.d(TAG, "list: ${list.toString().substring(0..25)}... size of list: ${list?.size()}")


                        var weatherCollectionForListView = ArrayList<WeatherLVItem>()

                        // Get individual data items
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

                            val n = NumFormatter()
                            var temp = n.roundNum(weatherObject.temperature).toString()

                            weatherCollectionForListView.add(WeatherLVItem(weatherObject.weatherMain, temp))
                            weatherCollection.add(weatherObject)

                        }
                        Log.d(TAG, "weather collection size: ${weatherCollection.size}")


                        // Update UI

                        withContext(Dispatchers.Main){
                            val lv = findViewById<ListView>(R.id.listView)
                            val weatherAdapter = WeatherAdapter(baseContext, weatherCollectionForListView)
                            lv.adapter = weatherAdapter

                            progressBar.visibility = View.GONE

                            lv.setOnItemClickListener { parent, view, position, id ->

                                val i = Intent(applicationContext, Detail::class.java)
                                i.putExtra("city", city)
                                i.putExtra("temperature", weatherCollection[position].temperature)
                                i.putExtra("feelsLike", weatherCollection[position].feelsLike)
                                i.putExtra("weather", weatherCollection[position].weatherMain)
                                i.putExtra("description", weatherCollection[position].weatherDescription)
                                startActivity(i)

                                //TODO: implement fragment for detail view
                            }
                        }
                    } catch(e: Exception){
                        Log.e(TAG," Error. message: ${e.message} stacktrace: ${e.stackTrace}")
                        Log.e(TAG, " response code: $responseCode")

                        withContext(Dispatchers.Main){
                            progressBar.visibility = View.GONE
                        }

                        //TODO: navigate back to main, with an intent to display an error
                        val i = Intent(applicationContext, MainActivity::class.java)
                        i.putExtra("error", responseCode)
                        startActivity(i)
                        finish()
                    }
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("city", city)
        outState.putParcelableArrayList("weatherCollection", weatherCollection)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        city = savedInstanceState.getString("city")?:""
        val restoreList:ArrayList<WeatherObject>? = savedInstanceState.getParcelableArrayList("weatherCollection")

        city?.let{ supportActionBar?.setTitle(city)}

        // If there is weather data from previous orientation, restore it all into the listView
        // No internet connection needed here.
        restoreList?.let{
            if(restoreList.count()>0){
                weatherCollection = restoreList
                val lv: ListView = findViewById(R.id.listView)
                val weatherCollectionForListView = ArrayList<WeatherLVItem>()
                val n = NumFormatter()

                // Populate listView
                for(i in 0..weatherCollection.size-1){
                    var weather = weatherCollection[i].weatherMain
                    var temp = weatherCollection[i].temperature
                    temp = n.roundNum(temp).toString()

                    weatherCollectionForListView.add(WeatherLVItem(weather, temp))
                    }

                // Set a click listener for each item
                lv.setOnItemClickListener { parent, view, position, id ->

                    val i = Intent(applicationContext, Detail::class.java)
                    i.putExtra("city", city)
                    i.putExtra("temperature", weatherCollection[position].temperature)
                    i.putExtra("feelsLike", weatherCollection[position].feelsLike)
                    i.putExtra("weather", weatherCollection[position].weatherMain)
                    i.putExtra("description", weatherCollection[position].weatherDescription)
                    startActivity(i)
                }
                lv.adapter = WeatherAdapter(baseContext, weatherCollectionForListView)
            }
        }
    }
}