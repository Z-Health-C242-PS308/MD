package com.sugara.z_health.view.ui.rekomendasi

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.widget.TextView
import com.google.gson.Gson
import com.sugara.z_health.R
import java.io.IOException

class RekomendasiActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecommendationAdapter
    private lateinit var emptyTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rekomendasi)

        recyclerView = findViewById(R.id.rvRekomendasi)
        emptyTextView = findViewById(R.id.tvEmpty)

        recyclerView.layoutManager = LinearLayoutManager(this)

        // Get mood category from intent
        val moodCategory = intent.getStringExtra("MOOD_CATEGORY") ?: "Low"

        // Load JSON data
        val recommendationsJson = loadJSONFromAsset("recommended_activities.json")
        val scoresJson = loadJSONFromAsset("score_data.json")

        val activities = mutableListOf<ActivityItem>()

        if (recommendationsJson != null) {
            val recommendations = Gson().fromJson(recommendationsJson, Recommendation::class.java)

            // Filter activities based on mood category and pair with scores
            activities.addAll(
                when (moodCategory) {
                    "Low" -> recommendations.low
                    "Moderate" -> recommendations.moderate
                    "High" -> recommendations.high
                    else -> listOf()
                }
            )
        } else {
            Log.e("RekomendasiActivity", "Failed to load recommendations JSON!")
            showEmptyState()
        }

        // Load scores from JSON and filter based on selected mood category
        val scores = if (scoresJson != null) {
            Gson().fromJson(scoresJson, ScoreData::class.java)
        } else {
            Log.e("RekomendasiActivity", "Failed to load scores JSON!")
            ScoreData(emptyMap(), emptyMap(), emptyMap())
        }

        // Now, activities contain only the filtered activities based on mood category
        // We can pass both activities and scores to the adapter
        if (activities.isNotEmpty()) {
            emptyTextView.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
            adapter = RecommendationAdapter(activities, this, scores)
            recyclerView.adapter = adapter
        } else {
            showEmptyState()
        }
    }

    private fun loadJSONFromAsset(fileName: String): String? {
        return try {
            val inputStream = assets.open(fileName)
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

    private fun showEmptyState() {
        emptyTextView.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
    }
}
