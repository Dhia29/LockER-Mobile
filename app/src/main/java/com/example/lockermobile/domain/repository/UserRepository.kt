package com.example.lockermobile.domain.repository

import com.example.lockermobile.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getCurrentUser(): Flow<User?>
    fun logout(): Flow<Unit>
    suspend fun updateProfile(user: User): Result<Unit>
    suspend fun uploadProfilePicture(email: String, imageBytes: ByteArray): Result<String>
}
