package com.serhohuk.weatherapp.data.models.forecastdata

data class Main(
    var feels_like: Double?,
    var grnd_level: Int?,
    var humidity: Int?,
    var pressure: Int?,
    var sea_level: Int?,
    var temp: Double?,
    var temp_kf: Double?,
    var temp_max: Double?,
    var temp_min: Double?
)