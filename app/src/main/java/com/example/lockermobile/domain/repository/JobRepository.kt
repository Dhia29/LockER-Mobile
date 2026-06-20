package com.example.lockermobile.domain.repository

import com.example.lockermobile.domain.model.Job
import com.example.lockermobile.domain.model.JobApplication
import kotlinx.coroutines.flow.Flow

interface JobRepository {
    fun getJobs(query: String? = null, category: String? = null, type: String? = null): Flow<List<Job>>
    fun getJobById(id: String): Flow<Job?>
    fun getApplications(email: String): Flow<List<JobApplication>>
    suspend fun applyForJob(jobId: String, userEmail: String): Result<Unit>
}
