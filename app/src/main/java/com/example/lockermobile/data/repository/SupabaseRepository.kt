package com.example.lockermobile.data.repository

import com.example.lockermobile.data.remote.SupabaseManager
import com.example.lockermobile.data.model.User
import com.example.lockermobile.data.model.JobPostingDto
import com.example.lockermobile.data.model.ApplicationDto
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.realtime
import io.github.jan.supabase.realtime.selectAsFlow
import io.github.jan.supabase.storage.storage
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// Tables from actual Supabase schema
private const val JOBS_TABLE = "job_postings"
private const val APPLICATIONS_TABLE = "applications"
private const val PROFILES_TABLE = "job_seeker_profiles"
private const val SAVED_JOBS_TABLE = "post_saves"

// Storage bucket
private const val BUCKET_NAME = "jobseeker_files"

@OptIn(io.github.jan.supabase.annotations.SupabaseExperimental::class)
@Singleton
class SupabaseRepository @Inject constructor() {

    private val auth = SupabaseManager.client.auth
    private val postgrest = SupabaseManager.client.postgrest
    private val storage = SupabaseManager.client.storage
    private val realtime = SupabaseManager.client.realtime

    // --- Authentication ---

    suspend fun signUp(email: String, password: String) {
        auth.signUpWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun signIn(email: String, password: String) {
        auth.signInWith(Email) {
            this.email = email
            this.password = password
        }
    }

    suspend fun signOut() {
        auth.signOut()
    }

    fun getCurrentUser() = auth.currentUserOrNull()

    // --- Database (Postgrest) ---

    suspend fun getJobs(): List<JobPostingDto> {
        return postgrest[JOBS_TABLE].select().decodeList<JobPostingDto>()
    }

    suspend fun getJobById(id: String): JobPostingDto {
        return postgrest[JOBS_TABLE].select {
            filter {
                eq("id", id)
            }
        }.decodeSingle<JobPostingDto>()
    }

    suspend fun applyForJob(jobId: String, jobseekerId: String) {
        postgrest[APPLICATIONS_TABLE].insert(mapOf(
            "job_id" to jobId,
            "jobseeker_id" to jobseekerId,
            "status" to "PENDING"
        ))
    }

    suspend fun getApplications(jobseekerId: String): List<ApplicationDto> {
        return postgrest[APPLICATIONS_TABLE].select {
            filter {
                eq("jobseeker_id", jobseekerId)
            }
        }.decodeList<ApplicationDto>()
    }

    suspend fun saveJob(jobId: String, userId: String) {
        postgrest[SAVED_JOBS_TABLE].insert(mapOf(
            "post_id" to jobId,
            "user_id" to userId
        ))
    }

    suspend fun getUserProfile(userId: String): User {
        return postgrest[PROFILES_TABLE].select {
            filter {
                eq("user_id", userId)
            }
        }.decodeSingle<User>()
    }

    suspend fun updateUserProfile(user: User) {
        postgrest[PROFILES_TABLE].update(user) {
            filter {
                eq("user_id", user.userId)
            }
        }
    }

    // --- Storage ---

    suspend fun uploadFile(path: String, data: ByteArray) {
        storage[BUCKET_NAME].upload(path, data)
    }

    suspend fun downloadFile(path: String): ByteArray {
        return storage[BUCKET_NAME].downloadPublic(path)
    }

    suspend fun deleteFile(path: String) {
        storage[BUCKET_NAME].delete(path)
    }

    // --- Realtime ---

    fun observeJobChanges(): Flow<List<JobPostingDto>> {
        return postgrest[JOBS_TABLE].selectAsFlow(JobPostingDto::id)
    }
}
