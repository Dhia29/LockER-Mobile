package com.example.lockermobile.domain.repository

import com.example.lockermobile.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getCurrentUser(): Flow<User?>
    fun logout(): Flow<Unit>
}
