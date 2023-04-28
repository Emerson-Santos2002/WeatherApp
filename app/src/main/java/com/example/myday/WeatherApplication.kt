package com.example.myday

import android.app.Application
import com.example.myday.domain.di.appModule
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class WeatherApplication : Application() {

    companion object {
        lateinit var instance: Application
            private set
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        instance = this
        startKoin {

            androidLogger()
            androidContext(this@WeatherApplication)
            modules(appModule)
        }
    }
}