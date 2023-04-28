package com.example.myday.util.`object`

import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.TextStyle
import java.util.Locale

object Calendar {

    fun Long?.formatHour(): String {
        return this?.let {

            val instant = Instant.ofEpochSecond(it)
            val formatter = DateTimeFormatter.ofPattern("HH:mm")
                .withZone(ZoneId.of("UTC-3"))

            return formatter.format(instant)

        } ?: "00:00"
    }

    //Retorna o Dia da semana
    fun getDayOfWeek(): String {
        val dayOfWeek = LocalDate.now().dayOfWeek
        return dayOfWeek.getDisplayName(TextStyle.FULL, Locale("pt", "BR"))
    }
}