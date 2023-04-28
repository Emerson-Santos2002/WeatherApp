package com.example.myday.data.model.modelentity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myday.data.model.modelresponse.ForecastResponse
import com.example.myday.util.formatting.StringUtils.Companion.formatTemperature
import com.example.myday.util.`object`.Calendar.formatHour

@Entity("forecast_weather")
data class ForecastWeatherData(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val cityName: String,
    val hour: String,
    val icon: String,
    val temperature: String
) {
    constructor(cityName: String?, forecastResponse: ForecastResponse) : this(
        cityName = cityName!!,
        hour = forecastResponse.dt?.formatHour() ?: "00:00",
        icon = forecastResponse.weatherList?.firstOrNull()?.icon ?: "11d",
        temperature = forecastResponse.mainInfo?.temperature?.formatTemperature() ?: "0"
    )
}
