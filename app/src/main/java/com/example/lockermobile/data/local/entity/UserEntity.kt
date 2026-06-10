package com.example.lockermobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lockermobile.domain.model.UserRole

import com.example.lockermobile.domain.model.*

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val email: String,
    val name: String,
    val passwordHash: String,
    val role: UserRole,
    val profilePicture: String,
    val bio: String,
    val location: String,
    val roleTitle: String, // e.g., "Mobile Developer"
    val experience: List<Experience> = emptyList(),
    val education: List<Education> = emptyList(),
    val skills: List<String> = emptyList()
)
