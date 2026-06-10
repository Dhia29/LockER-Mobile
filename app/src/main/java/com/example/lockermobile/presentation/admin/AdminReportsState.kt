package com.example.lockermobile.presentation.admin

data class AdminReport(
    val id: String,
    val type: String, // "JOB", "POST"
    val targetTitle: String,
    val reporterName: String,
    val reason: String,
    val status: String // "PENDING", "RESOLVED"
)

data class AdminReportsState(
    val isLoading: Boolean = false,
    val reports: List<AdminReport> = emptyList(),
    val error: String? = null
)
