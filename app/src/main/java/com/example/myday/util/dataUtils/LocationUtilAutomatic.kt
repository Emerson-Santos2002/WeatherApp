package com.example.myday.util.dataUtils

import android.annotation.SuppressLint
import android.util.Log
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.google.maps.model.AddressComponentType
import com.google.maps.model.LatLng
import java.util.Collections

/**
 * Classe que cuida do request automático de localização
 * @param placesClient instância que é responsável pelas execuções da biblioteca Places do Google Maps.
 */
class LocationUtilAutomatic(private val apiKey: String, private val placesClient: PlacesClient) {

    @SuppressLint("MissingPermission")
    fun getCityByPLacesAPI(onCityResult: (String?) -> Unit) {

        val placeFields: List<Place.Field> = Collections.singletonList(Place.Field.LAT_LNG)
        val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)
        val placeResponse: Task<FindCurrentPlaceResponse> = placesClient.findCurrentPlace(request)

        placeResponse.addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val response: FindCurrentPlaceResponse = task.result
                var cityWithMaxLikelihood: Place? = null
                var probabilityUserLocation = -1.0

                for (location in response.placeLikelihoods) {

                    if (location.likelihood > probabilityUserLocation) {
                        probabilityUserLocation = location.likelihood
                        cityWithMaxLikelihood = location.place
                    }
                }

                cityWithMaxLikelihood?.latLng?.let { latLng ->

                    val lat = latLng.latitude
                    val lng = latLng.longitude

                    reverseGeocodeForCity(LatLng(lat, lng)) {
                        Log.i("emerson", "city: $it")
                        onCityResult(it)
                    }
                } ?: onCityResult(null)
            } else onCityResult(null)
        }

    }

    private fun reverseGeocodeForCity(location: LatLng, onCityResult: (String?) -> Unit) {

        val geoContext = GeoApiContext.Builder().apiKey(apiKey).build()
        val results = GeocodingApi.reverseGeocode(geoContext, location).await()

        if (results.isNotEmpty()) {
            val cityName = results[0].addressComponents.firstOrNull {
                it.types.contains(AddressComponentType.ADMINISTRATIVE_AREA_LEVEL_2)
            }?.shortName

            onCityResult(cityName)
        }
    }
}