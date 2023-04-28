package com.example.myday.data.repository

import com.example.myday.data.datasource.CurrentWeatherDataSource
import com.example.myday.data.local.database.WeatherDatabase
import com.example.myday.data.model.modelentity.CurrentWeatherData
import kotlinx.coroutines.flow.Flow

class FavoriteWeatherRepository(
    private val database: WeatherDatabase,
    private val currentWeatherDataSource: CurrentWeatherDataSource
    ) {

    val favoriteCities: Flow<List<CurrentWeatherData>> =
        database.currentWeatherDAO.getListFavoriteCities()

    suspend fun saveCityInFavorites(city: String){
        currentWeatherDataSource.saveCityInFavorites(city)
    }

    suspend fun deleteCityFromFavorites(cityName: String){
        database.currentWeatherDAO.deleteCurrentWeather(cityName)
    }
}