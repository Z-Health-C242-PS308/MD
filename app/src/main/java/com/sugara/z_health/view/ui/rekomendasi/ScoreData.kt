package com.sugara.z_health.view.ui.rekomendasi

data class ScoreData(
    val low: Map<String, Int>,
    val moderate: Map<String, Int>,
    val high: Map<String, Int>
)
