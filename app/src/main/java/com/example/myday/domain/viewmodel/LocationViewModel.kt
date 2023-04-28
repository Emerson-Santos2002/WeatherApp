package com.example.myday.domain.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myday.data.repository.IntroLocationRepository
import com.example.myday.ui.fragment.intro.IntroLocalityFragment
import com.example.myday.util.dataUtils.IntroLocalityUIState
import com.example.myday.util.`object`.Location
import com.example.myday.util.`object`.Location.isLocationEnabled
import com.example.myday.util.`object`.Network.isNetworkAvailable

class LocationViewModel(private val introLocationRepository: IntroLocationRepository)
    : ViewModel() {

    private val _suggestions = MutableLiveData<List<String>>()
    val suggestions: LiveData<List<String>>
        get() = _suggestions

    private val _introSearchState = MutableLiveData<IntroLocalityUIState>(IntroLocalityUIState.RequestPermissionLoading)
    val introSearchState: LiveData<IntroLocalityUIState>
        get() = _introSearchState

    private var requestWasMade = false

    fun initializePlaces(context: Context) {
        introLocationRepository.initializePlaces(context)
    }

    fun createRequestLocationPermission(fragment: IntroLocalityFragment, context: Context) {

        Location.createRequestLocationPermission(fragment) { permissionRequestResult ->
            if (permissionRequestResult){
                checkLocationEnabled(context)
            } else {
                _introSearchState.postValue(IntroLocalityUIState.AutoSearchError)
            }
        }
    }

    fun requestLocationPermission(context: Context) {

        if (isNetworkAvailable(context)) {
            if (!requestWasMade) Location.requestLocationPermission()
            requestWasMade = true
        }
        else {
            _introSearchState.postValue(IntroLocalityUIState.NetworkDisabled)
        }
    }

    fun checkLocationEnabled(context: Context) {
        when {
            !isLocationEnabled(context) -> {
                _introSearchState.postValue(IntroLocalityUIState.LocationDisabled)
            }
            else -> getCityByPlacesAPI()
        }
    }

    fun awaitResultLocationActivation() {
        _introSearchState.postValue(IntroLocalityUIState.RequestPermissionLoading)
    }

    private fun getCityByPlacesAPI() {

        _introSearchState.postValue(IntroLocalityUIState.LoadingSelectedCity)

        introLocationRepository.getCityByPlacesAPI { result ->

            result?.let {
                saveDefaultCity(it)
                _introSearchState.postValue(IntroLocalityUIState.SelectedCityLoaded(it))
            } ?: run {
                _introSearchState.postValue(IntroLocalityUIState.AutoSearchError)
            }
        }
    }

    fun getSuggestionsList(text: String) {

        introLocationRepository.getSuggestionsListByPlacesAPI(text) { list ->
            _suggestions.postValue(list)
        }
    }

    fun saveDefaultCity(city: String) {
        introLocationRepository.saveDefaultCity(city)
    }
}