package com.example.myday.util.dataUtils

import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.errors.ApiException
import com.google.maps.model.AddressComponentType

class LocationUtilManual(private val placesClient: PlacesClient){

    fun getSuggestionsListByPlacesAPI(textQuery: String, onResult: (List<String>) -> Unit) {

        val sessionToken: AutocompleteSessionToken = AutocompleteSessionToken.newInstance()
        val filter = AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_2.toString()
        val suggestions = mutableListOf<String>()

        val request = FindAutocompletePredictionsRequest.builder()
            .setTypesFilter(mutableListOf(filter))
            .setSessionToken(sessionToken)
            .setQuery(textQuery)
            .build()

        val task: Task<FindAutocompletePredictionsResponse> = placesClient.
        findAutocompletePredictions(request)

        task.addOnSuccessListener { response: FindAutocompletePredictionsResponse ->

            Log.i("emerson", "LocationManualUtil:")
            for ((cont, item) in response.autocompletePredictions.withIndex()){
                val city = item.getPrimaryText(null).toString()
                suggestions.add(city)
                Log.i("emerson", "${cont}: $city")
            }
            onResult(suggestions)

        }.addOnFailureListener { exception: Exception ->
            if (exception is ApiException) {
                Log.e("emerson", "Lugar não encontrado: " + exception.message)
            }
        }
    }
}