package com.example.lockermobile.data.local.dao

import androidx.room.*
import com.example.lockermobile.data.local.entity.JobEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JobDao {
    @Query("SELECT * FROM jobs")
    fun getAllJobs(): Flow<List<JobEntity>>

    @Query("SELECT * FROM jobs WHERE id = :id")
    fun getJobById(id: String): Flow<JobEntity?>

    @Query("SELECT * FROM jobs WHERE title LIKE '%' || :query || '%' OR companyName LIKE '%' || :query || '%'")
    fun searchJobs(query: String): Flow<List<JobEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJobs(jobs: List<JobEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJob(job: JobEntity)
    
    @Delete
    suspend fun deleteJob(job: JobEntity)
}
