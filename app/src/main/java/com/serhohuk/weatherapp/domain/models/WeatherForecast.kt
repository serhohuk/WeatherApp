package com.serhohuk.weatherapp.domain.models

import com.serhohuk.weatherapp.data.models.forecastdata.City
import com.serhohuk.weatherapp.data.models.forecastdata.WeatherInfoResponse

data class WeatherForecast(
    var city: City?,
    var cnt: Int?,
    var list: List<WeatherInfoResponse>?,
)

data class SimpleForecast(
    var imageId : String,
    var dt : String,
    var minT : Int,
    var maxT : Int
)
