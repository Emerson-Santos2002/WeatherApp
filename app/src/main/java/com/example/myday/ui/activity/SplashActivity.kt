package com.example.myday.ui.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.myday.data.model.modelentity.ForecastWeatherData
import com.example.myday.domain.viewmodel.HomeViewModel
import com.example.myday.domain.viewmodel.SplashViewModel
import com.example.myday.util.dataUtils.WeatherResource
import com.example.myday.util.`object`.Network
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private val splashViewModel : SplashViewModel by viewModel()
    private val homeViewModel : HomeViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {

            if (!Network.isNetworkAvailable(applicationContext)) startActivity(NetworkErrorActivity::class.java)
            else if (splashViewModel.iFirstTime) startActivity(MyIntroActivity::class.java)
            else {
                val content: View = findViewById(android.R.id.content)
                content.viewTreeObserver.addOnDrawListener {

                    object : ViewTreeObserver.OnPreDrawListener {
                        override fun onPreDraw(): Boolean {

                            return if (homeViewModel.forecastWeather.value == WeatherResource.Loading<List<ForecastWeatherData>>()) {

                                content.viewTreeObserver.removeOnPreDrawListener(this)
                                true
                            }
                            else false
                        }
                    }
                }
                startActivity(MainActivity::class.java)
            }
        }
        splashScreen.setKeepOnScreenCondition{ false }
    }

    private fun startActivity(classScreen: Class<*>) {
        val intent = Intent(this, classScreen)
        startActivity(intent)
        finish()
    }
}