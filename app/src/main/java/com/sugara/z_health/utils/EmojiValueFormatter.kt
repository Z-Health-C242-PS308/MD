package com.sugara.z_health.utils

import android.graphics.Color
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.ValueFormatter


class EmojiValueFormatter(private val colors: List<Int>) : ValueFormatter() {
    override fun getBarLabel(barEntry: BarEntry?): String {
        barEntry?.let {
            val index = barEntry.x.toInt() * 2 + 1 // Adjust index to match the actual mood value entry
            if (barEntry.y == 10f) { // Only show emoji for the actual mood value entry
                return when (colors[index]) {
                    Color.GREEN -> "😊"
                    Color.YELLOW -> "😐"
                    Color.RED -> "😢"
                    else -> ""
                }
            }
        }
        return ""
    }
}