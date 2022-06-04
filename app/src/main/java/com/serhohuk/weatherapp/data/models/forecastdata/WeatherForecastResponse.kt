package com.serhohuk.weatherapp.data.models.forecastdata

data class WeatherForecastResponse(
    var city: City?,
    var cnt: Int?,
    var cod: String?,
    var list: List<WeatherInfoResponse>?,
    var message: Int?
)

data class WeatherInfoResponse(
    var clouds: Clouds?,
    var dt: Int?,
    var dt_txt: String?,
    var main: Main?,
    var pop: Double?,
    var rain: Rain?,
    var sys: Sys?,
    var visibility: Int?,
    var weather: List<Weather>?,
    var wind: Wind?
)