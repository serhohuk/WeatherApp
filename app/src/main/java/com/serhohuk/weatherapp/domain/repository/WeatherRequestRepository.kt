package com.serhohuk.weatherapp.domain.repository

import com.serhohuk.weatherapp.domain.models.WeatherCurrent
import retrofit2.Response

interface WeatherRequestRepository {

    suspend fun getCurrentWeather(latitude : Double, longitude : Double) : Response<WeatherCurrent>

    suspend fun getCurrentWeather(cityName : String) : Response<WeatherCurrent>
}