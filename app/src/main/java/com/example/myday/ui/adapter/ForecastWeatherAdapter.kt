package com.example.myday.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myday.data.model.modelentity.ForecastWeatherData
import com.example.myday.databinding.ResRvForecastWeatherBinding
import com.example.myday.util.dataUtils.ForecastWeatherDiffCallback
import com.example.myday.util.formatting.ImageUtils.Companion.setupGlideByRest

class ForecastWeatherAdapter :
    ListAdapter<
        ForecastWeatherData,
        ForecastWeatherAdapter.ForecastWeatherViewHolder
            >(ForecastWeatherDiffCallback()) {

    inner class ForecastWeatherViewHolder(private val binding: ResRvForecastWeatherBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(forecastWeather: ForecastWeatherData){

            binding.tvHour.text = forecastWeather.hour
            binding.ivWeather.setupGlideByRest(forecastWeather.icon)
            binding.tvTemperatureForecast.text = forecastWeather.temperature
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastWeatherViewHolder {
        return ForecastWeatherViewHolder(
            ResRvForecastWeatherBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ForecastWeatherViewHolder, position: Int) {

        holder.bind(getItem(position))
    }

    fun setData(newList: List<ForecastWeatherData>) {
        submitList(newList)
    }
}