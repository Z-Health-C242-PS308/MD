package com.sugara.z_health.view.ui.journal

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class JournalItem(
    val studyHours: Int,
    val extracurricularHours: Int,
    val sleepHours: Int,
    val socialHours: Int,
    val physicalActivityHours: Int
) : Parcelable