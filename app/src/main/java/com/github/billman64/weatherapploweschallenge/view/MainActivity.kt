package com.github.billman64.weatherapploweschallenge.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.github.billman64.weatherapploweschallenge.R

class MainActivity : AppCompatActivity() {

    val TAG:String = this.javaClass.simpleName + "--demo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cityView = findViewById<TextView>(R.id.city)
        cityView.text = "43016" // temp default for development only

        val button: Button = findViewById(R.id.button)
        button.setOnClickListener() {
            getWeatherData()
        }

    }

    fun getWeatherData(){

        Log.d(TAG, "getWeatherData()")

        val cityView = findViewById<TextView>(R.id.city)
        if(cityView.text.isNullOrBlank()) return
        else{
            // intent for ForecaseList activity
            val i = Intent(this, ForecastList::class.java)
            i.putExtra("city", cityView.text.toString())
            startActivity(i)
        }





    }
}