package com.sugara.z_health.view.ui.rekomendasi

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.sugara.z_health.R
import com.sugara.z_health.utils.Helper

class RecommendationAdapter(
    private val activities: List<ActivityItem>,  // Change Activity to ActivityItem
    private val context: Context,
    private val scores: ScoreData
) : RecyclerView.Adapter<RecommendationAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val description: TextView = itemView.findViewById(R.id.tvDescription)
        val category: TextView = itemView.findViewById(R.id.tvCategory)
        val image: ImageView = itemView.findViewById(R.id.ivIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_recomendation, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val activity = activities[position]
        holder.title.text = activity.title
        holder.description.text = activity.description

        // Display the category and score for the activity
        holder.category.text = when {
            scores.low.containsKey(activity.title) -> "Low: ${scores.low[activity.title]}"
            scores.moderate.containsKey(activity.title) -> "Moderate: ${scores.moderate[activity.title]}"
            scores.high.containsKey(activity.title) -> "High: ${scores.high[activity.title]}"
            else -> "Not Rated"
        }

        // Load the icon for the activity
        val drawable = Helper().getDrawableResource(context, activity.icon)
            ?: ContextCompat.getDrawable(context, R.drawable.ic_menhelt)

        Glide.with(context)
            .load(drawable)
            .apply(RequestOptions().dontTransform())  // Prevent transformations like resizing
            .into(holder.image)
    }

    override fun getItemCount(): Int = activities.size
}
