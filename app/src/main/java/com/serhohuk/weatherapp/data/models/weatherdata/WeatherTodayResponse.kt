package com.serhohuk.weatherapp.data.models.weatherdata

import com.serhohuk.weatherapp.data.models.forecastdata.Clouds
import com.serhohuk.weatherapp.data.models.forecastdata.Coord
import com.serhohuk.weatherapp.data.models.forecastdata.Weather

data class WeatherTodayResponse(
    var base: String?,
    var clouds: Clouds?,
    var cod: Int?,
    var coord: Coord?,
    var dt: Int?,
    var id: Int?,
    var main: Main?,
    var name: String?,
    var sys: Sys?,
    var timezone: Int?,
    var visibility: Int?,
    var weather: List<Weather>?,
    var wind: Wind?
)