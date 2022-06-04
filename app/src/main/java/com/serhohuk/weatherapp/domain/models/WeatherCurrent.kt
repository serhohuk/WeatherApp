package com.serhohuk.weatherapp.domain.models

import com.serhohuk.weatherapp.data.models.forecastdata.Clouds
import com.serhohuk.weatherapp.data.models.forecastdata.Coord
import com.serhohuk.weatherapp.data.models.forecastdata.Weather
import com.serhohuk.weatherapp.data.models.weatherdata.Main
import com.serhohuk.weatherapp.data.models.weatherdata.Sys
import com.serhohuk.weatherapp.data.models.weatherdata.Wind

data class WeatherCurrent(
    var base: String?,
    var clouds: Clouds?,
    var coord: Coord?,
    var main: Main?,
    var name: String?,
    var sys: Sys?,
    var visibility: Int?,
    var weather: List<Weather>?,
    var wind: Wind?
)
