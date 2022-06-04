package com.serhohuk.weatherapp.di

import com.serhohuk.weatherapp.data.repositoryImpl.WeatherForecastRepositoryImpl
import com.serhohuk.weatherapp.domain.repository.WeatherForecastRepository
import com.serhohuk.weatherapp.domain.usecase.ForecastCoordUseCase
import com.serhohuk.weatherapp.domain.usecase.ForecastUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun provideForecastCoordUseCase(repository: WeatherForecastRepository) = ForecastCoordUseCase(repository)

    @Singleton
    @Provides
    fun provideForecastUseCase(repository: WeatherForecastRepository) = ForecastUseCase(repository)
}