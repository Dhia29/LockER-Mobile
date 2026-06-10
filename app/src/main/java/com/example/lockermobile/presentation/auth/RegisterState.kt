package com.example.lockermobile.presentation.auth

import com.example.lockermobile.domain.model.UserRole

data class RegisterState(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val role: UserRole = UserRole.JOB_SEEKER,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRegisterSuccess: Boolean = false
)
