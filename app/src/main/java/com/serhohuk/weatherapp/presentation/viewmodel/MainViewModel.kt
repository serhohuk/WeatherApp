package com.serhohuk.weatherapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serhohuk.weatherapp.data.utils.Resource
import com.serhohuk.weatherapp.domain.usecase.ForecastCoordUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val forecastCoordUseCase: ForecastCoordUseCase
): ViewModel() {


    fun getResponse(){
        viewModelScope.launch {
            val response = forecastCoordUseCase.execute(48.86, 2.34)
            when(response){
                is Resource.Success->{
                    Log.d("WEATHER_TEST", response.data.toString())
                }
                is  Resource.Error -> {
                    Log.d("WEATHER_TEST", response.error!!.code.toString())
                }
            }
        }
    }
}