package com.example.lockermobile.presentation.admin

data class AdminDashboardState(
    val isLoading: Boolean = false,
    val totalUsers: Int = 0,
    val totalJobs: Int = 0,
    val totalPosts: Int = 0,
    val totalReports: Int = 0,
    val error: String? = null
)
