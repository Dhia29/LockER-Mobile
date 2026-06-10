package com.example.lockermobile.presentation.employer

import com.example.lockermobile.domain.model.JobApplication

data class CandidateManagementState(
    val isLoading: Boolean = false,
    val applicants: List<JobApplication> = emptyList(),
    val error: String? = null
)
