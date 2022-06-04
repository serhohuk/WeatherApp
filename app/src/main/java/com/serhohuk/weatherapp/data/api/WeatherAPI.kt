package com.serhohuk.weatherapp.data.api

import com.serhohuk.weatherapp.data.models.forecastdata.WeatherForecastResponse
import com.serhohuk.weatherapp.data.models.weatherdata.WeatherTodayResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPI {

    @GET("forecast")
    suspend fun forecastWeather5Days(
        @Query("lat")
        latitude : Double,
        @Query("lon")
        longitude : Double,
        @Query("units")
        units : String,
        @Query("lang")
        language : String,
        @Query("appid")
        apiKey : String
    ) : Response<WeatherForecastResponse>

    @GET("forecast")
    suspend fun forecastWeather5Days(
        @Query("q")
        cityName : String,
        @Query("units")
        units : String,
        @Query("lang")
        language : String,
        @Query("appid")
        apiKey : String
    ) : Response<WeatherForecastResponse>

    @GET("weather")
    suspend fun currentWeather(
        @Query("lat")
        latitude : Double,
        @Query("lon")
        longitude : Double,
        @Query("units")
        units : String,
        @Query("lang")
        language : String,
        @Query("appid")
        apiKey : String
    ) : Response<WeatherTodayResponse>

    @GET("weather")
    suspend fun currentWeather(
        @Query("q")
        cityName: String,
        @Query("units")
        units : String,
        @Query("lang")
        language : String,
        @Query("appid")
        apiKey : String
    ) : Response<WeatherTodayResponse>
}