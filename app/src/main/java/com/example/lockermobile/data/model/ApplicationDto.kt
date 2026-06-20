package com.example.lockermobile.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApplicationDto(
    val id: String,
    @SerialName("job_id") val jobId: String,
    @SerialName("jobseeker_id") val jobseekerId: String,
    val status: String,
    @SerialName("submitted_at") val submittedAt: String? = null,
    val nik: String? = null,
    @SerialName("tanggal_lahir") val tanggalLahir: String? = null,
    @SerialName("cv_snapshot_url") val cvSnapshotUrl: String? = null,
    @SerialName("ijazah_url") val ijazahUrl: String? = null
)
