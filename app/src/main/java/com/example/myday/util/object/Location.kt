package com.example.myday.util.`object`

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

object Location {

    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>

    fun createRequestLocationPermission(fragment: Fragment, onResult: (Boolean) -> Unit) {

        requestPermissionLauncher = fragment.registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { permissionRequestResult ->
            onResult(permissionRequestResult)
        }
    }

    fun requestLocationPermission(){
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    fun isLocationEnabled(context: Context): Boolean{
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @Suppress("DEPRECATION")
    fun provideApiKey(context: Context): String {
        return context.packageManager
            .getApplicationInfo(
                context.packageName,
                PackageManager.GET_META_DATA
            ).metaData
            .getString(Constants.MY_DAY_API_KEY)
            .toString()
    }
}