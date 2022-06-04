package com.serhohuk.weatherapp.data.repositoryImpl

import com.serhohuk.weatherapp.data.api.RetrofitWeather
import com.serhohuk.weatherapp.data.utils.Resource
import com.serhohuk.weatherapp.domain.models.WeatherCurrent
import com.serhohuk.weatherapp.domain.repository.WeatherRequestRepository
import com.serhohuk.weatherapp.data.api.RetrofitWeather.weatherAPI
import com.serhohuk.weatherapp.data.api.RetrofitWeather.WEATHER_API
import com.serhohuk.weatherapp.data.mappers.WeatherMapper
import com.serhohuk.weatherapp.data.utils.ErrorData

class WeatherRequestRepositoryImpl : WeatherRequestRepository {

    override suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        language: String
    ): Resource<WeatherCurrent> {
        val response = weatherAPI.currentWeather(latitude, longitude, "metric", language, WEATHER_API)
        if (response.isSuccessful){
            val weatherCurrent = WeatherMapper.mapCurrent(response.body()!!)
            return Resource.Success(weatherCurrent)
        }
        val error = RetrofitWeather.getError(response.errorBody()!!.charStream())
        return Resource.Error(error = ErrorData(error?.cod,error?.message))
    }

    override suspend fun getCurrentWeather(
        cityName: String,
        language: String
    ): Resource<WeatherCurrent> {
        val response = weatherAPI.currentWeather(cityName, "metric", language, WEATHER_API)
        if (response.isSuccessful){
            val weatherCurrent = WeatherMapper.mapCurrent(response.body()!!)
            return Resource.Success(weatherCurrent)
        }
        val error = RetrofitWeather.getError(response.errorBody()!!.charStream())
        return Resource.Error(error = ErrorData(error?.cod,error?.message))
    }


}