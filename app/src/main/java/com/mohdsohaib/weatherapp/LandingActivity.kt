package com.mohdsohaib.weatherapp

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.mohdsohaib.weatherapp.databinding.ActivityLandingBinding
import com.mohdsohaib.weatherapp.utils.NetworkUtils

class LandingActivity : AppCompatActivity() {

    private var _binding : ActivityLandingBinding ?= null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityLandingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startBtn.setOnClickListener {
            getDirection()
        }
    }

    private fun getDirection(){
        if(NetworkUtils.isInternetAvailable(this)){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            val builder = AlertDialog.Builder(this)
                .create()
            val view = layoutInflater.inflate(R.layout.custom_dialog,null)
            val  button = view.findViewById<Button>(R.id.dialogDismiss_button)
            builder.setView(view)
            button.setOnClickListener {
                 getDirection()
            }
            builder.setCanceledOnTouchOutside(false)
            builder.show()
        }
    }
}