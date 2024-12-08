package com.sugara.z_health.data.model

import com.google.gson.annotations.SerializedName

data class JournalWeekModel(

	@field:SerializedName("journal")
	val journal: List<JournalItem?>? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class JournalItem(

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

