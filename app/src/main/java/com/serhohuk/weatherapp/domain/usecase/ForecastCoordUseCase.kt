package com.serhohuk.weatherapp.domain.usecase

import com.serhohuk.weatherapp.domain.repository.WeatherForecastRepository
import javax.inject.Inject

class ForecastCoordUseCase @Inject constructor(private val repository: WeatherForecastRepository) {

    suspend fun execute(latitude : Double,
                        longitude : Double,
                        language : String = "en"
    ) = repository.getForecastWeather(latitude,longitude, language)
}