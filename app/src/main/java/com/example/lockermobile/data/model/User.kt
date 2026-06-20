package com.example.lockermobile.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    @SerialName("user_id") val userId: String,
    @SerialName("nama_lengkap") val fullName: String,
    @SerialName("bio_diri") val bio: String? = null,
    @SerialName("skill") val skills: String? = null,
    @SerialName("pas_foto") val profilePicture: String? = null,
    @SerialName("phone_number") val phoneNumber: String? = null,
    @SerialName("cv_url") val cvUrl: String? = null,
    @SerialName("alamat_domisili") val address: String? = null
)
