package com.example.myday.data.datasource

import com.example.myday.data.api.ApiService
import com.example.myday.data.local.database.WeatherDatabase
import com.example.myday.data.model.modelentity.CurrentWeatherData
import com.example.myday.util.dataUtils.SharedPreference
import com.example.myday.util.`object`.Constants
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import retrofit2.HttpException

class CurrentWeatherDataSource(
    private val apiService: ApiService,
    private val database: WeatherDatabase,
    private val sharedPreference: SharedPreference
) {

    val currentWeather: Flow<CurrentWeatherData> = flow {
        while (true) {

            val targetCity = sharedPreference.getDefaultCity()
            val completion = CompletableDeferred<CurrentWeatherData>()
            val response = apiService.getCurrentWeather(targetCity)

            if (response.isSuccessful) {

                response.body()?.let {

                    completion.complete(CurrentWeatherData(it))

                } ?: run{
                    database.currentWeatherDAO.getCurrentWeather(targetCity)
                        .map { completion.complete(it) }
                        .catch { completion.completeExceptionally(it) }
                }

            } else {
                completion.completeExceptionally(HttpException(response))
            }

            emit(completion.await())
            delay(Constants.DELAY)
        }
    }.flowOn(Dispatchers.IO)

    suspend fun saveCityInFavorites(targetCity: String) {

        val completion = CompletableDeferred<CurrentWeatherData>()
        val response = apiService.getCurrentWeather(targetCity)

        if (response.isSuccessful && response.body() != null)
            completion.complete(CurrentWeatherData(response.body()!!))

        else completion.cancel()

        database.currentWeatherDAO.saveCurrentWeather(completion.await())
    }
}