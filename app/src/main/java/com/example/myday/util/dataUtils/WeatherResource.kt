package com.example.myday.util.dataUtils

sealed class WeatherResource<T> (
    val data: T? = null,
    val throwable: Int? = null
){
    class Loading<T>(data: T? = null) : WeatherResource<T>(data)
    class Success<T>(data: T, throwable: Int? = null) : WeatherResource<T>(data, throwable)
    class Failure<T>(throwable: Int, data: T? = null) : WeatherResource<T>(data, throwable)
}
