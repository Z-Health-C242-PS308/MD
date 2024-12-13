package com.sugara.z_health.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.core.content.ContextCompat
import com.sugara.z_health.R
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class Helper {

    private val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())

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
            "Identifikasi" -> R.drawable.ic_rekomendasi
            "Rekomendasi Umum" -> R.drawable.ic_rekomendasi
            else -> R.drawable.ic_rekomendasi
        }
        return drawableRes?.let { ContextCompat.getDrawable(context, it) }
    }

    fun getColorStress(context: Context, level: String): Int {
        return when (level) {
            "Low" -> ContextCompat.getColor(context, R.color.primary)
            "Moderate" -> ContextCompat.getColor(context, R.color.warning)
            "High" -> ContextCompat.getColor(context, R.color.error)
            else -> ContextCompat.getColor(context, R.color.error)
        }
    }
    fun createCustomTempFile(context: Context): File {
        val filesDir = context.externalCacheDir
        return File.createTempFile(timeStamp, ".jpg", filesDir)
    }

    fun uriToFile(imageUri: Uri, context: Context): File {
        val myFile = createCustomTempFile(context)
        val inputStream = context.contentResolver.openInputStream(imageUri) as InputStream
        val outputStream = FileOutputStream(myFile)
        val buffer = ByteArray(1024)
        var length: Int
        while (inputStream.read(buffer).also { length = it } > 0) outputStream.write(buffer, 0, length)
        outputStream.close()
        inputStream.close()
        return myFile
    }
}