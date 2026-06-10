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
class EmployerJobsViewModel @Inject constructor(
    private val jobRepository: JobRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(EmployerJobsState())
    val state = _state.asStateFlow()

    init {
        loadJobs()
    }

    private fun loadJobs() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val userEmail = sessionManager.userEmail.first()
            if (userEmail != null) {
                // Mock companyId as 1
                jobRepository.getEmployerJobs("1").collect { jobs ->
                    _state.update { it.copy(isLoading = false, jobs = jobs) }
                }
            }
        }
    }
}
