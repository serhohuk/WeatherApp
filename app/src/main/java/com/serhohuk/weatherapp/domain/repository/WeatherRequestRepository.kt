package com.serhohuk.weatherapp.domain.repository

interface WeatherRequestRepository {

    suspend fun getCurrentWeather()
}