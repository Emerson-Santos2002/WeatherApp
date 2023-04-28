package com.example.myday.util.`object`

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

object Network {

    fun isNetworkAvailable(context: Context): Boolean {

        val connectivityManager = context.getSystemService(ConnectivityManager::class.java)
        val currentNetwork = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(currentNetwork)

        return capabilities?.let {

            when {
                it.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return true
                }
                it.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    return true
                }
                else -> return false
            }
        } ?: return false
    }
}