package com.example.lockermobile.domain.repository

import com.example.lockermobile.domain.model.User
import com.example.lockermobile.domain.model.UserRole
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(email: String, password: String): Flow<Result<User>>
    fun register(name: String, email: String, password: String, role: UserRole): Flow<Result<User>>
    fun logout(): Flow<Unit>
    fun getCurrentUser(): Flow<User?>
}
