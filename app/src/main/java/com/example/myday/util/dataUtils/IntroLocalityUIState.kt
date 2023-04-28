package com.example.myday.util.dataUtils

sealed class IntroLocalityUIState {

    object RequestPermissionLoading : IntroLocalityUIState()

    object NetworkDisabled : IntroLocalityUIState()
    object LocationDisabled : IntroLocalityUIState()
    object AutoSearchError : IntroLocalityUIState()

    object LoadingSelectedCity : IntroLocalityUIState()
    data class SelectedCityLoaded(val city: String) : IntroLocalityUIState()
}


