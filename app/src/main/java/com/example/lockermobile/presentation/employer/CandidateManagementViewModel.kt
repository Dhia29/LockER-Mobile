package com.example.lockermobile.presentation.employer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lockermobile.domain.repository.JobRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CandidateManagementViewModel @Inject constructor(
    private val jobRepository: JobRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CandidateManagementState())
    val state = _state.asStateFlow()

    init {
        loadApplicants()
    }

    private fun loadApplicants() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            // Mock fetching for job ID "1"
            jobRepository.getApplicationsByJob("1").collect { applicants ->
                _state.update { it.copy(isLoading = false, applicants = applicants) }
            }
        }
    }

    fun updateStatus(applicationId: String, status: String) {
        viewModelScope.launch {
            jobRepository.updateApplicationStatus(applicationId, status).onSuccess {
                loadApplicants()
            }
        }
    }
}
