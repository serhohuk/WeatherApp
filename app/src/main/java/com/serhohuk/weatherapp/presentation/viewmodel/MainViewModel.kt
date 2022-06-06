package com.serhohuk.weatherapp.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serhohuk.weatherapp.data.utils.Resource
import com.serhohuk.weatherapp.domain.models.GeoPoint
import com.serhohuk.weatherapp.domain.models.WeatherCurrent
import com.serhohuk.weatherapp.domain.models.WeatherForecast
import com.serhohuk.weatherapp.domain.usecase.ForecastCoordUseCase
import com.serhohuk.weatherapp.domain.usecase.ForecastUseCase
import com.serhohuk.weatherapp.domain.usecase.WeatherCoordUseCase
import com.serhohuk.weatherapp.domain.usecase.WeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val forecastCoordUseCase: ForecastCoordUseCase,
    private val forecastUseCase: ForecastUseCase,
    private val weatherCoordUseCase: WeatherCoordUseCase,
    private val weatherUseCase: WeatherUseCase
): ViewModel() {

    private val flowForecastWeather : MutableStateFlow<Resource<WeatherForecast>> = MutableStateFlow(Resource.Empty())
    val stateFlowForecast = flowForecastWeather.asStateFlow()

    private val flowCurrentWeather : MutableStateFlow<Resource<WeatherCurrent>> = MutableStateFlow(Resource.Empty())
    val stateFlowCurrent = flowCurrentWeather.asStateFlow()

    val coordinatesData : MutableLiveData<GeoPoint> = MutableLiveData()

    fun getWeatherForecastCoord(latitude : Double, longitude : Double, language : String = "en"){
        viewModelScope.launch {
            flowForecastWeather.value = Resource.Loading()
            val response = forecastCoordUseCase.execute(latitude, longitude, language)
            flowForecastWeather.value = response
        }
    }

    fun getWeatherForecast(cityName : String, language : String = "en"){
        viewModelScope.launch {
            flowForecastWeather.value = Resource.Loading()
            val response = forecastUseCase.execute(cityName, language)
            flowForecastWeather.value = response
        }
    }

    fun getWeatherCurrent(cityName: String, language: String){
        viewModelScope.launch {
            flowCurrentWeather.value = Resource.Loading()
            val response = weatherUseCase.execute(cityName,language)
           flowCurrentWeather.value = response
        }
    }

    fun getWeatherCurrentCoord(latitude : Double, longitude : Double, language: String){
        viewModelScope.launch {
            flowCurrentWeather.value = Resource.Loading()
            val response = weatherCoordUseCase.execute(latitude, longitude, language)
            flowCurrentWeather.value = response
        }
    }
}