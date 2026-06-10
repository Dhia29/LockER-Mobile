package com.example.lockermobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "applications")
data class ApplicationEntity(
    @PrimaryKey val id: String,
    val jobId: String,
    val userEmail: String,
    val status: String, // "PENDING", "ACCEPTED", "REJECTED"
    val appliedDate: String
)
