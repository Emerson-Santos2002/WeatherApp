package com.example.myday.domain.di

import android.content.Context
import android.content.SharedPreferences
import com.example.myday.data.api.ApiService
import com.example.myday.data.datasource.CurrentWeatherDataSource
import com.example.myday.data.datasource.ForecastWeatherDataSource
import com.example.myday.data.local.database.WeatherDatabase
import com.example.myday.data.repository.CurrentWeatherRepository
import com.example.myday.data.repository.FavoriteWeatherRepository
import com.example.myday.data.repository.ForecastWeatherRepository
import com.example.myday.data.repository.IntroLocationRepository
import com.example.myday.domain.viewmodel.FavoriteViewModel
import com.example.myday.domain.viewmodel.HomeViewModel
import com.example.myday.domain.viewmodel.LocationViewModel
import com.example.myday.domain.viewmodel.SplashViewModel
import com.example.myday.util.dataUtils.SharedPreference
import com.example.myday.util.`object`.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

fun provideInterceptor(): HttpLoggingInterceptor =
    HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

fun provideHttpClient(): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(provideInterceptor())
        .build()
}

val networkModule = module {
    single<ApiService> {

        Retrofit.Builder()
            .baseUrl(Constants.BASE_URl)
            .client(provideHttpClient())
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

    }
}

val databaseModule = module {
    single {
        WeatherDatabase.getInstance(androidContext())
    }
}

val sharedPreferencesModule = module {
    single<SharedPreferences> {
        androidApplication().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    }
}

val sharedPreferenceModule = module {
    single {
        SharedPreference(sharedPreferences = get())
    }
}

val dataSourceModule = module {
    single {
        CurrentWeatherDataSource(apiService = get(), sharedPreference = get(), database = get())
    }
    single {
        ForecastWeatherDataSource(apiService = get(), database = get(), sharedPreference = get())
    }
}

val repositoryModule = module {
    single {
        CurrentWeatherRepository(database = get(), currentWeatherDataSource = get())
    }
    single {
        ForecastWeatherRepository(database = get(), forecastWeatherDataSource = get())
    }
    single {
        IntroLocationRepository(sharedPreference = get())
    }
    single {
        FavoriteWeatherRepository(database = get(), currentWeatherDataSource = get())
    }
}

val viewModelModule = module {
    viewModel {
        SplashViewModel(sharedPreference = get())
    }
    viewModel {
        FavoriteViewModel(favoriteWeatherRepository = get())
    }
    viewModel {
        LocationViewModel(introLocationRepository = get())
    }
    viewModel {
        HomeViewModel(
            currentWeatherRepository = get(),
            forecastWeatherRepository = get()
        )
    }
}

val appModule = listOf(
    networkModule,
    databaseModule,
    sharedPreferencesModule,
    sharedPreferenceModule,
    dataSourceModule,
    repositoryModule,
    viewModelModule
)