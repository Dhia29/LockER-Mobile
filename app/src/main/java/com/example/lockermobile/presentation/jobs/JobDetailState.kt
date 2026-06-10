package com.example.lockermobile.presentation.jobs

import com.example.lockermobile.domain.model.Job

data class JobDetailState(
    val job: Job? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isApplied: Boolean = false
)
