package com.example.myday.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myday.data.model.modelentity.CurrentWeatherData
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrentWeatherDAO {

    @Query("SELECT * FROM current_weather WHERE city_name = :cityName")
    fun getCurrentWeather(cityName: String?): Flow<CurrentWeatherData>

    @Query("SELECT * FROM current_weather")
    fun getListFavoriteCities(): Flow<List<CurrentWeatherData>>

    @Query("DELETE FROM current_weather WHERE city_name = :cityName")
    suspend fun deleteCurrentWeather(cityName: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCurrentWeather(currentWeather: CurrentWeatherData)

}