package com.example.lockermobile.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AdminReportsViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(AdminReportsState())
    val state = _state.asStateFlow()

    init {
        loadReports()
    }

    private fun loadReports() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            // Mock reports
            val mockReports = listOf(
                AdminReport("1", "JOB", "Scam Job Listing", "User A", "Suspected fraudulent activity", "PENDING"),
                AdminReport("2", "POST", "Inappropriate Content", "User B", "Harassment", "PENDING"),
                AdminReport("3", "JOB", "Misleading Salary", "User C", "Incorrect info", "RESOLVED")
            )
            _state.update { it.copy(isLoading = false, reports = mockReports) }
        }
    }

    fun resolveReport(id: String) {
        viewModelScope.launch {
            val updated = _state.value.reports.map {
                if (it.id == id) it.copy(status = "RESOLVED") else it
            }
            _state.update { it.copy(reports = updated) }
        }
    }
}
