package com.serhohuk.weatherapp.domain.usecase

import com.serhohuk.weatherapp.data.repositoryImpl.WeatherRequestRepositoryImpl
import com.serhohuk.weatherapp.domain.repository.WeatherRequestRepository
import javax.inject.Inject

class WeatherCoordUseCase @Inject constructor(
    private val repository: WeatherRequestRepository
) {

    suspend fun execute(latitude : Double,
                        longitude : Double,
                        language : String = "en"
    ) = repository.getCurrentWeather(latitude, longitude, language)
}