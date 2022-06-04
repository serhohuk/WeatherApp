package com.serhohuk.weatherapp.domain.repository

import com.serhohuk.weatherapp.data.utils.Resource
import com.serhohuk.weatherapp.domain.models.WeatherForecast
import retrofit2.Response

interface WeatherForecastRepository {

    suspend fun getForecastWeather(latitude : Double,
                                   longitude : Double,
                                   language : String = "en") : Resource<WeatherForecast>

    suspend fun getForecastWeather(cityName : String,
                                    language: String = "en") : Response<WeatherForecast>
}