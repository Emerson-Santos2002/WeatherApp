package com.example.myday.domain.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myday.data.model.modelentity.CurrentWeatherData
import com.example.myday.data.repository.FavoriteWeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(private val favoriteWeatherRepository: FavoriteWeatherRepository) : ViewModel() {

    private val _listFavoriteCities = MutableStateFlow<List<CurrentWeatherData>>(emptyList())
    val listFavoriteCities : StateFlow<List<CurrentWeatherData>>
        get() = _listFavoriteCities

    init {

        viewModelScope.launch(Dispatchers.IO) {

            favoriteWeatherRepository.favoriteCities.collect { list ->

                _listFavoriteCities.value = list
            }
        }
    }

    fun saveInFavorites(city: String) = viewModelScope.launch(Dispatchers.IO) {
        favoriteWeatherRepository.saveCityInFavorites(city)
    }

    fun deleteCityFromFavorites(cityName: String) = viewModelScope.launch(Dispatchers.IO) {
        favoriteWeatherRepository.deleteCityFromFavorites(cityName)
    }
}