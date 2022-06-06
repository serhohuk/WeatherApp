package com.serhohuk.weatherapp.presentation.utils

interface OnActionResponse<T> {

    fun onResponse(data : T)
}