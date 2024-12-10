package com.sugara.z_health.view.ui.rekomendasi


import com.google.gson.annotations.SerializedName
data class Recommendation(
    @SerializedName("Low") val low: List<ActivityItem>,
    @SerializedName("Moderate") val moderate: List<ActivityItem>,
    @SerializedName("High") val high: List<ActivityItem>
)
