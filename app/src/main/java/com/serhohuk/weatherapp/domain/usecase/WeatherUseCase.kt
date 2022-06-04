package com.serhohuk.weatherapp.domain.usecase

import com.serhohuk.weatherapp.domain.repository.WeatherRequestRepository
import javax.inject.Inject

class WeatherUseCase @Inject constructor(
    private val repository: WeatherRequestRepository
) {
    suspend fun execute(cityName : String, language : String = "en") = repository.getCurrentWeather(cityName, language)
}