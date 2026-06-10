package com.example.lockermobile.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "companies")
data class CompanyEntity(
    @PrimaryKey val id: String,
    val name: String,
    val logoUrl: String,
    val description: String,
    val location: String,
    val industry: String
)
