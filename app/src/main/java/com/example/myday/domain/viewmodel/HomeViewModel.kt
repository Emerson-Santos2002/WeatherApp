package com.example.myday.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myday.data.model.modelentity.CurrentWeatherData
import com.example.myday.data.model.modelentity.ForecastWeatherData
import com.example.myday.data.repository.CurrentWeatherRepository
import com.example.myday.data.repository.ForecastWeatherRepository
import com.example.myday.util.dataUtils.WeatherResource
import com.example.myday.util.`object`.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.HttpException

class HomeViewModel(
    private val currentWeatherRepository: CurrentWeatherRepository,
    private val forecastWeatherRepository: ForecastWeatherRepository
) : ViewModel() {

    private val _currentWeather = MutableStateFlow<WeatherResource<CurrentWeatherData>>(WeatherResource.Loading())
    val currentWeather: StateFlow<WeatherResource<CurrentWeatherData>>
        get() = _currentWeather

    private val _forecastWeather = MutableStateFlow<WeatherResource<List<ForecastWeatherData>>>(WeatherResource.Loading())
    val forecastWeather: StateFlow<WeatherResource<List<ForecastWeatherData>>>
        get() = _forecastWeather

    private var currentWeatherJob: Job? = null

    init {

        viewModelScope.launch(Dispatchers.IO) {

            forecastWeatherRepository.forecastWeather
                .catch { exception ->

                    when (exception) {
                        is HttpException ->
                            _forecastWeather.value =
                                WeatherResource.Failure(exception.code())

                        is NullPointerException ->
                            _forecastWeather.value =
                                WeatherResource.Failure(Constants.EMPTY_BODY)
                    }
                }
                .collect{ forecastWeatherData ->
                    _forecastWeather.value =
                        WeatherResource.Success(forecastWeatherData)
                }
        }
    }

    fun startCurrentWeatherRequests() {

        var cacheCurrentWeather: CurrentWeatherData? = null

        currentWeatherJob = viewModelScope.launch(Dispatchers.IO) {

            currentWeatherRepository.currentWeather
                .catch { exception ->

                    when (exception) {
                        is HttpException ->
                            _currentWeather.value =
                                WeatherResource.Failure(exception.code())

                        is NullPointerException ->
                            _currentWeather.value =
                                WeatherResource.Failure(Constants.EMPTY_BODY)
                    }
                }
                .collect { currentWeatherData ->

                    // Atualizar UI com os dados obtidos
                    if (currentWeatherData != cacheCurrentWeather) {
                        cacheCurrentWeather = currentWeatherData
                        _currentWeather.value =
                            WeatherResource.Success(currentWeatherData)

                    }
                }
        }
    }

    fun cancelRequests() {
        currentWeatherJob?.cancel()
    }
}