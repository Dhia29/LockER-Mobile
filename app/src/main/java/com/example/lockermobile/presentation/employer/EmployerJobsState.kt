package com.example.lockermobile.presentation.employer

import com.example.lockermobile.domain.model.Job

data class EmployerJobsState(
    val isLoading: Boolean = false,
    val jobs: List<Job> = emptyList(),
    val error: String? = null
)
