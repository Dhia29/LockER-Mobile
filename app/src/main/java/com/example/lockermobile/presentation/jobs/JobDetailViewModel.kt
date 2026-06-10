package com.example.lockermobile.presentation.jobs

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lockermobile.core.data.SessionManager
import com.example.lockermobile.domain.repository.JobRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobDetailViewModel @Inject constructor(
    private val repository: JobRepository,
    private val sessionManager: SessionManager,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(JobDetailState())
    val state: StateFlow<JobDetailState> = _state.asStateFlow()

    private val jobId: String? = savedStateHandle["jobId"]

    init {
        getJobDetail()
    }

    private fun getJobDetail() {
        jobId?.let { id ->
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                
                val userEmail = sessionManager.userEmail.first()
                
                combine(
                    repository.getJobById(id),
                    if (userEmail != null) repository.getApplications(userEmail) else flowOf(emptyList())
                ) { job, apps ->
                    val isApplied = apps.any { it.jobId == id }
                    _state.update { it.copy(
                        job = job,
                        isApplied = isApplied,
                        isLoading = false
                    ) }
                }.collect()
            }
        }
    }

    fun applyForJob() {
        viewModelScope.launch {
            val userEmail = sessionManager.userEmail.first()
            val currentJobId = jobId
            if (userEmail != null && currentJobId != null) {
                repository.applyForJob(currentJobId, userEmail).onSuccess {
                    _state.update { it.copy(isApplied = true) }
                }
            }
        }
    }
}
