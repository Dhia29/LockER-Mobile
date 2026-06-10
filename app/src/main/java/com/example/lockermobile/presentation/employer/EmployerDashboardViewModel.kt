package com.example.lockermobile.presentation.employer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lockermobile.core.data.SessionManager
import com.example.lockermobile.domain.repository.JobRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmployerDashboardViewModel @Inject constructor(
    private val jobRepository: JobRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(EmployerDashboardState())
    val state = _state.asStateFlow()

    init {
        loadDashboardData()
    }

    private fun loadDashboardData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            val userEmail = sessionManager.userEmail.first()
            if (userEmail != null) {
                // Mock companyId as 1 for now
                val companyId = "1" 
                
                combine(
                    jobRepository.getEmployerJobs(companyId),
                    jobRepository.getApplicationsByJob("1") // Placeholder job for stats
                ) { jobs, apps ->
                    _state.update { it.copy(
                        isLoading = false,
                        activeJobs = jobs.size,
                        totalCandidates = apps.size,
                        pendingApplications = apps.count { it.status == "PENDING" }
                    ) }
                }.collect()
            }
        }
    }
}
