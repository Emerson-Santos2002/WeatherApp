package com.example.myday.data.repository

import com.example.myday.data.datasource.CurrentWeatherDataSource
import com.example.myday.data.local.database.WeatherDatabase
import com.example.myday.data.model.modelentity.CurrentWeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class CurrentWeatherRepository(
    private val database: WeatherDatabase,
    currentWeatherDataSource: CurrentWeatherDataSource
) {

    val currentWeather: Flow<CurrentWeatherData> =
        currentWeatherDataSource.currentWeather
            .onEach { data ->
                saveCurrentWeather(data)
            }

    private suspend fun saveCurrentWeather(currentWeatherData: CurrentWeatherData) {
        database.currentWeatherDAO.saveCurrentWeather(currentWeatherData)
    }
}


