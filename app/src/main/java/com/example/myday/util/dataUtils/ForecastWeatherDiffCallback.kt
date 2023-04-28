package com.example.myday.util.dataUtils

import androidx.recyclerview.widget.DiffUtil
import com.example.myday.data.model.modelentity.ForecastWeatherData

class ForecastWeatherDiffCallback: DiffUtil.ItemCallback<ForecastWeatherData>() {

    override fun areItemsTheSame(
        oldItem: ForecastWeatherData,
        newItem: ForecastWeatherData
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: ForecastWeatherData,
        newItem: ForecastWeatherData
    ): Boolean {
        return oldItem == newItem
    }
}