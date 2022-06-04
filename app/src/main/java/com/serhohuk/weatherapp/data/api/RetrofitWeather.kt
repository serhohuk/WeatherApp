package com.serhohuk.weatherapp.data.api

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.serhohuk.weatherapp.data.models.WeatherError
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.Reader

object RetrofitWeather {

    private const val WEATHER_BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val WEATHER_API = "92d1fb05d8d9932d02bddf8c32f79726"

    private val retrofit by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
        Retrofit.Builder()
            .baseUrl(WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val weatherAPI = retrofit.create(WeatherAPI::class.java)

    fun getError(reader: Reader): WeatherError? {
        val type = object : TypeToken<WeatherError>() {}.type
        return Gson().fromJson(reader, type)
    }
}