package com.example.myday.domain.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myday.util.dataUtils.SharedPreference

class SplashViewModel(private val sharedPreference: SharedPreference): ViewModel() {

    val iFirstTime : Boolean = sharedPreference.isFirstAppLaunch()
    fun saveFirstAppLaunch() = sharedPreference.saveFirstAppLaunch()

}