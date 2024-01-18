package com.mohdsohaib.weatherapp.repository

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mohdsohaib.weatherapp.LandingActivity
import com.mohdsohaib.weatherapp.api.WeatherServices
import com.mohdsohaib.weatherapp.model.WeatherResponse
import com.mohdsohaib.weatherapp.utils.NetworkUtils

class WeatherRepository(private val weatherServices: WeatherServices, private val applicationContext: Context) {

    private val weatherLiveData = MutableLiveData<WeatherResponse>()

    val weathers : LiveData<WeatherResponse>
        get() = weatherLiveData

    suspend fun getWeatherData(cityname : String){
            val result = weatherServices.getWeatherData(cityname)
            if(result.body() != null){
                weatherLiveData.postValue(result.body())
            }
    }
}