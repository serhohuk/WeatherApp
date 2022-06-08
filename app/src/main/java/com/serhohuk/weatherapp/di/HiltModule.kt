package com.serhohuk.weatherapp.di

import android.content.Context
import android.content.SharedPreferences
import com.serhohuk.weatherapp.data.repositoryImpl.WeatherForecastRepositoryImpl
import com.serhohuk.weatherapp.data.repositoryImpl.WeatherRequestRepositoryImpl
import com.serhohuk.weatherapp.domain.repository.WeatherForecastRepository
import com.serhohuk.weatherapp.domain.repository.WeatherRequestRepository
import com.serhohuk.weatherapp.domain.usecase.ForecastCoordUseCase
import com.serhohuk.weatherapp.domain.usecase.ForecastUseCase
import com.serhohuk.weatherapp.domain.usecase.WeatherCoordUseCase
import com.serhohuk.weatherapp.domain.usecase.WeatherUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HiltModule {


    @Singleton
    @Provides
    fun provideWeatherForecastRepo(): WeatherForecastRepository {
        return  WeatherForecastRepositoryImpl()
    }

    @Singleton
    @Provides
    fun provideWeatherCurrentRepo() : WeatherRequestRepository {
        return WeatherRequestRepositoryImpl()
    }

    @Singleton
    @Provides
    fun provideForecastCoordUseCase(repository: WeatherForecastRepository) = ForecastCoordUseCase(repository)

    @Singleton
    @Provides
    fun provideForecastUseCase(repository: WeatherForecastRepository) = ForecastUseCase(repository)

    @Singleton
    @Provides
    fun provideCurrentUseCase(repository : WeatherRequestRepository) = WeatherUseCase(repository)

    @Singleton
    @Provides
    fun provideCurrentCoordUseCase(repository : WeatherRequestRepository) = WeatherCoordUseCase(repository)

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
    }
}