package com.example.myday.data.datasource

import android.util.Log
import com.example.myday.data.api.ApiService
import com.example.myday.data.local.database.WeatherDatabase
import com.example.myday.data.model.modelentity.ForecastWeatherData
import com.example.myday.data.model.modelresponse.ForecastWeatherResponse
import com.example.myday.util.dataUtils.SharedPreference
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

class ForecastWeatherDataSource(
    private val apiService: ApiService,
    private val database: WeatherDatabase,
    private val sharedPreference: SharedPreference
) {

    val forecastWeather: Flow<List<ForecastWeatherData>> = flow {
    var job = true
        while (job){

            val targetCity = sharedPreference.getDefaultCity()
            val response = apiService.getForecastWeather(targetCity)
            val completion = CompletableDeferred<List<ForecastWeatherData>>()

            if (response.isSuccessful) {

                response.body()?.let {

                    val listForecastWeatherData = deserializeResponse(targetCity, it)
                    completion.complete(listForecastWeatherData)

                } ?: kotlin.run {
                    database.forecastWeatherDAO.getForecastWeather(targetCity)
                        .map { completion.complete(it) }
                        .catch { completion.completeExceptionally(it) }
                }
            } else {
                completion.completeExceptionally(HttpException(response))
            }

            emit(completion.await())
            job = false
        }
    }.flowOn(Dispatchers.IO)

    private suspend fun deserializeResponse(
        cityName: String?,
        response: ForecastWeatherResponse
    ): List<ForecastWeatherData> {

        val deferred = CompletableDeferred<List<ForecastWeatherData>>()
        val forecastWeatherDataList = mutableListOf<ForecastWeatherData>()
        val result = response.forecastList

        result?.let { forecastResponseList ->

            for (forecastResponse in forecastResponseList) {
                val l = ForecastWeatherData(cityName, forecastResponse)
                Log.i("emerson", l.toString())
                forecastWeatherDataList.add(l)
            }
            deferred.complete(forecastWeatherDataList)
        }

        return deferred.await()
    }
}