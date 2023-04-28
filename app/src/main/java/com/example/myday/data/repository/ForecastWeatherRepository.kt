package com.example.myday.data.repository

import android.util.Log
import com.example.myday.data.datasource.ForecastWeatherDataSource
import com.example.myday.data.local.database.WeatherDatabase
import com.example.myday.data.model.modelentity.ForecastWeatherData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

class ForecastWeatherRepository(
    private val database: WeatherDatabase,
    forecastWeatherDataSource: ForecastWeatherDataSource
) {

    val forecastWeather: Flow<List<ForecastWeatherData>> =
        forecastWeatherDataSource.forecastWeather
            .onEach { list ->
                Log.i("emerson", "Repository: $list")
                saveForecastWeather(list)
            }

    private suspend fun saveForecastWeather(forecastWeatherData: List<ForecastWeatherData>) {
        database.forecastWeatherDAO.saveForecastWeather(forecastWeatherData)
    }
}