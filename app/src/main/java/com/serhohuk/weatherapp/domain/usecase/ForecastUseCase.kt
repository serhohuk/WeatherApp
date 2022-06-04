package com.serhohuk.weatherapp.domain.usecase

import com.serhohuk.weatherapp.domain.repository.WeatherForecastRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ForecastUseCase @Inject constructor(private val repository: WeatherForecastRepository) {

    suspend fun execute(cityName : String,
                        language : String = "en"
    ) = repository.getForecastWeather(cityName, language)
}