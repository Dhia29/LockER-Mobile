package com.example.lockermobile.presentation.employer

data class PostJobState(
    val title: String = "",
    val category: String = "",
    val type: String = "Full-time",
    val location: String = "",
    val salary: String = "",
    val description: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
