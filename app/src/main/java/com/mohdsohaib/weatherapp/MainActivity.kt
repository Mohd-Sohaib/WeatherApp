package com.mohdsohaib.weatherapp

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mohdsohaib.weatherapp.api.RetrofitHelper
import com.mohdsohaib.weatherapp.api.WeatherServices
import com.mohdsohaib.weatherapp.databinding.ActivityMainBinding
import com.mohdsohaib.weatherapp.repository.WeatherRepository
import com.mohdsohaib.weatherapp.utils.NetworkUtils
import com.mohdsohaib.weatherapp.viewModel.MainViewModel
import com.mohdsohaib.weatherapp.viewModel.MainViewModelFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
    private var _binding : ActivityMainBinding ?= null
    private val binding get() = _binding!!
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.search.setOnClickListener {
            val inputEditTextField = EditText(this)
            val dialog = AlertDialog.Builder(this)
                .setTitle("City Name")
                .setView(inputEditTextField)
                .setPositiveButton("OK") { _, _ ->
                    val dialogCityName = inputEditTextField .text.toString()
                    viewModel.getWeather(dialogCityName)
                   // Toast.makeText(this, dialogCityName, Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .create()
            dialog.show()
        }


        val weatherServices = RetrofitHelper.getInstance().create(WeatherServices::class.java)
        val repository = WeatherRepository(weatherServices, applicationContext)
        viewModel = ViewModelProvider(this, MainViewModelFactory(repository))[MainViewModel::class.java]


            viewModel.weather.observe(this, Observer {
                if(it != null) {
                    //set temperature
                    binding.temp.text = (it.main.temp - 273.15).toInt().toString()
                    //set condition
                    val condition = it.weather.firstOrNull()?.main ?: "unknown"
                    binding.weatherCondition.text = condition
                    //set location
                    binding.location.text = it.name
                    //set country
                    binding.country.text = " / ${it.sys.country}"
                    //max temperature
                    binding.maxTemp.text = "${(it.main.temp_max - 273.15).toInt()}℃"
                    //min temperature
                    binding.minTemp.text = "${(it.main.temp_min - 273.15).toInt()}℃"
                    //humidity
                    binding.humidity.text = "${it.main.humidity}%"
                    //wind speed
                    binding.windSpeed.text = "${it.wind.speed} m/s"
                    //visibility
                    binding.visibility.text = "${it.visibility} m"
                    //sunrise
                    binding.sunrise.text = getDateTime(it.sys.sunrise.toString())
                    //sunset
                    binding.sunset.text = getDateTime(it.sys.sunset.toString())
                    //sea level
                    binding.seaLevel.text = "${it.main.pressure} hPa"
                    //get current day os week
                    binding.days.text = getDay(System.currentTimeMillis())
                    //get current date
                    binding.date.text = getDate()
                    //set the animation according to weather conditions
                    setAnimatedIcon(condition)
                }
            })
    }

    private fun setAnimatedIcon(condition :String){
        when(condition){
            "Clear" -> {
                binding.iconAnimated.setAnimation(R.raw.sunny)
            }
            "Clouds" -> {
                binding.iconAnimated.setAnimation(R.raw.fewclouds)
            }
            "Rain", "Drizzle" -> {
                binding.iconAnimated.setAnimation(R.raw.rain)
            }
            "Thunderstorm" -> {
                binding.iconAnimated.setAnimation(R.raw.thunderstrom)
            }
            "Snow" -> {
                binding.iconAnimated.setAnimation(R.raw.snow)
            }
            "Mist","Smoke","Haze","Fog","Dust","Sand" -> {
                binding.iconAnimated.setAnimation(R.raw.mist)
            }
            else -> {
                binding.iconAnimated.setAnimation(R.raw.sunny)
            }
        }
        binding.iconAnimated.playAnimation()
    }

    @SuppressLint("SimpleDateFormat")
    private fun getDateTime(s: String): String? {
        return try {
            val sdf = SimpleDateFormat("h:mm a")
            val netDate = Date(s.toLong() * 1000)
            sdf.format(netDate)
        } catch (e: Exception) {
            e.toString()
        }
    }

    private fun getDay(timestamp : Long): String?{
        return try {
            val sdf = SimpleDateFormat("EEEE", Locale.getDefault())
            sdf.format((Date(timestamp)))
        }catch (e : Exception){
            e.toString()
        }
    }

    private fun getDate(): String?{
        return try {
            val sdf = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
            sdf.format(Date())
        }catch (e : Exception){
            e.toString()
        }
    }
}
