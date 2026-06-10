package com.example.lockermobile.data.repository

import com.example.lockermobile.core.data.SessionManager
import com.example.lockermobile.data.local.dao.UserDao
import com.example.lockermobile.data.local.entity.UserEntity
import com.example.lockermobile.domain.model.Experience
import com.example.lockermobile.domain.model.User
import com.example.lockermobile.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) : UserRepository {

    override fun getCurrentUser(): Flow<User?> = flow {
        val email = sessionManager.userEmail.first()
        if (email != null) {
            val entity = userDao.getUserByEmail(email)
            if (entity != null) {
                emit(entity.toDomain())
            } else {
                emit(null)
            }
        } else {
            emit(null)
        }
    }

    override fun logout(): Flow<Unit> = flow {
        sessionManager.clearSession()
        emit(Unit)
    }

    private fun UserEntity.toDomain(): User {
        return User(
            id = email,
            name = name,
            email = email,
            profilePicture = profilePicture,
            role = roleTitle,
            roleEnum = role,
            location = location,
            bio = bio,
            skills = skills,
            experience = experience,
            education = education
        )
    }
}
