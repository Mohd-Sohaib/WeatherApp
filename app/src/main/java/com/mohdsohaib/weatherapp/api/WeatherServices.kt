package com.mohdsohaib.weatherapp.api

import com.mohdsohaib.weatherapp.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "4519b18622844f962c86bda8f871023f"
interface WeatherServices {

    @GET("data/2.5/weather?appid=$API_KEY")
    suspend fun getWeatherData(@Query("q") cityname : String) : Response<WeatherResponse>

}