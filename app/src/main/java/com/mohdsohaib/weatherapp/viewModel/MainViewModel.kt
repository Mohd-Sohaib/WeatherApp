package com.mohdsohaib.weatherapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohdsohaib.weatherapp.model.WeatherResponse
import com.mohdsohaib.weatherapp.repository.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: WeatherRepository) : ViewModel() {
    init {
        getWeather("Delhi")
    }
    val weather : LiveData<WeatherResponse>
        get() = repository.weathers

    fun getWeather(cityname : String){
        viewModelScope.launch(Dispatchers.IO){
            repository.getWeatherData(cityname)
        }
    }
}