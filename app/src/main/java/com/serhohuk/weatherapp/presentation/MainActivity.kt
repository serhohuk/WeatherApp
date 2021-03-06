package com.serhohuk.weatherapp.presentation

import android.content.SharedPreferences
import android.location.LocationRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.serhohuk.weatherapp.R
import com.serhohuk.weatherapp.presentation.utils.ConnectionChecker
import com.serhohuk.weatherapp.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    val viewModel : MainViewModel by viewModels()
    private lateinit var bottomNav : BottomNavigationView
    @Inject lateinit var shPrefs : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_WeatherApp)
        setContentView(R.layout.activity_main)
        bottomNav = findViewById(R.id.bottom_nav)

        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = fragment.navController
        bottomNav.setupWithNavController(navController)

        if (ConnectionChecker.getConnectionType(this)!=0){
            viewModel.getWeatherCurrent(shPrefs.getString("city", "Kyiv") ?: "Kyiv", Locale.getDefault().language)
            viewModel.getWeatherForecast(shPrefs.getString("city", "Kyiv")?: "Kyiv", Locale.getDefault().language)
        }

        viewModel.coordinatesData.observe(this, Observer {
            navController.navigate(R.id.weatherNowFragment)
            viewModel.getWeatherCurrentCoord(it.lat, it.lon, Locale.getDefault().language)
            viewModel.getWeatherForecastCoord(it.lat, it.lon, Locale.getDefault().language)
        })
    }
}