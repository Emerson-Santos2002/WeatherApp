package com.example.myday.util.dataUtils

import androidx.recyclerview.widget.DiffUtil
import com.example.myday.data.model.modelentity.CurrentWeatherData

class CurrentWeatherDiffCallback : DiffUtil.ItemCallback<CurrentWeatherData>() {

    override fun areItemsTheSame(
        oldItem: CurrentWeatherData,
        newItem: CurrentWeatherData
    ): Boolean {
        return oldItem.city_name == newItem.city_name
    }

    override fun areContentsTheSame(
        oldItem: CurrentWeatherData,
        newItem: CurrentWeatherData
    ): Boolean {
        return oldItem == newItem
    }
}