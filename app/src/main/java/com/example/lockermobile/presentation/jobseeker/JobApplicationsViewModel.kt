package com.example.lockermobile.presentation.jobseeker

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lockermobile.core.data.SessionManager
import com.example.lockermobile.domain.repository.JobRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JobApplicationsViewModel @Inject constructor(
    private val jobRepository: JobRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(JobApplicationsState())
    val state = _state.asStateFlow()

    init {
        loadApplications()
    }

    private fun loadApplications() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            
            val userEmail = sessionManager.userEmail.first()
            if (userEmail != null) {
                jobRepository.getApplications(userEmail).collect { apps ->
                    _state.update { it.copy(
                        isLoading = false,
                        applications = apps
                    ) }
                }
            } else {
                _state.update { it.copy(isLoading = false, error = "User not logged in") }
            }
        }
    }
}
