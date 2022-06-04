package com.serhohuk.weatherapp.data.mappers

import com.serhohuk.weatherapp.data.models.forecastdata.WeatherForecastResponse
import com.serhohuk.weatherapp.data.models.weatherdata.WeatherTodayResponse
import com.serhohuk.weatherapp.domain.models.WeatherCurrent
import com.serhohuk.weatherapp.domain.models.WeatherForecast

object WeatherMapper {

    fun mapForecast(data : WeatherForecastResponse) : WeatherForecast {
        return WeatherForecast(
            data.city,
            data.cnt,
            data.list
        )
    }

    fun mapCurrent(data : WeatherTodayResponse) : WeatherCurrent {
        return WeatherCurrent(
            data.base,
            data.clouds,
            data.coord,
            data.main,
            data.name,
            data.sys,
            data.visibility,
            data.weather,
            data.wind
        )
    }
}