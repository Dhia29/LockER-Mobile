package com.example.lockermobile.domain.model

import kotlinx.serialization.Serializable

data class User(
    val id: String,
    val name: String,
    val email: String,
    val profilePicture: String,
    val role: String, // e.g., "Mobile Developer"
    val roleEnum: UserRole = UserRole.JOB_SEEKER,
    val location: String,
    val bio: String,
    val experience: List<Experience> = emptyList(),
    val education: List<Education> = emptyList(),
    val skills: List<String> = emptyList()
)

@Serializable
data class Experience(
    val title: String,
    val company: String,
    val duration: String,
    val description: String
)

@Serializable
data class Education(
    val school: String,
    val degree: String,
    val year: String
)
