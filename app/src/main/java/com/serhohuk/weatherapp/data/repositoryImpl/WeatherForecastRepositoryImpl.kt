package com.serhohuk.weatherapp.data.repositoryImpl

import com.serhohuk.weatherapp.data.api.RetrofitWeather.weatherAPI
import com.serhohuk.weatherapp.data.api.RetrofitWeather.WEATHER_API
import com.serhohuk.weatherapp.data.api.RetrofitWeather.getError
import com.serhohuk.weatherapp.data.mappers.WeatherMapper
import com.serhohuk.weatherapp.data.utils.ErrorData
import com.serhohuk.weatherapp.data.utils.Resource
import com.serhohuk.weatherapp.domain.models.WeatherForecast
import com.serhohuk.weatherapp.domain.repository.WeatherForecastRepository
import retrofit2.Response


class WeatherForecastRepositoryImpl : WeatherForecastRepository {

    override suspend fun getForecastWeather(
        latitude: Double,
        longitude: Double,
        language : String
    ): Resource<WeatherForecast> {
        val response = weatherAPI.forecastWeather5Days(latitude, longitude, "metric", language, WEATHER_API)
        if (response.isSuccessful){
            val weatherForecast = WeatherMapper.mapForecast(response.body()!!)
            return Resource.Success(weatherForecast)
        }
        val error = getError(response.errorBody()!!.charStream())
        return Resource.Error(error = ErrorData(error?.cod,error?.message))
    }

    override suspend fun getForecastWeather(cityName: String, language: String): Response<WeatherForecast> {
        TODO("Not yet implemented")
    }
}