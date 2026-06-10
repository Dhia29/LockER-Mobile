package com.example.lockermobile.data.repository

import com.example.lockermobile.data.local.dao.ApplicationDao
import com.example.lockermobile.data.local.dao.JobDao
import com.example.lockermobile.data.local.entity.ApplicationEntity
import com.example.lockermobile.data.local.entity.JobEntity
import com.example.lockermobile.domain.model.Job
import com.example.lockermobile.domain.model.JobApplication
import com.example.lockermobile.domain.repository.JobRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class JobRepositoryImpl @Inject constructor(
    private val jobDao: JobDao,
    private val applicationDao: ApplicationDao
) : JobRepository {

    override fun getJobs(query: String?, category: String?, type: String?): Flow<List<Job>> {
        return jobDao.getAllJobs().map { entities ->
            entities.filter { entity ->
                val matchesQuery = query.isNullOrEmpty() || 
                        entity.title.contains(query, ignoreCase = true) || 
                        entity.companyName.contains(query, ignoreCase = true)
                val matchesCategory = category.isNullOrEmpty() || category == "All" || entity.category == category
                val matchesType = type.isNullOrEmpty() || type == "All" || entity.type == type
                
                matchesQuery && matchesCategory && matchesType
            }.map { it.toDomain() }
        }
    }

    override fun getJobById(id: String): Flow<Job?> {
        return jobDao.getJobById(id).map { it?.toDomain() }
    }

    override fun getApplications(email: String): Flow<List<JobApplication>> {
        return combine(
            applicationDao.getApplicationsByUser(email),
            jobDao.getAllJobs()
        ) { applications, jobs ->
            applications.map { app ->
                val job = jobs.find { it.id == app.jobId }
                JobApplication(
                    id = app.id,
                    jobId = app.jobId,
                    jobTitle = job?.title ?: "Unknown Job",
                    companyName = job?.companyName ?: "Unknown Company",
                    companyLogo = job?.logoUrl ?: "",
                    userEmail = app.userEmail,
                    status = app.status,
                    appliedDate = app.appliedDate
                )
            }
        }
    }

    override suspend fun applyForJob(jobId: String, userEmail: String): Result<Unit> {
        return try {
            val app = ApplicationEntity(
                id = UUID.randomUUID().toString(),
                jobId = jobId,
                userEmail = userEmail,
                status = "PENDING",
                appliedDate = "Today"
            )
            applicationDao.insertApplication(app)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateApplicationStatus(applicationId: String, status: String): Result<Unit> {
        return try {
            val apps = applicationDao.getAllApplications().first()
            val app = apps.find { it.id == applicationId }
            if (app != null) {
                applicationDao.updateApplication(app.copy(status = status))
                Result.success(Unit)
            } else {
                Result.failure(Exception("Application not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getEmployerJobs(companyId: String): Flow<List<Job>> {
        // Mock filtering by companyId
        return jobDao.getAllJobs().map { entities ->
            entities.filter { it.companyId == companyId }.map { it.toDomain() }
        }
    }

    override fun getApplicationsByJob(jobId: String): Flow<List<JobApplication>> {
        return applicationDao.getApplicationsByJob(jobId).map { entities ->
            entities.map { app ->
                // In a real app we'd fetch the user name here
                JobApplication(
                    id = app.id,
                    jobId = app.jobId,
                    jobTitle = "Job $jobId",
                    companyName = "My Company",
                    companyLogo = "",
                    userEmail = app.userEmail,
                    status = app.status,
                    appliedDate = app.appliedDate
                )
            }
        }
    }

    private fun JobEntity.toDomain(): Job {
        return Job(
            id = id,
            title = title,
            companyName = companyName,
            location = location,
            salary = salary,
            type = type,
            logoUrl = logoUrl,
            description = description,
            postedTime = postedTime,
            category = category
        )
    }
}
