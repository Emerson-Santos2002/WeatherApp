package com.example.myday.data.model.modelresponse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class ForecastWeatherResponse(
    @JsonProperty("list") val forecastList: List<ForecastResponse>?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ForecastResponse(
    @JsonProperty("dt") val dt: Long?,
    @JsonProperty("main") val mainInfo: MainResponse?,
    @JsonProperty("weather") val weatherList: List<WeatherResponse>?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MainResponse(
    @JsonProperty("temp") val temperature: Float?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class WeatherResponse(
    @JsonProperty("icon") val icon: String?
)
