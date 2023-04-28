package com.example.myday.util.formatting

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.myday.R
import com.example.myday.WeatherApplication

class ImageUtils {

    companion object {

         private fun String.getImage(): Drawable? {

            val context = WeatherApplication.instance.applicationContext

            val resourceId = when(this) {

                "01d" -> R.drawable.weather_clear_sky_day
                "01n" -> R.drawable.weather_clear_sky_night

                "02d" -> R.drawable.weather_few_clouds_day
                "02n" -> R.drawable.weather_few_clouds_night

                "03d", "03n" -> R.drawable.weather_scattered_clouds
                "04d", "04n" -> R.drawable.weather_broken_clouds

                "09d" -> R.drawable.weather_drizzle_rain
                "10d", "10n" -> R.drawable.weather_rain
                "11d" -> R.drawable.weather_thunderstorm

                else -> R.drawable.weather_thunderstorm
            }

            return ContextCompat.getDrawable(context, resourceId)

        }

        fun ImageView.setupGlide(imageResId: Int) {
            Glide.with(this)
                .load(imageResId)
                .into(this)
        }

        fun ImageView.setupGlideByRest(icon: String? = null, drawableResId: Int? = null){

            val requestOptions = RequestOptions()
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)

            icon?.let {
                Glide.with(context)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(it.getImage())
                    .into(this)
            }
                ?: Glide.with(context)
                    .applyDefaultRequestOptions(requestOptions)
                    .load(drawableResId)
                    .into(this)

        }
    }
}