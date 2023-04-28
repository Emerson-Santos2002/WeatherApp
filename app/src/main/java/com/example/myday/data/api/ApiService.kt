package com.example.myday.data.api

import com.example.myday.data.model.modelresponse.CurrentWeatherResponse
import com.example.myday.data.model.modelresponse.ForecastWeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {
        private const val KEY_API = "158ecf6ae7ce89ca63dc99eede1d8682"
    }

    /**
     * Realiza uma requisição GET com o objetivo de obter informações climáticas atuais.
     * Para mais informações consulte a documentação em https://openweathermap.org/current.
     */
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("q") city_name: String?,
        @Query("lang") lang: String = "pt_br",
        @Query("units") units: String = "metric",
        @Query("appid") key: String = KEY_API
    ) : Response<CurrentWeatherResponse>

    /**
     * Realiza uma requisição para obter informações climáticas futuras.
     * Para mais informações sobre os parâmetros, consulte a documentação em https://openweathermap.org/forecast5.
     * @return Um objeto do tipo Call<ForecastResponse>, que contém as informações climáticas futuras.
     */
    @GET("forecast")
    suspend fun getForecastWeather(
        @Query("q") city_name: String?,
        @Query("cnt") timestamp: Int = 8,
        @Query("lang") lang: String = "pt_br",
        @Query("units") units: String = "metric",
        @Query("appid") key: String = KEY_API
    ) : Response<ForecastWeatherResponse>
}