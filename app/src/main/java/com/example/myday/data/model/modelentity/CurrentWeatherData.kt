package com.example.myday.data.model.modelentity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.myday.data.model.modelresponse.CurrentWeatherResponse
import com.example.myday.util.`object`.Calendar.getDayOfWeek
import com.example.myday.util.`object`.Calendar.formatHour
import com.example.myday.util.formatting.StringUtils.Companion.formatPercent
import com.example.myday.util.formatting.StringUtils.Companion.formatTemperature
import com.example.myday.util.formatting.StringUtils.Companion.formatVisibility
import com.example.myday.util.formatting.StringUtils.Companion.formatWind

@Entity(tableName = "current_weather")
data class CurrentWeatherData(
    @PrimaryKey
    val city_name : String,
    val hour : String,
    val icon : String,
    val temperature : String,
    val description : String,
    val humidity : String,
    val visibility : String,
    val wind : String,
    val day_week : String = getDayOfWeek()
){
    constructor(weatherData: CurrentWeatherResponse) : this(
        city_name = weatherData.cityName,
        hour = weatherData.dt?.formatHour() ?: "00:00",
        temperature = weatherData.mainInfo?.temp?.formatTemperature() ?: "0",
        humidity = weatherData.mainInfo?.humidity?.toString()?.formatPercent() ?: "0%",
        visibility = weatherData.visibility?.formatVisibility() ?: "0km",
        wind = weatherData.windInfo?.speed?.toString()?.formatWind() ?: "0km/h",
        icon = weatherData.weatherList?.firstOrNull()?.icon ?: "11d",
        description = weatherData.weatherList?.firstOrNull()?.description ?: "previsão"
    )
}
