package com.example.lockermobile.presentation.profile

import com.example.lockermobile.domain.model.User

data class ProfileState(
    val user: User? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
