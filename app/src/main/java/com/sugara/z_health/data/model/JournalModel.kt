package com.sugara.z_health.data.model

import com.google.gson.annotations.SerializedName

data class JournalModel(

	@field:SerializedName("journal")
	val journal: Journal? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

	data class Journal(

	@field:SerializedName("journal_id")
	val journalId: String? = null,

	@field:SerializedName("waktu_tidur")
	val waktuTidur: Double? = null,

	@field:SerializedName("waktu_belajar")
	val waktuBelajar: Double? = null,

	@field:SerializedName("jurnal_harian")
	val jurnalHarian: String? = null,

	@field:SerializedName("user_id")
	val userId: String? = null,

	@field:SerializedName("created")
	val created: String? = null,

	@field:SerializedName("predict")
	val predict: Predict? = null,

	@field:SerializedName("aktivitas_sosial")
	val aktivitasSosial: Double? = null,

	@field:SerializedName("waktu_belajar_tambahan")
	val waktuBelajarTambahan: Double? = null,

	@field:SerializedName("aktivitas_fisik")
	val aktivitasFisik: Double? = null
)

data class PredictedStress(

	@field:SerializedName("score")
	val score: String? = null,

	@field:SerializedName("stress_level")
	val stressLevel: String? = null
)

data class RecommendationsItem(

	@field:SerializedName("icon")
	val icon: String? = null,

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("title")
	val title: String? = null
)

data class Data(

	@field:SerializedName("recommendations")
	val recommendations: List<RecommendationsItem?>? = null,

	@field:SerializedName("predicted_stress")
	val predictedStress: PredictedStress? = null
)

data class Predict(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("status")
	val status: String? = null
)
