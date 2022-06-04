package com.serhohuk.weatherapp.data.mappers

import com.serhohuk.weatherapp.data.models.forecastdata.WeatherForecastResponse
import com.serhohuk.weatherapp.domain.models.WeatherForecast

object WeatherMapper {

    fun mapForecast(data : WeatherForecastResponse) : WeatherForecast {
        return WeatherForecast(
            data.city,
            data.cnt,
            data.list
        )
    }
}