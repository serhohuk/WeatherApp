package com.serhohuk.weatherapp.domain.repository

import com.serhohuk.weatherapp.data.utils.Resource
import com.serhohuk.weatherapp.domain.models.WeatherCurrent
import retrofit2.Response

interface WeatherRequestRepository {

    suspend fun getCurrentWeather(latitude : Double, longitude : Double, language : String) : Resource<WeatherCurrent>

    suspend fun getCurrentWeather(cityName : String, language: String) : Resource<WeatherCurrent>
}