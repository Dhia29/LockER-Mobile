package com.example.lockermobile.data.repository

import com.example.lockermobile.core.data.SessionManager
import com.example.lockermobile.data.local.dao.UserDao
import com.example.lockermobile.data.local.entity.UserEntity
import com.example.lockermobile.domain.model.User
import com.example.lockermobile.domain.model.UserRole
import com.example.lockermobile.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) : AuthRepository {

    override fun login(email: String, password: String): Flow<Result<User>> = flow {
        val userEntity = userDao.getUserByEmail(email)
        if (userEntity != null && userEntity.passwordHash == password) {
            val user = userEntity.toDomain()
            sessionManager.saveSession(user.email, userEntity.role, user.name)
            emit(Result.success(user))
        } else {
            emit(Result.failure(Exception("Invalid email or password")))
        }
    }

    override fun register(name: String, email: String, password: String, role: UserRole): Flow<Result<User>> = flow {
        val existingUser = userDao.getUserByEmail(email)
        if (existingUser != null) {
            emit(Result.failure(Exception("User already exists")))
            return@flow
        }

        val newUser = UserEntity(
            email = email,
            name = name,
            passwordHash = password,
            role = role,
            profilePicture = "https://i.pravatar.cc/150?u=$email",
            bio = "",
            location = "",
            roleTitle = role.name
        )
        userDao.insertUser(newUser)
        sessionManager.saveSession(email, role, name)
        emit(Result.success(newUser.toDomain()))
    }

    override fun logout(): Flow<Unit> = flow {
        sessionManager.clearSession()
        emit(Unit)
    }

    override fun getCurrentUser(): Flow<User?> = flow {
        val email = sessionManager.userEmail.first()
        if (email != null) {
            val entity = userDao.getUserByEmail(email)
            emit(entity?.toDomain())
        } else {
            emit(null)
        }
    }

    private fun UserEntity.toDomain(): User {
        return User(
            id = email, // Using email as ID for mock
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
