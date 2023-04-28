package com.example.myday.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myday.data.model.modelentity.CurrentWeatherData
import com.example.myday.databinding.ResRvFavoriteCityBinding
import com.example.myday.util.dataUtils.CurrentWeatherDiffCallback
import com.example.myday.util.formatting.ImageUtils.Companion.setupGlideByRest

class FavoriteCityAdapter :
    ListAdapter<
            CurrentWeatherData,
            FavoriteCityAdapter.FavoriteCityViewHolder
            >(CurrentWeatherDiffCallback()) {

    private lateinit var itemClickListener: OnItemClickListener
    private lateinit var itemLongClickListener: OnItemLongClickListener

    inner class FavoriteCityViewHolder(
        private val binding: ResRvFavoriteCityBinding,
        clickListener: OnItemClickListener,
        longClickListener: OnItemLongClickListener
        ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                clickListener.onItemCLick(adapterPosition)
            }
            itemView.setOnLongClickListener {
                longClickListener.onLongItemClick(adapterPosition)
                true
            }
        }

        fun bind(currentWeather: CurrentWeatherData) {

            binding.tvTemperatureFavorite.text = currentWeather.temperature
            binding.tvCityNameFavorite.text = currentWeather.city_name
            binding.tvHumidityFavorite.text = currentWeather.humidity
            binding.tvWindFavorite.text = currentWeather.wind

            binding.ivWeatherFavorite.setupGlideByRest(currentWeather.icon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteCityViewHolder {
        return FavoriteCityViewHolder(
            ResRvFavoriteCityBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            ),
            itemClickListener,
            itemLongClickListener
        )
    }

    override fun onBindViewHolder(holder: FavoriteCityViewHolder, position: Int) {

        holder.bind(getItem(position))
    }

    fun setData(newList: List<CurrentWeatherData>) {
        submitList(newList)
    }

    interface OnItemClickListener {
        fun onItemCLick(position: Int)
    }

    interface OnItemLongClickListener {
        fun onLongItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    fun setOnLongItemCLickListener(listener: OnItemLongClickListener) {
        itemLongClickListener = listener
    }
}