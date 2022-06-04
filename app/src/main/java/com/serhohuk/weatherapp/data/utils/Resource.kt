package com.serhohuk.weatherapp.data.utils

sealed class Resource<T>(
    val data : T? = null,
    val error : ErrorData? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(error: ErrorData, data: T? = null) : Resource<T>(data, error)
    class Loading<T> : Resource<T>()
    class Empty<T> : Resource<T>()
}