package com.serhohuk.weatherapp.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.serhohuk.weatherapp.R
import com.serhohuk.weatherapp.databinding.ActivityMainBinding
import com.serhohuk.weatherapp.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val viewModel : MainViewModel by viewModels()
    private lateinit var bottomNav : BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_WeatherApp)
        setContentView(R.layout.activity_main)
        bottomNav = findViewById(R.id.bottom_nav)

        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = fragment.navController
        bottomNav.setupWithNavController(navController)

        viewModel.getWeatherCurrent("Kyiv", "uk")
    }
}