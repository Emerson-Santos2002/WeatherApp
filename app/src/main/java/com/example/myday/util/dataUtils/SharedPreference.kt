package com.example.myday.util.dataUtils

import android.content.SharedPreferences
import com.example.myday.util.`object`.Constants

class SharedPreference(
     private val sharedPreferences: SharedPreferences
) {

    fun isFirstAppLaunch(): Boolean {
        return sharedPreferences.getBoolean(Constants.IS_FIRST_APP_LAUNCH, true)
    }

    fun saveFirstAppLaunch() {
        sharedPreferences.edit().putBoolean(Constants.IS_FIRST_APP_LAUNCH, false).apply()
    }

    fun getDefaultCity(): String? {
        return sharedPreferences.getString(Constants.DEFAULT_CITY, "")
    }

    fun saveDefaultCity(cityName: String) {
        sharedPreferences.edit().putString(Constants.DEFAULT_CITY, cityName).apply()
    }
}