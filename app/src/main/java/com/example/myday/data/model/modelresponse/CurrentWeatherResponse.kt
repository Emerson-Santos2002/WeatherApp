package com.example.myday.data.model.modelresponse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class CurrentWeatherResponse(
    @JsonProperty("weather") val weatherList: List<Weather>?,
    @JsonProperty("main") val mainInfo: Main?,
    @JsonProperty("wind") val windInfo: Wind?,
    @JsonProperty("visibility") val visibility: Short?,
    @JsonProperty("dt") val dt: Long?,
    @JsonProperty("name") val cityName: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Weather(
    @JsonProperty("description")val description: String?,
    @JsonProperty("icon")val icon: String?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Main(
    @JsonProperty("temp")val temp: Float?,
    @JsonProperty("humidity")val humidity: Byte?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Wind(
    @JsonProperty("speed")val speed: Float?
)