package com.github.billman64.weatherapploweschallenge.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.github.billman64.weatherapploweschallenge.R
import com.github.billman64.weatherapploweschallenge.utils.NumFormatter

class Detail : AppCompatActivity() {
    private val TAG = this.javaClass.simpleName + "--demo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail)


        val bundle:Bundle? = intent.extras
        Log.d(TAG, " intent extras size(): ${bundle?.size()}")

        bundle?.let{
            bundle.apply {

                val city = bundle.get("city").toString()
                supportActionBar?.setTitle(city)

                val tempView = findViewById<TextView>(R.id.temp)
                var temp = it.getString("temperature")
                val n = NumFormatter()
                temp = n.roundNum(temp!!).toString() + "\u2109"
                tempView.text = temp

                val feelsLikeView = findViewById<TextView>(R.id.feels_like)
                var feelsLike = it.getString("feelsLike")
                feelsLike = getString(R.string.feelLike) + ": " + n.roundNum(feelsLike!!).toString() + "\u2109"
                feelsLikeView.text = feelsLike

                val weatherView = findViewById<TextView>(R.id.weather)
                weatherView.text = it.getString("weather")

                val descriptionView = findViewById<TextView>(R.id.description)
                descriptionView.text = it.getString("description")
            }
        }
    }
}