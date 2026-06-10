package com.example.lockermobile.presentation.admin

import com.example.lockermobile.domain.model.User

data class AdminUsersState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val searchQuery: String = "",
    val error: String? = null
)
