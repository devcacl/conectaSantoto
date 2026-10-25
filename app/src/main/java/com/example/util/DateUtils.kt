package com.example.util

import java.util.Calendar

/** Utilidades de fecha compatibles con minSdk 24 (sin java.time). */
object DateUtils {
    private val monthsShort = listOf("ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "DIC")

    private fun fromToday(offsetDays: Int): Calendar =
        Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, offsetDays) }

    /** "2026-10-25" para hoy + [offsetDays]. */
    fun dayText(offsetDays: Int = 0): String = fromToday(offsetDays).let {
        String.format("%04d-%02d-%02d", it.get(Calendar.YEAR), it.get(Calendar.MONTH) + 1, it.get(Calendar.DAY_OF_MONTH))
    }

    /** "18 OCT" para hoy + [offsetDays]. */
    fun badge(offsetDays: Int): String = fromToday(offsetDays).let {
        String.format("%02d %s", it.get(Calendar.DAY_OF_MONTH), monthsShort[it.get(Calendar.MONTH)])
    }

    /** "19 OCT 2026" para hoy + [offsetDays]. */
    fun newsDate(offsetDays: Int): String = fromToday(offsetDays).let {
        "${badge(offsetDays)} ${it.get(Calendar.YEAR)}"
    }

    /** Primer día hábil (lun-vie) a partir de mañana, formato yyyy-MM-dd. */
    fun nextBusinessDay(): String {
        var offset = 1
        while (fromToday(offset).get(Calendar.DAY_OF_WEEK).let { it == Calendar.SATURDAY || it == Calendar.SUNDAY }) offset++
        return dayText(offset)
    }

    /** Próximos [count] días hábiles a partir de mañana. */
    fun upcomingBusinessDays(count: Int): Set<String> {
        val days = linkedSetOf<String>()
        var offset = 1
        while (days.size < count) {
            val dow = fromToday(offset).get(Calendar.DAY_OF_WEEK)
            if (dow != Calendar.SATURDAY && dow != Calendar.SUNDAY) days += dayText(offset)
            offset++
        }
        return days
    }
}
