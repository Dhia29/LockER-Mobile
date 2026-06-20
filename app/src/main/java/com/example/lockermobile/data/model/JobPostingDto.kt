package com.example.lockermobile.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JobPostingDto(
    val id: String,
    @SerialName("company_id") val companyId: String,
    val judul: String,
    val deskripsi: String,
    val kategori: String,
    val lokasi: String,
    val deadline: String? = null,
    val status: String,
    @SerialName("created_at") val createdAt: String? = null
)
