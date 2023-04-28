package com.example.myday.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myday.data.model.modelentity.ForecastWeatherData
import kotlinx.coroutines.flow.Flow

@Dao
interface ForecastWeatherDAO {

    @Query("SELECT * FROM forecast_weather WHERE cityName = :cityName")
    fun getForecastWeather(cityName: String?): Flow<List<ForecastWeatherData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveForecastWeather(listForecastWeatherData: List<ForecastWeatherData>)
}