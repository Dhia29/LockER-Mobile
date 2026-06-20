package com.example.lockermobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jobs")
data class JobEntity(
    @PrimaryKey val id: String,
    val companyId: String,
    val title: String,
    val companyName: String, // Denormalized for easier access
    val location: String,
    val salary: String,
    val type: String,
    val logoUrl: String,
    val description: String,
    val postedTime: String,
    val category: String
)
