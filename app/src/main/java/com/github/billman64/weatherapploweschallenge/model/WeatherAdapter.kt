package com.github.billman64.weatherapploweschallenge.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.github.billman64.weatherapploweschallenge.R

class WeatherAdapter(private val context: Context, private val weatherList:ArrayList<WeatherLVItem>): BaseAdapter() {
    private lateinit var weather: TextView
    private lateinit var temp:TextView

    override fun getCount(): Int {
        return weatherList.size
    }

    override fun getItem(position: Int): Any {
        return weatherList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var cv = convertView
        cv = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false)
        weather = cv.findViewById(R.id.weather)
        weather.text = weatherList[position].weatherMain
        temp = cv.findViewById(R.id.temp)
        temp.text = "temp: " + weatherList[position].temperature
        return cv
    }

    //TODO: implement onClickListener for detail view
}