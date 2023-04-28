package com.example.myday.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myday.R
import com.example.myday.data.model.modelentity.CurrentWeatherData
import com.example.myday.data.model.modelentity.ForecastWeatherData
import com.example.myday.databinding.FragmentHomeBinding
import com.example.myday.ui.adapter.ForecastWeatherAdapter
import com.example.myday.domain.viewmodel.HomeViewModel
import com.example.myday.domain.viewmodel.SplashViewModel
import com.example.myday.util.dataUtils.WeatherResource
import com.example.myday.util.formatting.ImageUtils.Companion.setupGlide
import com.example.myday.util.formatting.ImageUtils.Companion.setupGlideByRest
import com.example.myday.util.`object`.Constants
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModel()
    private val splashViewModel: SplashViewModel by viewModel()
    private val binding by lazy { FragmentHomeBinding.inflate(layoutInflater) }

    private lateinit var adapter: ForecastWeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        splashViewModel.saveFirstAppLaunch()
        setupUI()

        viewLifecycleOwner.lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.STARTED) {

                homeViewModel.currentWeather.collect { uiState ->
                    when(uiState) {
                        is WeatherResource.Loading -> currentWeatherLoading()
                        is WeatherResource.Success -> uiState.data?.let { currentWeatherSuccess(it) }
                        is WeatherResource.Failure -> uiState.throwable?.let { currentWeatherFailure(it) }
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {

            repeatOnLifecycle(Lifecycle.State.STARTED) {

                homeViewModel.forecastWeather.collect { uiState ->
                    when(uiState) {
                        is WeatherResource.Loading -> forecastWeatherLoading()
                        is WeatherResource.Success -> uiState.data?.let { forecastWeatherSuccess(it) }
                        is WeatherResource.Failure -> uiState.throwable?.let { forecastWeatherFailure(it) }
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        homeViewModel.startCurrentWeatherRequests()
    }

    override fun onStop() {
        super.onStop()
        homeViewModel.cancelRequests()
    }

    private fun setupUI() {

        adapter = ForecastWeatherAdapter()

        binding.ivHumidity.setupGlide(R.drawable.ic_humidity)
        binding.ivVisibility.setupGlide(R.drawable.ic_visibility)
        binding.ivWindSpeed.setupGlide(R.drawable.ic_wind)

        binding.rvForecastWeather.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = this@HomeFragment.adapter
        }

    }

    private fun currentWeatherLoading() {

        binding.ivLoading.visibility = View.VISIBLE
        binding.btnTryAgain.visibility = View.GONE
        binding.vgBelowImage.visibility = View.GONE
        binding.ivLoading.setupGlide(R.raw.ic_loading)
    }

    @SuppressLint("SetTextI18n")
    private fun currentWeatherSuccess(data: CurrentWeatherData) {

        binding.ivLoading.visibility = View.GONE
        binding.btnTryAgain.visibility = View.GONE
        binding.vgBelowImage.visibility = View.VISIBLE

        binding.ivWeather.setupGlideByRest(data.icon)
        binding.tvCityName.text = data.city_name
        binding.tvHourDayOfWeek.text = "${data.hour} - ${data.day_week}"
        binding.tvTemperature.text = data.temperature
        binding.tvDescription.text = data.description
        binding.tvHumidity.text = data.humidity
        binding.tvVisibility.text = data.visibility
        binding.tvWindSpeed.text = data.wind
    }

    private fun currentWeatherFailure(code: Int) {

        binding.ivLoading.visibility = View.GONE
        binding.btnTryAgain.visibility = View.VISIBLE
        binding.vgBelowImage.visibility = View.GONE

        binding.ivWeather.setupGlideByRest("18d")
        errorMessage(code)
    }

    private fun forecastWeatherLoading() {

        binding.rvForecastWeather.visibility = View.GONE
        binding.ivRvLoading.visibility = View.VISIBLE
        binding.ivRvLoading.setupGlide(R.raw.ic_loading)
    }
    private fun forecastWeatherSuccess(forecastWeatherDataList: List<ForecastWeatherData>) {

        binding.rvForecastWeather.visibility = View.VISIBLE
        binding.ivRvLoading.visibility = View.GONE
        adapter.setData(forecastWeatherDataList)
    }

    private fun forecastWeatherFailure(code: Int) {

        binding.rvForecastWeather.visibility = View.GONE
        binding.ivRvLoading.visibility = View.GONE
        errorMessage(code)
    }

    private fun errorMessage(code: Int) {

        val message: String = when(code) {
            Constants.EMPTY_BODY -> getString(R.string.error_1)
            Constants.ERROR_API_KEY -> getString(R.string.error_401)
            Constants.ERROR_INVALID_CITY -> getString(R.string.error_404)
            Constants.CALL_LIMIT_REACHED -> getString(R.string.error_429)
            else -> getString(R.string.error_else)
        }

        val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setIcon(R.drawable.ic_error_48dp)
            .setTitle(getString(R.string.info_title_error))
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(getString(R.string.confirm)) { _, _ -> }
            .create()

        alertDialog.show()
    }
}