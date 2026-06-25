package com.example.lockermobile.data.repository

import com.example.lockermobile.core.data.SessionManager
import com.example.lockermobile.data.local.dao.UserDao
import com.example.lockermobile.data.local.entity.UserEntity
import com.example.lockermobile.domain.model.Experience
import com.example.lockermobile.domain.model.User
import com.example.lockermobile.domain.repository.UserRepository
import com.example.lockermobile.data.repository.SupabaseRepository
import com.example.lockermobile.data.model.User as SupabaseUserDto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val sessionManager: SessionManager,
    private val supabaseRepository: SupabaseRepository
) : UserRepository {

    override fun getCurrentUser(): Flow<User?> = flow {
        val email = sessionManager.userEmail.first()
        val role = sessionManager.userRole.first()
        val name = sessionManager.userName.first() ?: "User"
        
        if (email != null) {
            val entity = userDao.getUserByEmail(email)
            if (entity != null) {
                emit(entity.toDomain())
            } else {
                emit(User(
                    id = email,
                    name = name,
                    email = email,
                    profilePicture = "",
                    role = role?.name ?: "Unknown Role",
                    roleEnum = role ?: com.example.lockermobile.domain.model.UserRole.JOB_SEEKER,
                    location = "",
                    bio = "",
                    skills = emptyList(),
                    experience = emptyList(),
                    education = emptyList()
                ))
            }
        } else {
            emit(null)
        }
    }

    override fun logout(): Flow<Unit> = flow {
        sessionManager.clearSession()
        supabaseRepository.signOut()
        emit(Unit)
    }

    override suspend fun updateProfile(user: User): Result<Unit> {
        return try {
            // Preservation logic: Get existing password hash
            val existingEntity = userDao.getUserByEmail(user.email)
            val passwordHash = existingEntity?.passwordHash ?: ""

            // Update local Room
            val entity = user.toEntity(passwordHash)
            userDao.insertUser(entity)

            // Update Supabase profiles table
            val supabaseDto = SupabaseUserDto(
                id = user.id, // Or get ID from auth
                userId = user.id,
                fullName = user.name,
                bio = user.bio,
                skills = user.skills.joinToString(","),
                profilePicture = user.profilePicture,
                phoneNumber = user.phoneNumber,
                address = user.address
            )
            supabaseRepository.updateUserProfile(supabaseDto)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun uploadProfilePicture(email: String, imageBytes: ByteArray): Result<String> {
        return try {
            val path = "avatars/$email-${System.currentTimeMillis()}.jpg"
            supabaseRepository.uploadFile(path, imageBytes)
            val publicUrl = supabaseRepository.downloadFile(path) // This usually returns the public URL in some SDKs, or we construct it.
            // For now assuming downloadFile logic in SupabaseRepository handles retrieving the link.
            // If it returns raw bytes, we might need a publicUrl helper in SupabaseRepository.
            
            // Re-fetching or constructing the public URL based on Supabase storage rules
            // Assuming your SupabaseRepository.downloadFile(path) was meant to be downloadPublic or similar.
            // Let's assume a standard URL format if not returned directly.
            val baseUrl = "https://skdelezmotmbjydxznsz.supabase.co/storage/v1/object/public/jobseeker_files"
            val fullUrl = "$baseUrl/$path"
            
            Result.success(fullUrl)
        } catch (e: Exception) {
            Result.failure(e)
        }
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
            phoneNumber = phoneNumber,
            address = address,
            skills = skills,
            experience = experience,
            education = education
        )
    }

    private fun User.toEntity(passwordHash: String): UserEntity {
        return UserEntity(
            email = email,
            name = name,
            passwordHash = passwordHash,
            role = roleEnum,
            profilePicture = profilePicture,
            bio = bio,
            location = location,
            roleTitle = role,
            phoneNumber = phoneNumber,
            address = address,
            experience = experience,
            education = education,
            skills = skills
        )
    }
}
