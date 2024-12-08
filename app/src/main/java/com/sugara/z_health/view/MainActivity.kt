package com.sugara.z_health.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.ViewModelProvider
import com.sugara.z_health.R
import com.sugara.z_health.databinding.ActivityLoginBinding
import com.sugara.z_health.databinding.ActivityMainBinding
import com.sugara.z_health.viewmodel.LoginViewModel
import com.sugara.z_health.viewmodel.MainViewModel
import com.sugara.z_health.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {


    private lateinit var binding : ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel = obtainViewModel(this)

        mainViewModel.getSession().observe(this) { session ->

                //switch to login activity after 2 seconds
                Handler(Looper.getMainLooper()).postDelayed({
                    if (!session.isLogin!!){
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        val intent = Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                }, 2000)
        }

    }

    private fun obtainViewModel(activity: AppCompatActivity): MainViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(MainViewModel::class.java)
    }


}