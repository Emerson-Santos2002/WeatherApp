package com.example.myday.util.formatting

class StringUtils {

    companion object{

        fun Float.formatTemperature() : String{
            return this.toInt().toString()
        }

        fun Short.formatVisibility() : String{
            return this.div(1000).toString().plus("Km")
        }

        fun String.formatWind() : String{
            val formatPattern = "Km/h"
            return this.plus(formatPattern)
        }

        fun String.formatPercent() : String{
            val formatPattern = "%"
            return this.plus(formatPattern)
        }
    }
}