package com.github.billman64.weatherapploweschallenge.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.github.billman64.weatherapploweschallenge.R

class Detail : AppCompatActivity() {
    val TAG = this.javaClass.simpleName + "--demo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail)


        val bundle:Bundle? = intent.extras
        Log.d(TAG, " intent extras size(): ${bundle?.size()}")

        bundle?.let{
            bundle.apply {

                //TODO: city in titleBar
//                supportActionBar?.setTitle(city)

                val tempView = findViewById<TextView>(R.id.temp)
                tempView.text = it.getString("temperature") + "\u2109"

                val feelsLikeView = findViewById<TextView>(R.id.feels_like)
                feelsLikeView.text = "Feels like: " + it.getString("feelsLike") + "\u2109"

                val weatherView = findViewById<TextView>(R.id.weather)
                weatherView.text = it.getString("weather")

                val descriptionView = findViewById<TextView>(R.id.description)
                descriptionView.text = it.getString("description")
            }
        }
    }
}