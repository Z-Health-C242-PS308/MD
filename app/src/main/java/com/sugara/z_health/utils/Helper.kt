package com.sugara.z_health.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import com.sugara.z_health.R
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Calendar

class Helper {
    fun encodeUrl(url: String): String {
        val baseUrl = UrlHelper.STORAGE_URL
        val path = url.removePrefix(baseUrl)
        val encodedPath = URLEncoder.encode(path, StandardCharsets.UTF_8.toString()).replace("+", "%20")
        return "$baseUrl$encodedPath"
    }
    fun getCurrentDate(): String {
        val date = Calendar.getInstance().time
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        return formatter.format(date)
    }

    fun convertToDateFormat(date: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val date = formatter.parse(date)
        val newFormatter = SimpleDateFormat("yyyy-MM-dd")
        return newFormatter.format(date)
    }

    fun generateColor(level: String): Int {
        return when (level) {
            "Low" -> Color.GREEN
            "Medium" -> Color.YELLOW
            "High" -> Color.RED
            else -> Color.RED
        }
    }

    fun convertDateIndo(date: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date = formatter.parse(date)
        val newFormatter = SimpleDateFormat("dd MMMM yyyy")
        return newFormatter.format(date)
    }

    fun convertToDay(date: String): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date = formatter.parse(date)
        val newFormatter = SimpleDateFormat("EEE")
        return newFormatter.format(date)
    }

    fun convertToDecimal(value: Double?): String {
        val nonNullValue = value ?: 0.0
        return if (nonNullValue % 1 == 0.0) {
            nonNullValue.toInt().toString()
        } else {
            nonNullValue.toString()
        }
    }

    fun getDrawableResource(context: Context, key: String): Drawable? {
        val drawableRes = when (key) {
            "Konseling" -> R.drawable.ic_konseling
            "Hotline" -> R.drawable.ic_hotline
            "Sosialisasi" -> R.drawable.afirmasidiri
            "Olahraga" -> R.drawable.ic_sport
            "Tidur" -> R.drawable.ic_sleep
            "Pola Makan" -> R.drawable.ic_food_healt
            "Meditasi" -> R.drawable.ic_meditation
            "Kelola Waktu" -> R.drawable.ic_time_management
            "Menggambar" -> R.drawable.ic_drawing
            "Hobi" -> R.drawable.ic_hobby
            "Media Sosial" -> R.drawable.ic_social_media
            "Kafein dan Alcohol" -> R.drawable.ic_coffe
            "Afirmasi Diri" -> R.drawable.afirmasidiri
            "Atur Keuangan" -> R.drawable.ic_finance
            "Bantuan Profesional" -> R.drawable.ic_professional_help
            "Komunikasi" -> R.drawable.ic_communication
            "Pengobatan" -> R.drawable.ic_medicine
            "Perawatan diri" -> R.drawable.afirmasidiri
            "Batasi Jam Kerja" -> R.drawable.ic_time_management
            "Hubungin teman" -> R.drawable.ic_friendship_call
            "Hubungi orang tua" -> R.drawable.ic_parent_call
            "Atur emosi" -> R.drawable.ic_emotional_management
            "Identifikasi" -> R.drawable.ic_identifikasi
            "Rekomendasi Umum" -> R.drawable.ic_rekomendasi
            else -> R.drawable.ic_rekomendasi
        }
        return drawableRes?.let { ContextCompat.getDrawable(context, it) }
    }
}