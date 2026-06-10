package com.example.lockermobile.domain.model

data class JobApplication(
    val id: String,
    val jobId: String,
    val jobTitle: String,
    val companyName: String,
    val companyLogo: String,
    val userEmail: String,
    val status: String, // PENDING, ACCEPTED, REJECTED
    val appliedDate: String
)
