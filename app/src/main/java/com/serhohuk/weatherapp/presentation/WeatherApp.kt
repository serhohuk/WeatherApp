package com.serhohuk.weatherapp.presentation

import android.app.Application
import android.preference.PreferenceManager
import com.serhohuk.weatherapp.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import org.osmdroid.config.Configuration
import java.io.File

@HiltAndroidApp
class WeatherApp : Application() {

    override fun onCreate() {
        super.onCreate()
        setOsmDroidMapsConfig()
    }

    private fun setOsmDroidMapsConfig(){
        val osmConf = Configuration.getInstance()
        val basePath = File(cacheDir.absolutePath, "osmdroid")
        osmConf.osmdroidBasePath = basePath
        val tileCache = File(osmConf.osmdroidBasePath.absolutePath, "tile")
        osmConf.osmdroidTileCache = tileCache
        osmConf.load(this, PreferenceManager.getDefaultSharedPreferences(this))
        osmConf.userAgentValue = BuildConfig.APPLICATION_ID
    }
}