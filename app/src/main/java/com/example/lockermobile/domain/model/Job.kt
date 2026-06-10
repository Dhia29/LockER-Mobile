package com.example.lockermobile.domain.model

data class Job(
    val id: String,
    val title: String,
    val companyName: String,
    val location: String,
    val salary: String,
    val type: String, // Full-time, Part-time, etc.
    val logoUrl: String,
    val description: String,
    val postedTime: String,
    val category: String
)
