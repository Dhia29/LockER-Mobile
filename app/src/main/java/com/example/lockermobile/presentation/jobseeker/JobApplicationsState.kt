package com.example.lockermobile.presentation.jobseeker

import com.example.lockermobile.domain.model.JobApplication

data class JobApplicationsState(
    val isLoading: Boolean = false,
    val applications: List<JobApplication> = emptyList(),
    val error: String? = null
)
