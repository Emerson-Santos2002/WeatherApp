package com.example.myday.data.repository

import android.content.Context
import com.example.myday.util.dataUtils.LocationUtilAutomatic
import com.example.myday.util.dataUtils.LocationUtilManual
import com.example.myday.util.dataUtils.SharedPreference
import com.example.myday.util.`object`.Location
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient

/**
 * Repositório que lida com as requisições de localização e permissão da aplicação
 * @param sharedPreference BD onde fica armazenado a cidade default do úsuario
 */
class IntroLocationRepository(private val sharedPreference: SharedPreference) {

    private lateinit var apiKey: String
    private lateinit var placesClient: PlacesClient

    /**
     * Inicialização obrigatória de todos os componentes do Places requerido na criação da atividade.
     */
    fun initializePlaces(context: Context) {
        apiKey = Location.provideApiKey(context)
        configPLaces(context)
    }

    private fun configPLaces(context: Context){
        Places.initialize(context, apiKey)
        placesClient = Places.createClient(context)
    }

    fun getCityByPlacesAPI(onResult: (String?) -> Unit) {
        LocationUtilAutomatic(apiKey, placesClient).getCityByPLacesAPI{ result -> onResult(result) }
    }

    fun getSuggestionsListByPlacesAPI(text: String, onResult: (List<String>) -> Unit) {

        LocationUtilManual(placesClient).getSuggestionsListByPlacesAPI(text) {list ->
            onResult(list)
        }
    }

    fun saveDefaultCity(cityName: String) = sharedPreference.saveDefaultCity(cityName)
}