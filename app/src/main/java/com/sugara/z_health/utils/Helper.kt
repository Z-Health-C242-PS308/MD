package com.sugara.z_health.utils

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.core.content.ContextCompat
import com.sugara.z_health.R
import java.io.File
import java.io.IOException
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

    fun loadJSONFromAsset(context: Context, fileName: String): String? {
        return try {
            val inputStream = context.assets.open(fileName)
            val size = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            inputStream.close()
            String(buffer, Charsets.UTF_8)
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
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

    fun getFileFromUri(context: Context, uri: Uri): File {
        val inputStream = context.contentResolver.openInputStream(uri)
        val tempFile = File.createTempFile("upload", null, context.cacheDir)
        tempFile.outputStream().use { outputStream ->
            inputStream?.copyTo(outputStream)
        }
        return tempFile
    }



    fun getDrawableResource(context: Context, key: String): Drawable? {
        return when (key) {
            "Konseling" -> ContextCompat.getDrawable(context, R.drawable.ic_counseling)
            "Hotline" -> ContextCompat.getDrawable(context, R.drawable.ic_hotline)
            "Sosialisasi" -> ContextCompat.getDrawable(context, R.drawable.ic_socialization)
            "Olahraga" -> ContextCompat.getDrawable(context, R.drawable.ic_sports)
            "Tidur" -> ContextCompat.getDrawable(context, R.drawable.ic_sleep)
            "Pola Makan" -> ContextCompat.getDrawable(context, R.drawable.ic_food)
            "Meditasi" -> ContextCompat.getDrawable(context, R.drawable.ic_meditation)
            "Kelola Waktu" -> ContextCompat.getDrawable(context, R.drawable.ic_time_management)
            "Menggambar" -> ContextCompat.getDrawable(context, R.drawable.ic_drawing)
            "Hobi" -> ContextCompat.getDrawable(context, R.drawable.ic_hobby)
            "Media Sosial" -> ContextCompat.getDrawable(context, R.drawable.ic_sosmed)
            "Kafein dan Alcohol" -> ContextCompat.getDrawable(context, R.drawable.ic_caffeine_alcohol)
            "Afirmasi Diri" -> ContextCompat.getDrawable(context, R.drawable.ic_self_affirmation)
            "Atur Keuangan" -> ContextCompat.getDrawable(context, R.drawable.ic_finance_management)
            "Bantuan Profesional" -> ContextCompat.getDrawable(context, R.drawable.ic_professional_help)
            "Komunikasi" -> ContextCompat.getDrawable(context, R.drawable.ic_communication)
            "Pengobatan" -> ContextCompat.getDrawable(context, R.drawable.ic_medication)
            "Perawatan Diri" -> ContextCompat.getDrawable(context, R.drawable.ic_self_care)
            "Batasi Jam Kerja" -> ContextCompat.getDrawable(context, R.drawable.ic_time_management)
            "Hubungi Teman" -> ContextCompat.getDrawable(context, R.drawable.ic_contact_friends)
            "Hubungi Orang Tua" -> ContextCompat.getDrawable(context, R.drawable.ic_contact_parents)
            "Atur Emosi" -> ContextCompat.getDrawable(context, R.drawable.ic_emotion_control)
            "Identifikasi" -> ContextCompat.getDrawable(context, R.drawable.ic_identification)
            "Rekomendasi Umum" -> ContextCompat.getDrawable(context, R.drawable.ic_general_recommendation)
            else -> null // Kembalikan null jika tidak ada yang cocok
        }
    }


}