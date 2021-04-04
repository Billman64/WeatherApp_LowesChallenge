package com.github.billman64.weatherapploweschallenge.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.github.billman64.weatherapploweschallenge.R

class MainActivity : AppCompatActivity() {

    val TAG:String = this.javaClass.simpleName + "--demo"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val errorTv = findViewById<TextView>(R.id.error)
        val errorAdviceTv = findViewById<TextView>(R.id.errorAdvice)
        errorTv.visibility = View.GONE

        val cityView = findViewById<TextView>(R.id.city)

        val button: Button = findViewById(R.id.button)
        button.setOnClickListener() {
            getWeatherData()
        }


        // Error handling

        val bundle:Bundle? = intent.extras
        bundle?.let {
            val error = it.getString("error")
            var errorMsg = getString(R.string.netError)
            var errorMsgAdvice = ""

            Log.d(TAG, " response code received: $error")

            when(error){
                "404" -> {      // bad city
                    errorMsg += " " + getString(R.string.badInput)
                    errorMsgAdvice = getString(R.string.badInputAdvice)
                }
                "401" -> {      // bad API key
                    errorMsg += " " + getString(R.string.badApiKey)
                    errorMsgAdvice = getString(R.string.badApiKeyAdvice)
                }
            }

            errorTv.text = errorMsg
            errorAdviceTv.text = errorMsgAdvice
            errorTv.visibility = View.VISIBLE
            errorAdviceTv.visibility = View.VISIBLE
        }
    }

    fun getWeatherData(){

        Log.d(TAG, "getWeatherData()")
        val cityView = findViewById<TextView>(R.id.city)

        // If input is not blank, process weather data
        if(cityView.text.isNullOrBlank()) return
        else{

            // intent for ForecaseList activity
            val i = Intent(this, ForecastList::class.java)
            i.putExtra("city", cityView.text.toString())
            startActivity(i)
        }
    }
}