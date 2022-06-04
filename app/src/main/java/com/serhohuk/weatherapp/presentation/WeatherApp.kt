package com.serhohuk.weatherapp.presentation

import android.app.Application
import dagger.hilt.DefineComponent
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApp : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}