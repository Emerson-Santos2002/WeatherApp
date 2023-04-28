package com.example.myday.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myday.data.local.dao.CurrentWeatherDAO
import com.example.myday.data.local.dao.ForecastWeatherDAO
import com.example.myday.data.model.modelentity.CurrentWeatherData
import com.example.myday.data.model.modelentity.ForecastWeatherData

@Database(entities = [CurrentWeatherData::class, ForecastWeatherData::class], version = 3)
abstract class WeatherDatabase : RoomDatabase() {

    abstract val currentWeatherDAO: CurrentWeatherDAO
    abstract val forecastWeatherDAO: ForecastWeatherDAO

    companion object {
        @Volatile
        private var INSTANCE: WeatherDatabase? = null
        private const val DATABASE_NAME = "WeatherDatabase"

        fun getInstance(androidContext: Context): WeatherDatabase {
            synchronized(this) {
                return INSTANCE ?: buildDatabase(androidContext)
            }
        }

        private fun buildDatabase(context: Context): WeatherDatabase {

            INSTANCE = Room.databaseBuilder(
                context,
                WeatherDatabase::class.java,
                DATABASE_NAME
            ).fallbackToDestructiveMigration()
                .build()

            return INSTANCE as WeatherDatabase
        }
    }
}