package com.example.lockermobile.presentation.home

import com.example.lockermobile.domain.model.Job
import com.example.lockermobile.domain.model.User

data class HomeState(
    val user: User? = null,
    val jobs: List<Job> = emptyList(),
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val selectedCategory: String = "All",
    val error: String? = null
)
