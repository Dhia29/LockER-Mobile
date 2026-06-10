package com.example.lockermobile.presentation.employer

data class EmployerDashboardState(
    val isLoading: Boolean = false,
    val activeJobs: Int = 0,
    val totalCandidates: Int = 0,
    val pendingApplications: Int = 0,
    val error: String? = null
)
